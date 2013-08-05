package fi.foyt.fni.view.gamelibrary;

import java.io.FileNotFoundException;
import java.util.Arrays;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;

import fi.foyt.fni.gamelibrary.PublicationController;
import fi.foyt.fni.gamelibrary.GameLibraryTagController;
import fi.foyt.fni.persistence.model.gamelibrary.GameLibraryTag;

@RequestScoped
@Named
@Stateful
@URLMappings(mappings = {
  @URLMapping(
		id = "gamelibrary-publication-tag-list", 
		pattern = "/gamelibrary/tags/#{publicationTagListBackingBean.tag}", 
		viewId = "/gamelibrary/publicationtaglist.jsf"
  )
})
public class PublicationTagListBackingBean extends AbstractPublicationListBackingBean {
	
	@Inject
	private PublicationController publicationController;
	
	@Inject
	private GameLibraryTagController gameLibraryTagController;

	@URLAction
	public void init() throws FileNotFoundException {
		gameLibraryTag = gameLibraryTagController.findTagByText(tag);
		if (gameLibraryTag == null) {
			throw new FileNotFoundException();
		}
		
		setPublications(publicationController.listProductsByTags(Arrays.asList(gameLibraryTag)));
	}
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public GameLibraryTag getGameLibraryTag() {
		return gameLibraryTag;
	}

	private String tag;
	private GameLibraryTag gameLibraryTag;
}