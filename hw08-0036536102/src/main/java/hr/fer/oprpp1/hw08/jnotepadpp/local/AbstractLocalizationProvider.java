package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLocalizationProvider implements ILocalizationProvider{

	private List<ILocalizationListener> promatraci = new ArrayList<>();
	

	public AbstractLocalizationProvider() {
		
	}
	
	public void fire() {
		for(ILocalizationListener l : promatraci) {
			l.localizationChanged();
		}
	}
	
	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		promatraci.remove(l);
	}

	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		promatraci.add(listener);
		
	}

}
