package fi.foyt.fni.system;

import java.util.List;
import java.util.Locale;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fi.foyt.fni.persistence.dao.common.CountryDAO;
import fi.foyt.fni.persistence.dao.common.LanguageDAO;
import fi.foyt.fni.persistence.dao.system.SystemSettingDAO;
import fi.foyt.fni.persistence.model.common.Country;
import fi.foyt.fni.persistence.model.common.Language;
import fi.foyt.fni.persistence.model.system.SystemSetting;
import fi.foyt.fni.persistence.model.system.SystemSettingKey;
import fi.foyt.fni.users.UserController;

@RequestScoped
@Stateful
public class SystemSettingsController {
	
	private static final String DEFAULT_COUNTRY_CODE = "FI";

	@Inject
	private SystemSettingDAO systemSettingDAO;

	@Inject
	private LanguageDAO languageDAO;

	@Inject
	private CountryDAO countryDAO;

	@Inject
	private UserController userController;
	
	public String getSetting(SystemSettingKey key) {
		SystemSetting systemSetting = systemSettingDAO.findByKey(key);
		if (systemSetting != null)
			return systemSetting.getValue();

		return null;
	}

	public Integer getIntegerSetting(SystemSettingKey key) {
		return NumberUtils.createInteger(getSetting(key));
	}

	public Long getLongSetting(SystemSettingKey key) {
		return NumberUtils.createLong(getSetting(key));
	}

	public Locale getLocaleSetting(SystemSettingKey key) {
		String setting = getSetting(key);
		return LocaleUtils.toLocale(setting);
	}

	public Locale getDefaultLocale() {
		return getLocaleSetting(SystemSettingKey.DEFAULT_LOCALE);
	}

	public void updateSetting(SystemSettingKey key, String value) {
		SystemSetting systemSetting = systemSettingDAO.findByKey(key);
		if (systemSetting == null)
			systemSetting = systemSettingDAO.create(key, value);
		else
			systemSetting = systemSettingDAO.updateValue(systemSetting, value);
	}

	public String getTheme() {
		return "default_dev";
	}

	public String getThemePath(HttpServletRequest request) {
		return request.getContextPath() + "/themes/" + getTheme();
	}
	
	public List<Language> listLanguages() {
		return languageDAO.listAll();
	}
	
	public List<Language> listLocalizedLanguages() {
		return languageDAO.listByLocalized(Boolean.TRUE);
	}

	public Country findCountryById(Long id) {
		return countryDAO.findById(id);
	}

	public Country findCountryByCode(String code) {
		return countryDAO.findByCode(code);
	}

	public Country getDefaultCountry() {
		return findCountryByCode(DEFAULT_COUNTRY_CODE);
	}
	
	public List<Country> listCountries() {
		return countryDAO.listAll();
	}
}
