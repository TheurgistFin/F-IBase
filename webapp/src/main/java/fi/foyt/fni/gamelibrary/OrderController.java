package fi.foyt.fni.gamelibrary;

import java.util.Date;
import java.util.List;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import fi.foyt.fni.persistence.dao.gamelibrary.OrderDAO;
import fi.foyt.fni.persistence.dao.gamelibrary.OrderItemDAO;
import fi.foyt.fni.persistence.model.gamelibrary.BookPublication;
import fi.foyt.fni.persistence.model.gamelibrary.Order;
import fi.foyt.fni.persistence.model.gamelibrary.OrderItem;
import fi.foyt.fni.persistence.model.gamelibrary.OrderStatus;
import fi.foyt.fni.persistence.model.gamelibrary.OrderType;
import fi.foyt.fni.persistence.model.gamelibrary.Publication;
import fi.foyt.fni.persistence.model.illusion.IllusionEvent;
import fi.foyt.fni.persistence.model.users.Address;
import fi.foyt.fni.persistence.model.users.User;
import fi.foyt.fni.session.SessionController;
	
public class OrderController {

  @Inject
	private SessionController sessionController;

  @Inject
	private PublicationController publicationController;

	@Inject
	private Event<OrderCreatedEvent> orderCreatedEvent;
	
	@Inject
	private Event<OrderPaidEvent> orderPaidEvent;

	@Inject
	private Event<OrderWaitingForDeliveryEvent> orderWaitingForDeliveryEvent;

	@Inject
	private Event<OrderCanceledEvent> orderCanceledEvent;

  @Inject
  private Event<OrderShippedEvent> orderShippedEvent;

  @Inject
  private Event<OrderDeliveredEvent> orderDeliveredEvent;
  
	@Inject
	private OrderDAO orderDAO;
	
	@Inject
	private OrderItemDAO orderItemDAO;

	/* Order */
	
	public Order createOrder(User customer, String accessKey, String customerCompany, String customerEmail, String customerFirstName, String customerLastName, String customerMobile, String customerPhone, OrderStatus orderStatus, OrderType type, Double shippingCosts, String notes, Address deliveryAddress) {
		Date now = new Date();
		Order order = orderDAO.create(customer, accessKey, customerCompany, customerEmail, customerFirstName, customerLastName, customerMobile, customerPhone, orderStatus, type, shippingCosts, notes, deliveryAddress, now, null, null, null, null);
		orderCreatedEvent.fire(new OrderCreatedEvent(sessionController.getLocale(), order.getId()));
		return order;
	}

	public Order findOrderById(Long orderId) {
		return orderDAO.findById(orderId);
	}
	
  public List<Order> listOrdersByStatus(OrderStatus status) {
    return orderDAO.listByOrderStatus(status);
  }
  
	public List<Order> listOrdersByPublication(Publication publication) {
		return orderItemDAO.listOrdersByPublication(publication);
	}

	public Order updateOrderAsPaid(Order order) {
	  if (order.getOrderStatus() != OrderStatus.PAID) {
		  Date now = new Date();
  		order = orderDAO.updateOrderStatus(orderDAO.updatePaid(order, now), OrderStatus.PAID);
  		orderPaidEvent.fire(new OrderPaidEvent(sessionController.getLocale(), order.getId()));
	  }
	  
		return order;
	}

	public Order updateOrderAsWaitingForDelivery(Order order) {
    if (order.getOrderStatus() != OrderStatus.WAITING_FOR_DELIVERY) {
		  Date now = new Date();
  		order = orderDAO.updateOrderStatus(orderDAO.updatePaid(order, now), OrderStatus.WAITING_FOR_DELIVERY);
	  	orderWaitingForDeliveryEvent.fire(new OrderWaitingForDeliveryEvent(sessionController.getLocale(), order.getId()));
    }
    
		return order;
	}
	
	public Order updateOrderAsCanceled(Order order) {
	  if (order.getOrderStatus() != OrderStatus.CANCELED) {
  		Date now = new Date();
  		order = orderDAO.updateOrderStatus(orderDAO.updateCanceled(order, now), OrderStatus.CANCELED);
  		orderCanceledEvent.fire(new OrderCanceledEvent(sessionController.getLocale(), order.getId()));
	  }
	  
		return order;
	}

  public Order updateOrderAsShipped(Order order) {
    if (order.getOrderStatus() != OrderStatus.SHIPPED) {
      Date now = new Date();
      order = orderDAO.updateOrderStatus(orderDAO.updateShipped(order, now), OrderStatus.SHIPPED);
      
      List<OrderItem> orderItems = orderItemDAO.listByOrder(order);
      for (OrderItem orderItem : orderItems) {
        if (orderItem.getPublication() instanceof BookPublication) {
          BookPublication bookPublication = (BookPublication) orderItem.getPublication();
          publicationController.incBookPublicationPrintCount(bookPublication);
        }
      }
      
      orderShippedEvent.fire(new OrderShippedEvent(sessionController.getLocale(), order.getId()));
    }
    
    return order;
  }

  public Order updateOrderAsDelivered(Order order) {
    if (order.getOrderStatus() != OrderStatus.DELIVERED) {
      Date now = new Date();
      order = orderDAO.updateOrderStatus(orderDAO.updateDelivered(order, now), OrderStatus.DELIVERED);
      orderDeliveredEvent.fire(new OrderDeliveredEvent(sessionController.getLocale(), order.getId()));
    }
    
    return order;
  }
	
	/* OrderItem */
	
	public OrderItem createOrderItem(Order order, Publication publication, IllusionEvent illusionEvent, String name, Double unitPrice, Integer count) {
		return orderItemDAO.create(order, publication, illusionEvent, name, unitPrice, count);
	}

	public List<OrderItem> listOrderItems(Order order) {
		return orderItemDAO.listByOrder(order);
	}

  public OrderItem findOrderItemByIllusionEventNotNull(Order order) {
    for (OrderItem item : listOrderItems(order)) {
      if (item.getIllusionEvent() != null) {
        return item;
      }
    }
    
    return null;
  }
  
  public IllusionEvent findOrderIllusionEvent(Order order) {
    OrderItem orderItem = findOrderItemByIllusionEventNotNull(order);
    if (orderItem != null) {
      return orderItem.getIllusionEvent();
    }
    
    return null;
  }
	
}
