package hr.fer.oprpp1.hw08.jnotepadpp.local;

public class LocalizationProviderBridge extends AbstractLocalizationProvider{
	
	private ILocalizationProvider parent;
	private boolean connected;
	private String currentLanguage;
	ILocalizationListener l = new ILocalizationListener() {

		@Override
		public void localizationChanged() {
			LocalizationProviderBridge.this.currentLanguage = parent.getLanguage();
			LocalizationProviderBridge.this.fire();
		}};

	
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.connected = false;
		this.parent = parent;
		this.currentLanguage = parent.getLanguage();
	}
	
	public void disconnect() {
		this.parent.removeLocalizationListener(l);
		this.connected = false;
	}
		
	public void connect() {
		if(!connected) {
			if(!this.parent.getLanguage().equals(this.currentLanguage)) {
				this.currentLanguage = this.parent.getLanguage();
				this.fire();
			}
			this.parent.addLocalizationListener(l);
		    this.connected = true;
		}
	}

	@Override
	public String getString(String key) {
		return parent.getString(key);
	}

	@Override
	public String getLanguage() {
		return this.currentLanguage;
	}

}
