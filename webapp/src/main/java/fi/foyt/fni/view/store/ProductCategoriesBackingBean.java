package fi.foyt.fni.view.store;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import fi.foyt.fni.gamelibrary.StoreTagController;
import fi.foyt.fni.persistence.model.gamelibrary.StoreTag;

@RequestScoped
@Named
@Stateful
public class ProductCategoriesBackingBean {
	
	@Inject
	private StoreTagController storeTagController;

	@PostConstruct
	public void init() {
		tags = storeTagController.listActiveStoreTags();
	}
	
	public List<StoreTag> getTags() {
		return tags;
	}
	
	public void setTags(List<StoreTag> tags) {
		this.tags = tags;
	}
	
	private List<StoreTag> tags;
	
}