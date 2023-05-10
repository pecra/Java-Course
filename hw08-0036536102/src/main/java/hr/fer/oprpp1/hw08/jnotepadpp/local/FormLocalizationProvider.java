package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class FormLocalizationProvider extends LocalizationProviderBridge{

	public FormLocalizationProvider(ILocalizationProvider parent, JFrame frame) {
		super(parent);
		frame.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				FormLocalizationProvider.this.connect();
			}
			public void windowClosed(WindowEvent e) {
				FormLocalizationProvider.this.disconnect();
			}
		});
	}

}
