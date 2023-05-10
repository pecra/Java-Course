package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

public abstract class LocalizableAction extends AbstractAction{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	private String description;
	private String OK;
	private String porukaSaveAs;
	private String title;

	public LocalizableAction(String key, String description, ILocalizationProvider lp) {
		String k = lp.getString(key);
		this.key = key;
		this.description = description;
		this.putValue(key, k);
		lp.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				LocalizableAction.this.putValue(Action.NAME, lp.getString(LocalizableAction.this.key));
				LocalizableAction.this.putValue(Action.SHORT_DESCRIPTION,lp.getString(LocalizableAction.this.description));
			};
		}
		 );
		
	}
	

}
