package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.Component;
import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageIcon zelena;
	private ImageIcon crvena;
	private List<SingleDocumentModel> list = new ArrayList<>();
	SingleDocumentModel current;
	int listlength = 0;
	private List<MultipleDocumentListener> promatraci = new ArrayList<>();
	private LocalizationProvider l = LocalizationProvider.getInstance();
	
	public DefaultMultipleDocumentModel() {
		super();
		this.loadCurrent();
		try {
			this.loadIcons();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void loadCurrent() {
		this.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int index = DefaultMultipleDocumentModel.this.getSelectedIndex();
				if(index != -1) {
					SingleDocumentModel s = DefaultMultipleDocumentModel.this.list.get(index);
					if(listlength == list.size()) {
					for(MultipleDocumentListener l : promatraci) {
						l.currentDocumentChanged(current,s);
					}
					} else {
						listlength++;
					}
					DefaultMultipleDocumentModel.this.current = s;
				}
				else {
					DefaultMultipleDocumentModel.this.current = null;
				}
			}
			
		});
	}
	void loadIcons() throws Exception {
		InputStream is = this.getClass().getResourceAsStream("icons/disketaZ.jpg");
		if(is==null) {
			throw new Exception();
		}
		byte[] bytes = is.readAllBytes();
		this.zelena = new ImageIcon(new ImageIcon(bytes).getImage().getScaledInstance(16,16, Image.SCALE_SMOOTH));
		
		is = this.getClass().getResourceAsStream("icons/disketaC.jpg");
		if(is==null) {
			throw new Exception();
		}
		bytes = is.readAllBytes();
		this.crvena = new ImageIcon(new ImageIcon(bytes).getImage().getScaledInstance(16,16, Image.SCALE_SMOOTH));
	}
	
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return list.iterator();
	}

	@Override
	public JComponent getVisualComponent() {
		return this;
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel novi = new DefaultSingleDocumentModel(null,null);
		for(MultipleDocumentListener l : promatraci) {
				l.documentAdded(novi);
			
		}
		this.list.add(novi);
		this.addTab("undefined",new JScrollPane(novi.getTextComponent()));
		int index = this.getComponentCount()-1;
		this.setSelectedIndex(index);
		this.setIconAt(index, zelena);
		novi.addSingleDocumentListener(new SingleDocumentListener() {

			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				int index = DefaultMultipleDocumentModel.this.getIndexOfDocument(model);
				if(DefaultMultipleDocumentModel.this.getIconAt(index) == zelena) {
					DefaultMultipleDocumentModel.this.setIconAt(index, crvena);
				}
				else {
					DefaultMultipleDocumentModel.this.setIconAt(index, zelena);
				}
			}

			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				Path p = model.getFilePath();
				String s = "undefined";
				if(p != null) {
					s = p.getFileName().toString();
				}
				int index = DefaultMultipleDocumentModel.this.getIndexOfDocument(model);
				DefaultMultipleDocumentModel.this.setTitleAt(index, s);
			}
			
		});
		return novi;
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return this.current;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		if(path == null) {
			throw new IllegalArgumentException();
		}
		if(this.list.contains(new DefaultSingleDocumentModel(path," "))) {
			
			for(MultipleDocumentListener l : promatraci) {
				l.currentDocumentChanged(current,this.findForPath(path));
			}
			int j = this.getIndexOfDocument(this.findForPath(path));
			this.setSelectedIndex(j);
			this.current = this.findForPath(path);
			return this.findForPath(path);
		}
		else {
			
	    	byte[] okteti;
	    	try {
	    		okteti = Files.readAllBytes(path);
	    	} catch(Exception ex) {
	    		String[] options = new String[1];
				options[0] = l.getString("OK").toString();
	    		JOptionPane.showOptionDialog(
	    				DefaultMultipleDocumentModel.this, 
	    				String.format(l.getString("readingErr"),path.toAbsolutePath()),
	    				l.getString("error"), 
	    				JOptionPane.DEFAULT_OPTION,
	    				JOptionPane.ERROR_MESSAGE,null,options,options[0]);
	    		return null;
	    	}
	    	String tekst = new String(okteti, StandardCharsets.UTF_8);
			
			DefaultSingleDocumentModel d =new DefaultSingleDocumentModel(path,tekst);
			this.list.add(d);
			this.addTab(d.getFilePath().getFileName().toString(),new JScrollPane(d.getTextComponent()));

			for(MultipleDocumentListener l : promatraci) {
				if(this.getCurrentDocument() == null) {
					l.documentAdded(d);
				}
				else {
					l.currentDocumentChanged(current, d);
				}
			}
			int index = this.getComponentCount()-1;
			this.setSelectedIndex(index);
			this.setIconAt(index, zelena);
			this.setToolTipTextAt(index,d.getFilePath().toAbsolutePath().toString() );
			d.addSingleDocumentListener(new SingleDocumentListener() {

				@Override
				public void documentModifyStatusUpdated(SingleDocumentModel model) {
					int index = DefaultMultipleDocumentModel.this.getIndexOfDocument(model);
					if(DefaultMultipleDocumentModel.this.getIconAt(index) == zelena) {
						DefaultMultipleDocumentModel.this.setIconAt(index, crvena);
					}
					else {
						DefaultMultipleDocumentModel.this.setIconAt(index, zelena);
					}
				}

				@Override
				public void documentFilePathUpdated(SingleDocumentModel model) {
					int index = DefaultMultipleDocumentModel.this.getIndexOfDocument(model);
					DefaultMultipleDocumentModel.this.setTitleAt(index, model.getFilePath().getFileName().toString());
					
				}
				
			});
			return d;
		}
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		if(this.current.getFilePath()!= null && newPath != null && !this.current.getFilePath().equals(newPath) && !(this.findForPath(newPath)==null)) {
			throw new IllegalArgumentException();
		}
		if(newPath == null) {
			newPath = model.getFilePath();
		}
		File f = newPath.toFile();
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte[] podatci = model.getTextComponent().getText().getBytes(StandardCharsets.UTF_8);
		try {
			Files.write(newPath, podatci);
		} catch (IOException e1) {
			String[] options = new String[1];
			options[0] = l.getString("OK").toString();
    		JOptionPane.showOptionDialog(
    				DefaultMultipleDocumentModel.this, 
    				String.format(l.getString("writingErr"),newPath.toFile().getAbsolutePath()),
    				l.getString("error"), 
    				JOptionPane.DEFAULT_OPTION,
    				JOptionPane.ERROR_MESSAGE,null,options,options[0]);
			return;
		}
		String[] options = new String[1];
		options[0] = l.getString("OK").toString();
		JOptionPane.showOptionDialog(
				DefaultMultipleDocumentModel.this, 
				l.getString("snimljena"),
				l.getString("title"), 
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE,null,options,options[0]);
		model.setFilePath(newPath);
		int index = this.getSelectedIndex();
		this.setToolTipTextAt(index,model.getFilePath().toString());
		model.setModified(false);
		this.current = model;
	}
	

	@Override
	public void closeDocument(SingleDocumentModel model) {
			int index = this.getSelectedIndex();
			this.list.remove(index);
			this.removeTabAt(index);
			if(this.list.size() > 0) {
				this.current = list.get(this.getSelectedIndex());
				for(MultipleDocumentListener l : promatraci) {
					l.currentDocumentChanged(model, current);
					l.documentRemoved(model);
				}
			}
			else {
				for(MultipleDocumentListener l : promatraci) {
					l.documentRemoved(model);
				}
				this.current = null;
			}
			this.listlength--;
			return;
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		this.promatraci.add(l);
		
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		this.promatraci.remove(l);
		
	}

	@Override
	public int getNumberOfDocuments() {
		return this.list.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return list.get(index);
	}

	@Override
	public SingleDocumentModel findForPath(Path path) {
		if(path == null) {
			throw new IllegalArgumentException("path cant be null");
		}
		for(SingleDocumentModel s: list) {
			if(((DefaultSingleDocumentModel)s).p != null && ((DefaultSingleDocumentModel)s).p.equals(path)) {
				return s;
			}
		}
		return null;
	}

	@Override
	public int getIndexOfDocument(SingleDocumentModel doc) {
		return this.list.indexOf(doc);
	}
	

}

