package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider {

	private String language;
	private ResourceBundle bundle;
	private static final LocalizationProvider instance = new LocalizationProvider();
	
	private LocalizationProvider() {
		this.language = "en";
		Locale locale = Locale.forLanguageTag(language);
    	this.bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.local.prijevodi", locale);
	}
	
	public static LocalizationProvider getInstance() {
		 return instance;
	}
	
	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	@Override
	public String getLanguage() {
		return this.language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
		Locale locale = Locale.forLanguageTag(language);
    	this.bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.local.prijevodi", locale);
    	this.fire();
	}

}
