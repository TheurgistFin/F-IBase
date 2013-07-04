package fi.foyt.fni.persistence.model.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceException;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

@Entity
public class MultilingualString {
	
	public Long getId() {
		return id;
	}
	
	public LocalizedString getDefaultString() {
		return defaultString;
	}
	
	public void setDefaultString(LocalizedString defaultString) {
		this.defaultString = defaultString;
	}
	
	public List<LocalizedString> getStrings() {
		return strings;
	}
	
	public void setStrings(List<LocalizedString> strings) {
		this.strings = strings;
	}
	
	public void addString(LocalizedString localizedString) {
    if (this.strings.contains(localizedString)) {
      throw new PersistenceException("MultilingualString already contains this LocalizedString");
    } else {
      if (localizedString.getMultilingualString() != null) {
      	localizedString.getMultilingualString().removeString(localizedString);
      }
      
      localizedString.setMultilingualString(this);
    }
  }

  public void removeString(LocalizedString localizedString) {
    if (!this.strings.contains(localizedString)) {
      throw new PersistenceException("MultilingualString does not contain this LocalizedString");
    } else {
    	 localizedString.setMultilingualString(null);
    }
  }
	
	@Transient
	public String getDefaultValue() {
		if (getDefaultString() != null)
  		return getDefaultString().getValue();
		
		return null;
	}
	
	@Transient
	public String getValue(Locale locale) {
		for (LocalizedString string : getStrings()) {
			if (string.getLocale().equals(locale))
				return string.getValue();
		}
		
		if (StringUtils.isNotBlank(locale.getCountry())) {
			String value = getValue(new Locale(locale.getLanguage()));
			if (value != null)
				return value;
		}
		
		return getDefaultValue();
	}

	@Transient
	public Map<Locale, String> toMap() {
		Map<Locale, String> result = new HashMap<>();
		
		for (LocalizedString string : getStrings()) {
			result.put(string.getLocale(), string.getValue());
		}		
		
		return Collections.unmodifiableMap(result);
	}

	@Id
  @GeneratedValue (strategy=GenerationType.IDENTITY)
  private Long id;
	
	@ManyToOne
	private LocalizedString defaultString;
	
	@OneToMany (mappedBy = "multilingualString")
	private List<LocalizedString> strings = new ArrayList<>();
}
