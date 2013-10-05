package fi.foyt.fni.licences;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

public class CreativeCommonsLicense {
	
	public static final String URL_PREFIX = "http://creativecommons.org/licenses/";
	public static final String ICON_PREFIX = "http://i.creativecommons.org/l/";
	public static final String NORMAL_ICON = "88x31.png";
	public static final String COMPACT_ICON = "80x15.png";
		
	public CreativeCommonsLicense(String[] properties, String version, String jurisdiction) {
		this.properties = properties;
		this.version = version;
		this.jurisdiction = jurisdiction;
	}
	
	public String getIconUrl(boolean compact) {
		StringBuilder urlBuilder = new StringBuilder();
		
		urlBuilder.append(ICON_PREFIX);
		urlBuilder.append(StringUtils.join(properties, '-'));
		
		if (StringUtils.isNotBlank(version)) {
		  urlBuilder.append('/');
		  urlBuilder.append(version);
		}
		
		if (StringUtils.isNotBlank(jurisdiction)) {
			urlBuilder.append('/');
			urlBuilder.append(jurisdiction);
		}
		
		urlBuilder.append('/');
		
		if (compact) {
			urlBuilder.append(COMPACT_ICON);
		} else {
			urlBuilder.append(NORMAL_ICON);
		}
		
		return urlBuilder.toString();
	}
	
	public String getIconUrl() {
		return getIconUrl(false);
	}
	
	public String getCompactIconUrl() {
		return getIconUrl(true);
	}
	
	/**
	 * Returns whether 
	 * 
	 * @return
	 */
	public boolean getPublicDomain() {
		return properties[0].equals("publicdomain");
	}
	
	public boolean getShareAlike() {
		return (!getPublicDomain()) && ArrayUtils.contains(properties, "sa");
	}
	
	public boolean getDerivatives() {
		return getPublicDomain() || !ArrayUtils.contains(properties, "nd");
	}
	
	public boolean getCommercial() {
		return getPublicDomain() || !ArrayUtils.contains(properties, "nc");
	}
	
	public boolean getAttribution() {
		return (!getPublicDomain()) && ArrayUtils.contains(properties, "by");
	}
	
	public String getUrl() {
		StringBuilder urlBuilder = new StringBuilder();
		
		urlBuilder.append(URL_PREFIX);
		urlBuilder.append(StringUtils.join(properties, '-'));
		
		if (StringUtils.isNotBlank(version)) {
  		urlBuilder.append('/');
	  	urlBuilder.append(version);
		}
		
		if (StringUtils.isNotBlank(jurisdiction)) {
			urlBuilder.append('/');
			urlBuilder.append(jurisdiction);
		}
		
		return urlBuilder.toString();
	}
	
	private String[] properties;
	private String version;
	private String jurisdiction;
}
