package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DefaultSingleDocumentModel implements SingleDocumentModel{

	Path p;
	String content;
	JTextArea text;
	boolean modified = false;
	private List<SingleDocumentListener> promatraci = new ArrayList<>();
	
	private class DocListener implements DocumentListener{

		@Override
		public void insertUpdate(DocumentEvent e) {
			if(!DefaultSingleDocumentModel.this.isModified()) {
			DefaultSingleDocumentModel.this.setModified(true);
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			DefaultSingleDocumentModel.this.setModified(true);
		}
		
	}
	public DefaultSingleDocumentModel(Path p, String content) {
		this.p = p;
		this.content = content;
		text = new JTextArea();
		text.setText(content);
		text.getDocument().addDocumentListener(new DocListener());
	}
	public JTextArea getTextComponent() {
		return text;
	}

	public Path getFilePath() {
		return this.p;
	}

	public void setFilePath(Path path) {
		if(path == null) {
			throw new IllegalArgumentException();
		}
		this.p = path;
		for(SingleDocumentListener l : promatraci) {
			l.documentFilePathUpdated(this);
		}
	}
	

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
		for(SingleDocumentListener l : promatraci) {
			l.documentModifyStatusUpdated(this);
		}
	}

	public void addSingleDocumentListener(SingleDocumentListener l) {
		promatraci.add(l);
		
	}

	public void removeSingleDocumentListener(SingleDocumentListener l) {
		promatraci.remove(l);
		
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(this.p == null) {
			if(((DefaultSingleDocumentModel)obj).p != null) {
				return false;
			}
			if(((DefaultSingleDocumentModel)obj) == this) {
				return true;
			}
			else {
				return false;
			}
		}
		if(((DefaultSingleDocumentModel)obj).p == null) {
			if(this.p != null) {
				return false;
			}
			return true;
		}
		return this.p.toString().equals(((DefaultSingleDocumentModel)obj).p.toString());
	}

}
