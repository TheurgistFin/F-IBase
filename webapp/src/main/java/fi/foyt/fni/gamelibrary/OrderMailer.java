package fi.foyt.fni.gamelibrary;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.mail.MessagingException;

import org.apache.commons.lang3.StringUtils;

import fi.foyt.fni.i18n.ExternalLocales;
import fi.foyt.fni.mail.Mailer;
import fi.foyt.fni.persistence.model.gamelibrary.Order;
import fi.foyt.fni.persistence.model.gamelibrary.OrderItem;
import fi.foyt.fni.persistence.model.gamelibrary.OrderType;
import fi.foyt.fni.persistence.model.system.SystemSettingKey;
import fi.foyt.fni.system.SystemSettingsController;

@ApplicationScoped
public class OrderMailer {
	
	@Inject
	private Logger logger;

  @Inject
	private Mailer mailer;
	
	@Inject
	private OrderController orderController;

	@Inject
	private SystemSettingsController systemSettingsController;
	
	public void onOrderShipped(@Observes OrderShippedEvent event) {
	  if (event.getOrderId() != null) {
      Order order = orderController.findOrderById(event.getOrderId()); 
      if (order != null) {
        if (order.getType() == OrderType.GAMELIBRARY_BOOK) {
          Locale locale = event.getLocale();
          String customerName = order.getCustomerFirstName() + ' ' + order.getCustomerLastName();
          String customerEmail = order.getCustomerEmail();
          
          List<OrderItem> items = orderController.listOrderItems(order);
          
          StringBuilder contentBuilder = new StringBuilder();
          contentBuilder.append(getLocalizedValue(locale, "gamelibrary.mail.shipped.contentGreeting", customerName, order.getId()));
          contentBuilder.append(getLocalizedValue(locale, "gamelibrary.mail.shipped.contentText"));
          
          if (order.getDeliveryAddress() != null) {
            String streetAddress = order.getDeliveryAddress().getStreet1();
            if (StringUtils.isNotBlank(order.getDeliveryAddress().getStreet2())) {
              streetAddress += '\n' + order.getDeliveryAddress().getStreet2();
            }
            contentBuilder.append(getLocalizedValue(locale, "gamelibrary.mail.shipped.contentAddress", streetAddress, order.getDeliveryAddress().getPostalCode() + ' ' + order.getDeliveryAddress().getCity(), order.getDeliveryAddress().getCountry().getName()));
          }
          
          Double totalCosts = 0d;
          
          StringBuilder itemsList = new StringBuilder();
          for (int i = 0, l = items.size(); i < l; i++) {
            OrderItem item = items.get(i);
            itemsList.append(item.getCount());
            itemsList.append(" x ");
            itemsList.append(item.getName());
            if (i < (l - 1)) {
              itemsList.append('\n');
            }
            
            totalCosts += item.getUnitPrice() * item.getCount();
          }
          
          if (order.getShippingCosts() != null) {
            totalCosts += order.getShippingCosts();
          }
          
          NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(locale);
          currencyInstance.setCurrency(Currency.getInstance("EUR"));
          String totalCostsFormatted = currencyInstance.format(totalCosts);
  
          double vatPercent = systemSettingsController.getVatPercent();
          double tax = totalCosts - (totalCosts / (1 + (vatPercent / 100)));
          
          String taxAmount = currencyInstance.format(tax);
          String vatText = null;
          
          if (!systemSettingsController.isVatRegistered()) {
            vatText = getLocalizedValue(locale, "gamelibrary.mail.notVatRegistered");
          } else {
            vatText = getLocalizedValue(locale, "gamelibrary.mail.vatPercentage", tax);
          }
          
          contentBuilder.append(getLocalizedValue(locale, "gamelibrary.mail.shipped.contentProducts", itemsList.toString(), totalCostsFormatted, taxAmount, vatText));
          contentBuilder.append(getLocalizedValue(locale, "gamelibrary.mail.shipped.contentFooter"));
          
          String subject = getLocalizedValue(locale, "gamelibrary.mail.shipped.subject");
          String content = contentBuilder.toString();
          
          notifyShopOwner("Fwd: " + subject, content);
          notifyCustomer(customerName, customerEmail, subject, content);
        }
      } else {
        logger.severe("Tried to mail 'waiting for delivery' mail for non-existing order");
      }
    } else {
      logger.severe("Tried to mail 'waiting for delivery' mail for non-existing orderId");
    }
	}

	public void onOrderWaitingForDelivery(@Observes OrderWaitingForDeliveryEvent event) {
		if (event.getOrderId() != null) {
  		Order order = orderController.findOrderById(event.getOrderId()); 
  		if (order != null) {
        if (order.getType() == OrderType.GAMELIBRARY_BOOK) {
    		  Locale locale = event.getLocale();
    		  String customerName = order.getCustomerFirstName() + ' ' + order.getCustomerLastName();
    	    String customerEmail = order.getCustomerEmail();
    	    
    		  List<OrderItem> items = orderController.listOrderItems(order);
    		  
    	    StringBuilder contentBuilder = new StringBuilder();
    	    contentBuilder.append(getLocalizedValue(locale, "gamelibrary.mail.waitingForDelivery.contentGreeting", customerName, order.getId()));
    	    contentBuilder.append(getLocalizedValue(locale, "gamelibrary.mail.waitingForDelivery.contentText"));
    	            
    	    if (order.getDeliveryAddress() != null) {
    	      String streetAddress = order.getDeliveryAddress().getStreet1();
    	      if (StringUtils.isNotBlank(order.getDeliveryAddress().getStreet2())) {
    	        streetAddress += '\n' + order.getDeliveryAddress().getStreet2();
    	      }
    	      contentBuilder.append(getLocalizedValue(locale, "gamelibrary.mail.waitingForDelivery.contentAddress", streetAddress, order.getDeliveryAddress().getPostalCode() + ' ' + order.getDeliveryAddress().getCity(), order.getDeliveryAddress().getCountry().getName()));
    	    }
    	    
    	    Double totalCosts = 0d;
    	    
    	    StringBuilder itemsList = new StringBuilder();
    	    for (int i = 0, l = items.size(); i < l; i++) {
    	      OrderItem item = items.get(i);
    	      itemsList.append(item.getCount());
    	      itemsList.append(" x ");
    	      itemsList.append(item.getName());
    	      if (i < (l - 1)) {
    	        itemsList.append('\n');
    	      }
    	      
    	      totalCosts += item.getUnitPrice() * item.getCount();
    	    }
    	    
    	    if (order.getShippingCosts() != null) {
    	      totalCosts += order.getShippingCosts();
    	    }
    	    
    	    NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(locale);
    	    currencyInstance.setCurrency(Currency.getInstance("EUR"));
    	    String totalCostsFormatted = currencyInstance.format(totalCosts);
    	    
    	    double vatPercent = systemSettingsController.getVatPercent();
          double tax = totalCosts - (totalCosts / (1 + (vatPercent / 100)));
          
          String taxAmount = currencyInstance.format(tax);
          String vatText = null;
          
          if (!systemSettingsController.isVatRegistered()) {
            vatText = getLocalizedValue(locale, "gamelibrary.mail.notVatRegistered");
          } else {
            vatText = getLocalizedValue(locale, "gamelibrary.mail.vatPercentage", tax);
          }
    	    
    	    contentBuilder.append(getLocalizedValue(locale, "gamelibrary.mail.waitingForDelivery.contentProducts", itemsList.toString(), totalCostsFormatted, taxAmount, vatText));
    	    contentBuilder.append(getLocalizedValue(locale, "gamelibrary.mail.waitingForDelivery.contentFooter"));
    	    
    	    String subject = getLocalizedValue(locale, "gamelibrary.mail.waitingForDelivery.subject");
    		  String content = contentBuilder.toString();
    	    
          notifyShopOwner("Fwd: " + subject, content);
    			notifyCustomer(customerName, customerEmail, subject, content);
        }
  		} else {
  			logger.severe("Tried to mail 'waiting for delivery' mail for non-existing order");
  		}
		} else {
			logger.severe("Tried to mail 'waiting for delivery' mail for non-existing orderId");
		}
	}

  private void notifyCustomer(String customerName, String customerEmail, String subject, String content) {
    String fromName = systemSettingsController.getSetting(SystemSettingKey.GAMELIBRARY_ORDERMAILER_NAME);
    String fromMail = systemSettingsController.getSetting(SystemSettingKey.GAMELIBRARY_ORDERMAILER_MAIL);
    
    try {
      mailer.sendMail(fromMail, fromName, customerEmail, customerName, subject, content, "text/plain");
    } catch (MessagingException e) {
    	logger.log(Level.SEVERE, "Failed to send email to customer", e);
    }
  }
  
  private void notifyShopOwner(String subject, String content) {
    String shopOwnerName = systemSettingsController.getSetting(SystemSettingKey.GAMELIBRARY_SHOP_OWNER_NAME);
    String shopOwnerEmail = systemSettingsController.getSetting(SystemSettingKey.GAMELIBRARY_SHOP_OWNER_MAIL);
    String fromName = systemSettingsController.getSetting(SystemSettingKey.GAMELIBRARY_ORDERMAILER_NAME);
    String fromMail = systemSettingsController.getSetting(SystemSettingKey.GAMELIBRARY_ORDERMAILER_MAIL);
    
    try {
      mailer.sendMail(fromMail, fromName, shopOwnerEmail, shopOwnerName, subject, content, "text/plain");
    } catch (MessagingException e) {
      logger.log(Level.SEVERE, "Failed to send email to shop owner", e);
    }
  }

	private String getLocalizedValue(Locale locale, String key, Object... params) {
		return ExternalLocales.getText(locale, key, params);
	}

}
