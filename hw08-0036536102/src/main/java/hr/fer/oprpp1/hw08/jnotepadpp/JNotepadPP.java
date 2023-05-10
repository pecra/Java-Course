package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Collator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import hr.fer.oprpp1.hw08.jnotepadpp.local.*;

public class JNotepadPP extends JFrame {

	private static final long serialVersionUID = 1L;
	private MultipleDocumentModel mdm;
	 
	private boolean closing = false;
	private FormLocalizationProvider alp;
	private JMenuItem uppercase,lowercase,invertcase,ascending,descending,unique,sv;
	
	public JNotepadPP() {
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocation(0, 0);
		setSize(600, 600);
        alp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
        System.out.println(alp);
		initGUI();
		this.setTitle("JNotepad++");
	}
	
	private void initGUI() {
		
		mdm = new DefaultMultipleDocumentModel();
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(mdm.getVisualComponent(), BorderLayout.CENTER);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new GridLayout(1,2));
		JLabel lengt = new JLabel("");
		lengt.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
		JLabel col = new JLabel("");
		col.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
		JLabel time = new JLabel("", SwingConstants.RIGHT);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); 
		Thread t = new Thread(()->{
			while(true) {
				try {
					Thread.sleep(500);
				} catch(Exception ex) {}
				if(closing) {
					break;
				}
				SwingUtilities.invokeLater(()->{
						time.setText(dtf.format(LocalDateTime.now()));
				});
			}
		});
		t.setDaemon(true);
		t.start();
		
		statusPanel.add(lengt);
		statusPanel.add(col);
		statusPanel.add(time);
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 16));	
		
		((DefaultMultipleDocumentModel)mdm).addChangeListener(new ChangeListener() {
			 public void stateChanged(ChangeEvent e) {
				
	             Component c = ((DefaultMultipleDocumentModel) mdm).getSelectedComponent();
	             DefaultSingleDocumentModel d = (DefaultSingleDocumentModel) ((DefaultMultipleDocumentModel) mdm).getCurrentDocument();
	             if(d != null) {
	            	 Path p = d.getFilePath();
	            	 if(p != null) {
	            		 JNotepadPP.this.setTitle(p.toString()+ "- JNotepad++");
	            	 }
	            	 else {
	            	 JNotepadPP.this.setTitle("(unnamed) - JNotepad++");
	            	 }
	             }
	             else {
	            	 JNotepadPP.this.setTitle("JNotepad++");
	             }
			     }
			});
		ChangeListener[] lista = ((DefaultMultipleDocumentModel)mdm).getChangeListeners();
		ChangeListener ch1 = lista[1];
		ChangeListener ch2 = lista[2];
		((DefaultMultipleDocumentModel)mdm).removeChangeListener(ch1);
		((DefaultMultipleDocumentModel)mdm).removeChangeListener(ch2);
		((DefaultMultipleDocumentModel)mdm).addChangeListener(ch2);
		((DefaultMultipleDocumentModel)mdm).addChangeListener(ch1);
		mdm.addMultipleDocumentListener(new MultipleDocumentListener() {

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				JTextComponent old = previousModel.getTextComponent();
			
				for(CaretListener l :old.getCaretListeners()) {
					old.removeCaretListener(l);
				}
			/*	for(DocumentListener l :((AbstractDocument) old.getDocument()).getDocumentListeners()) {
					((AbstractDocument) old.getDocument()).removeDocumentListener(l);
				}*/
				JTextComponent c = currentModel.getTextComponent();
				int len = (int) c.getText().chars().count();
			    lengt.setText(String.format(" lenght:%d", len));
				c.getDocument().addDocumentListener(new DocumentListener() {

					@Override
					public void insertUpdate(DocumentEvent e) {
						int len = (int) c.getText().chars().count();
					    lengt.setText(String.format(" lenght:%d", len));
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						int len = (int) c.getText().chars().count();
					    lengt.setText(String.format(" lenght:%d", len));
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						int len = (int) c.getText().chars().count();
					    lengt.setText(String.format(" lenght:%d", len));
					}
					
				});
				c.addCaretListener(new CaretListener() {
					@Override
					public void caretUpdate(CaretEvent e) {
						JTextArea c = (JTextArea)e.getSource();
						
						int dot = e.getDot();
			            int mark = e.getMark();
			            if (dot == mark) {
			            	unique.setEnabled(false);
                            ascending.setEnabled(false);
                            descending.setEnabled(false);
			                uppercase.setEnabled(false);
			                lowercase.setEnabled(false);
			                invertcase.setEnabled(false);
			            }
			            else{
			            	unique.setEnabled(true);
			            	ascending.setEnabled(true);
                            descending.setEnabled(true);
			            	uppercase.setEnabled(true);
			                lowercase.setEnabled(true);
			                invertcase.setEnabled(true);
			            }
			            
						int linenum = 1;
		                int columnnum = 1;
		                int sellen = Math.abs(c.getCaret().getDot()-c.getCaret().getMark());
		                try {
		                    int caretpos = c.getCaretPosition();
		                    linenum = c.getLineOfOffset(caretpos);

		                    columnnum = caretpos - c.getLineStartOffset(linenum);

		                    linenum += 1;
		                }
		                catch(Exception ex) { }
		                col.setText(String.format(" Ln:%d Col:%d Sel:%d", linenum,columnnum+1,sellen));
					}
				});
				c.setCaretPosition(len);
			}

			@Override
			public void documentAdded(SingleDocumentModel model) {
				JTextComponent c = model.getTextComponent();
				int len = (int) c.getText().chars().count();
			    lengt.setText(String.format(" lenght:%d", len));
				c.getDocument().addDocumentListener(new DocumentListener() {

					@Override
					public void insertUpdate(DocumentEvent e) {
						int len = (int) c.getText().chars().count();
					    lengt.setText(String.format(" lenght:%d", len));
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						int len = (int) c.getText().chars().count();
					    lengt.setText(String.format(" lenght:%d", len));
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						int len = (int) c.getText().chars().count();
					    lengt.setText(String.format(" lenght:%d", len));
					}
					
				});
				c.addCaretListener(new CaretListener() {
					@Override
					public void caretUpdate(CaretEvent e) {
						int dot = e.getDot();
			            int mark = e.getMark();
			            if (dot == mark) {
			            	unique.setEnabled(false);
			            	ascending.setEnabled(false);
                            descending.setEnabled(false);
			                uppercase.setEnabled(false);
			                lowercase.setEnabled(false);
			                invertcase.setEnabled(false);
			            }
			            else{
			            	unique.setEnabled(true);
			            	ascending.setEnabled(true);
                            descending.setEnabled(true);
			            	uppercase.setEnabled(true);
			                lowercase.setEnabled(true);
			                invertcase.setEnabled(true);
			            }
						JTextArea c = (JTextArea)e.getSource();
						int linenum = 1;
		                int columnnum = 1;
		                int sellen = Math.abs(c.getCaret().getDot()-c.getCaret().getMark());
		                try {
		                    int caretpos = c.getCaretPosition();
		                    linenum = c.getLineOfOffset(caretpos);
		                    columnnum = caretpos - c.getLineStartOffset(linenum);

		                    linenum += 1;
		                }
		                catch(Exception ex) { }
		                col.setText(String.format(" Ln:%d Col:%d Sel:%d", linenum,columnnum+1,sellen));
					}
				});
				c.setCaretPosition(0);
				
			}

			@Override
			public void documentRemoved(SingleDocumentModel model) {
				JTextComponent old = model.getTextComponent();
				for(CaretListener l :old.getCaretListeners()) {
					old.removeCaretListener(l);
				}
				/*for(DocumentListener l :((AbstractDocument) old.getDocument()).getDocumentListeners()) {
					((AbstractDocument) old.getDocument()).removeDocumentListener(l);
				}*/
				
			}
			
		});
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				JNotepadPP.this.close();
			}
			
		});
		initActions();
		createActions();
		createMenus();
		createToolbars();
		
	}




private Action exitAction;
private Action copyAction;
private Action cutAction;
private Action pasteAction;
private Action statisticalAction;
private Action loadDocumentAction;
private Action openDocumentAction;
private Action closeDocumentAction;
private Action saveDocumentAction;
private Action saveAsDocumentAction;
private Action toggleCaseAction;
private Action upperCaseAction;
private Action lowerCaseAction;
private Action ascendingAction;
private Action descendingAction;
private Action uniqueAction;

protected void close() {
	for(int i = 0;i < mdm.getNumberOfDocuments();i++) {
		int j = mdm.getIndexOfDocument(mdm.getDocument(i));
		((DefaultMultipleDocumentModel)mdm).setSelectedIndex(j);
		if(mdm.getDocument(i).isModified()) {
			int result1 = 0;
			String[] options = new String[3];
			options[0] = alp.getString("OK").toString();
			options[1] = alp.getString("save").toString();
			options[2] = alp.getString("cancel").toString();
			result1 = JOptionPane.showOptionDialog((DefaultMultipleDocumentModel)mdm,String.format(alp.getString("porukaClose2").toString(),mdm.getDocument(i).getFilePath()) ,
					alp.getString("warning").toString(),
	                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
			if(result1 == 1) {
				for(ActionListener a: sv.getActionListeners()) {
				    a.actionPerformed(new ActionEvent(mdm.getDocument(i), ActionEvent.ACTION_PERFORMED, null) {
						private static final long serialVersionUID = 1L;
				    });
				}
			}
			if(result1 == 2) {
				return;
			}
		}
	}
	int result = 0;
	String[] options = new String[2];
	options[0] = alp.getString("OK").toString();
	options[1] = alp.getString("cancel").toString();
	result = JOptionPane.showOptionDialog((DefaultMultipleDocumentModel)mdm,alp.getString("porukaClose").toString() ,
			alp.getString("warning").toString(),
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
	if(result == 0) {
		JNotepadPP.this.closing = true;
		alp.disconnect();
		dispose();
	}
	
	}

private void initActions() {
	
    ascendingAction = new LocalizableAction("ascending","opisascending", alp) {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Locale hrLocale = Locale.forLanguageTag(alp.getLanguage());
			Collator hrCollator = Collator.getInstance(hrLocale);
			JTextArea comp = mdm.getCurrentDocument().getTextComponent();
			Document doc = comp.getDocument();
			int len = Math.abs(comp.getCaret().getDot()
					-comp.getCaret().getMark());
			int offset = 0;
			offset = Math.min(comp.getCaret().getDot(),mdm.getCurrentDocument().getTextComponent().getCaret().getMark());
			int line1 = 0,line2 = 0;
			try {
				line1 = comp.getLineOfOffset(offset);
				line2 = comp.getLineOfOffset(len+offset);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			try {
				int startOffset = comp.getLineStartOffset(line1);
				int endOffset = comp.getLineEndOffset(line2);
				String text = doc.getText(startOffset, endOffset-startOffset);
				text = sortT(text,hrCollator);
				doc.remove(startOffset, endOffset-startOffset);
				doc.insertString(startOffset, text, null);
			} catch(BadLocationException ex) {
				ex.printStackTrace();
			}
		}

		private String sortT(String text,Collator c) {
			String[] rijeci = text.split("\n");
			Arrays.sort(rijeci,(a,b)->c.compare(a,b));
			String ret = "";
			for(String s : rijeci) {
				ret = ret + s + "\n";
			}
			return new String(ret);
		}
	};
	
    descendingAction = new LocalizableAction("descending","opisdescending", alp) {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Locale hrLocale = Locale.forLanguageTag(alp.getLanguage());
			Collator hrCollator = Collator.getInstance(hrLocale);
			JTextArea comp = mdm.getCurrentDocument().getTextComponent();
			Document doc = comp.getDocument();
			int len = Math.abs(comp.getCaret().getDot()
					-comp.getCaret().getMark());
			int offset = 0;
			offset = Math.min(comp.getCaret().getDot(),mdm.getCurrentDocument().getTextComponent().getCaret().getMark());
			int line1 = 0,line2 = 0;
			try {
				line1 = comp.getLineOfOffset(offset);
				line2 = comp.getLineOfOffset(len+offset);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			try {
				int startOffset = comp.getLineStartOffset(line1);
				int endOffset = comp.getLineEndOffset(line2);
				String text = doc.getText(startOffset, endOffset-startOffset);
				text = sortT(text,hrCollator);
				doc.remove(startOffset, endOffset-startOffset);
				doc.insertString(startOffset, text, null);
			} catch(BadLocationException ex) {
				ex.printStackTrace();
			}
		}

		private String sortT(String text,Collator c) {
			String[] rijeci = text.split("\n");
			Arrays.sort(rijeci,(a,b)->-c.compare(a,b));
			String ret = "";
			for(String s : rijeci) {
				ret = ret + s + "\n";
			}
			return new String(ret);
		}
	};
	
    uniqueAction = new LocalizableAction("unique","opisunique", alp) {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea comp = mdm.getCurrentDocument().getTextComponent();
			Document doc = comp.getDocument();
			int len = Math.abs(comp.getCaret().getDot()
					-comp.getCaret().getMark());
			int offset = 0;
			offset = Math.min(comp.getCaret().getDot(),mdm.getCurrentDocument().getTextComponent().getCaret().getMark());
			int line1 = 0,line2 = 0;
			try {
				line1 = comp.getLineOfOffset(offset);
				line2 = comp.getLineOfOffset(len+offset);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			try {
				int startOffset = comp.getLineStartOffset(line1);
				int endOffset = comp.getLineEndOffset(line2);
				String text = doc.getText(startOffset, endOffset-startOffset);
				text = uniq(text);
				doc.remove(startOffset, endOffset-startOffset);
				doc.insertString(startOffset, text, null);
			} catch(BadLocationException ex) {
				ex.printStackTrace();
			}
		}

		private String uniq(String text) {
			String[] rijeci = text.split("\n");
			List<Integer> forremoval = new ArrayList<>();
			for(int i = 0,j = rijeci.length; i < j; i++) {
				for(int k = i+1; k < j; k++) {
					if(rijeci[i].equals(rijeci[k])) {
						if(!forremoval.contains(k)) {
							forremoval.add(k);
						}
					}
				}
			}
			String ret = "";
			for(int i = 0,j = rijeci.length; i < j; i++) {
				String s = rijeci[i];
				if(!forremoval.contains(i)) {
					ret = ret + s + "\n";
				}
			}
			return new String(ret);
		}
	};
	
    toggleCaseAction = new LocalizableAction("invertcase","opisinvert", alp) {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = mdm.getCurrentDocument().getTextComponent().getDocument();
			int len = Math.abs(mdm.getCurrentDocument().getTextComponent().getCaret().getDot()
					-mdm.getCurrentDocument().getTextComponent().getCaret().getMark());
			int offset = 0;
				offset = Math.min(mdm.getCurrentDocument().getTextComponent().getCaret().getDot(),mdm.getCurrentDocument().getTextComponent().getCaret().getMark());
			
			try {
				String text = doc.getText(offset, len);
				text = changeCase(text);
				doc.remove(offset, len);
				doc.insertString(offset, text, null);
			} catch(BadLocationException ex) {
				ex.printStackTrace();
			}
		}

		private String changeCase(String text) {
			char[] znakovi = text.toCharArray();
			for(int i = 0; i < znakovi.length; i++) {
				char c = znakovi[i];
				if(Character.isLowerCase(c)) {
					znakovi[i] = Character.toUpperCase(c);
				} else if(Character.isUpperCase(c)) {
					znakovi[i] = Character.toLowerCase(c);
				}
			}
			return new String(znakovi);
		}
	};
	
    upperCaseAction = new LocalizableAction("uppercase","opisupper", alp) {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = mdm.getCurrentDocument().getTextComponent().getDocument();
			int len = Math.abs(mdm.getCurrentDocument().getTextComponent().getCaret().getDot()
					-mdm.getCurrentDocument().getTextComponent().getCaret().getMark());
			int offset = 0;
				offset = Math.min(mdm.getCurrentDocument().getTextComponent().getCaret().getDot(),mdm.getCurrentDocument().getTextComponent().getCaret().getMark());
			try {
				String text = doc.getText(offset, len);
				text = changeCase(text);
				doc.remove(offset, len);
				doc.insertString(offset, text, null);
			} catch(BadLocationException ex) {
				ex.printStackTrace();
			}
		}

		private String changeCase(String text) {
			char[] znakovi = text.toCharArray();
			for(int i = 0; i < znakovi.length; i++) {
				char c = znakovi[i];
				znakovi[i] = Character.toUpperCase(c);
			}
			return new String(znakovi);
		}
	};
	
    lowerCaseAction = new LocalizableAction("lowercase","opislower", alp) {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = mdm.getCurrentDocument().getTextComponent().getDocument();
			int len = Math.abs(mdm.getCurrentDocument().getTextComponent().getCaret().getDot()
					-mdm.getCurrentDocument().getTextComponent().getCaret().getMark());
			int offset = 0;
				offset = Math.min(mdm.getCurrentDocument().getTextComponent().getCaret().getDot(),mdm.getCurrentDocument().getTextComponent().getCaret().getMark());
			try {
				String text = doc.getText(offset, len);
				text = changeCase(text);
				doc.remove(offset, len);
				doc.insertString(offset, text, null);
			} catch(BadLocationException ex) {
				ex.printStackTrace();
			}
		}

		private String changeCase(String text) {
			char[] znakovi = text.toCharArray();
			for(int i = 0; i < znakovi.length; i++) {
				char c = znakovi[i];
				znakovi[i] = Character.toLowerCase(c);
			}
			return new String(znakovi);
		}
	};
	 exitAction = new LocalizableAction("exit","opisExit", alp) {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JNotepadPP.this.close();
		}
	};
	
    copyAction = new LocalizableAction("copy","opisCopy", alp) {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea editor = mdm.getCurrentDocument().getTextComponent();
			editor.copy();
		}
	};
	
	 cutAction = new LocalizableAction("cut","opisCut", alp) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextArea editor = mdm.getCurrentDocument().getTextComponent();
				editor.cut();
			}
		};
		
    pasteAction = new LocalizableAction("paste","opisPaste", alp) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextArea editor = mdm.getCurrentDocument().getTextComponent();
				editor.paste();
			}
		};
	
    statisticalAction = new LocalizableAction("statistical","opisStatistical", alp) {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] polje = JNotepadPP.this.statistical();
			
			String[] options = new String[1];
			options[0] = this.getValue("OK").toString();
			JOptionPane.showOptionDialog((DefaultMultipleDocumentModel)mdm,String.format( this.getValue("statsMessage").toString(),
					polje[0],polje[1],polje[2]),
					this.getValue("stats").toString(),
	                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		}
	};
	statisticalAction.putValue("OK", alp.getString("OK"));
	statisticalAction.putValue("stats",alp.getString("stats"));
	statisticalAction.putValue("statsMessage",alp.getString("statsMessage"));
	alp.addLocalizationListener(new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			statisticalAction.putValue("OK", alp.getString("OK"));
			statisticalAction.putValue("stats",alp.getString("stats"));
			statisticalAction.putValue("statsMessage",alp.getString("statsMessage"));
		};
	});

    loadDocumentAction = new LocalizableAction("load","opisLoad", alp) {
	
	    private static final long serialVersionUID = 1L;
	
	    @Override
    	public void actionPerformed(ActionEvent e) {
	    	JFileChooser fc = new JFileChooser();
	    	fc.setDialogTitle("Open file");
	    	if(fc.showOpenDialog((Component) mdm)!=JFileChooser.APPROVE_OPTION) {
	    		return;
	    	}
	    	File fileName = fc.getSelectedFile();
	    	Path filePath = fileName.toPath();
	    	if(!Files.isReadable(filePath)) {
	    		String[] options = new String[1];
				options[0] = this.getValue("OK").toString();
				JOptionPane.showOptionDialog((DefaultMultipleDocumentModel)mdm,String.format( this.getValue("porukaNepostojeca").toString(),filePath.toAbsolutePath()),
						this.getValue("warning").toString(),
		                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
				return;
	    	}
	    	mdm.loadDocument(filePath);
    	}
    };
    
    loadDocumentAction.putValue("OK", alp.getString("OK"));
    loadDocumentAction.putValue("warning",alp.getString("warning"));
    loadDocumentAction.putValue("porukaNepostojeca",alp.getString("porukaNepostojeca"));
	alp.addLocalizationListener(new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			loadDocumentAction.putValue("OK", alp.getString("OK"));
			loadDocumentAction.putValue("warning",alp.getString("warning"));
			loadDocumentAction.putValue("porukaNepostojeca",alp.getString("porukaNepostojeca"));
		};
	});
 

    openDocumentAction = new LocalizableAction("open","opisOpen", alp) {
    	
    	//flp.addLocalizationListener(()->gumbOpen.setText(flp.getString("open")));
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
		    mdm.createNewDocument();
		    }
	};
	
    closeDocumentAction = new LocalizableAction("close","opisClose", alp) {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int result = 0;
			if(mdm.getCurrentDocument().isModified()) {
				
				String[] options = new String[2];
				options[0] = this.getValue("OK").toString();
				options[1] = this.getValue("cancel").toString();
				result = JOptionPane.showOptionDialog((DefaultMultipleDocumentModel)mdm,this.getValue("porukaClose").toString(),
						this.getValue("warning").toString(),
		                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
			}
			if(result == 0) {
		        mdm.closeDocument(mdm.getCurrentDocument());
			}
		}
	};
	closeDocumentAction.putValue("OK", alp.getString("OK"));
	closeDocumentAction.putValue("cancel", alp.getString("cancel"));
	closeDocumentAction.putValue("warning",alp.getString("warning"));
	closeDocumentAction.putValue("porukaClose",alp.getString("porukaClose"));
	alp.addLocalizationListener(new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			closeDocumentAction.putValue("OK", alp.getString("OK"));
			closeDocumentAction.putValue("warning",alp.getString("warning"));
			closeDocumentAction.putValue("cancel", alp.getString("cancel"));
			closeDocumentAction.putValue("porukaClose",alp.getString("porukaClose"));
		};
	});
	
    saveDocumentAction = new LocalizableAction("save","opisSave", alp) {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Path openedFilePath = mdm.getCurrentDocument().getFilePath();
			if(openedFilePath==null) {
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogTitle("Save document");
				if(jfc.showSaveDialog((Component) mdm)!=JFileChooser.APPROVE_OPTION) {
					String[] options = new String[1];
					options[0] = this.getValue("OK").toString();
					JOptionPane.showOptionDialog((DefaultMultipleDocumentModel)mdm, this.getValue("porukaNotSaved"),
							this.getValue("warning").toString(),
			                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
					return;
				}
				openedFilePath = jfc.getSelectedFile().toPath();
			}
			try {
		    mdm.saveDocument(mdm.getCurrentDocument(), openedFilePath);
			}
			catch(IllegalArgumentException f) {
				String[] options = new String[1];
				options[0] = this.getValue("OK").toString();
				JOptionPane.showOptionDialog((DefaultMultipleDocumentModel)mdm, this.getValue("pathOpened"),
						this.getValue("warning").toString(),
		                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
				return;
			}
		}
	};
	saveDocumentAction.putValue("OK", alp.getString("OK"));
	saveDocumentAction.putValue("pathOpened", alp.getString("pathOpened"));
	saveDocumentAction.putValue("porukaNotSaved",alp.getString("porukaNotSaved"));
	saveDocumentAction.putValue("warning",alp.getString("warning"));
	alp.addLocalizationListener(new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			saveDocumentAction.putValue("pathOpened", alp.getString("pathOpened"));
			saveDocumentAction.putValue("OK", alp.getString("OK"));
			saveDocumentAction.putValue("porukaNotSaved",alp.getString("porukaNotSaved"));
			saveDocumentAction.putValue("warning",alp.getString("warning"));
		};
	});
	
	saveAsDocumentAction = new LocalizableAction("saveAs","opisSaveAs", alp) {
			
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				Path openedFilePath = mdm.getCurrentDocument().getFilePath();
				if(openedFilePath!=null) {
					String[] options = new String[1];
					options[0] = this.getValue("OK").toString();
					JOptionPane.showOptionDialog((DefaultMultipleDocumentModel)mdm, this.getValue("porukaSaveAs"),
							this.getValue("title").toString(),
			                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
					}
					JFileChooser jfc = new JFileChooser();
					jfc.setDialogTitle("Save document as");
					if(jfc.showSaveDialog((Component) mdm)!=JFileChooser.APPROVE_OPTION) {
						String[] options = new String[1];
						options[0] = this.getValue("OK").toString();
						JOptionPane.showOptionDialog((DefaultMultipleDocumentModel)mdm, this.getValue("porukaNotSaved"),
								this.getValue("warning").toString(),
				                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
						
						return;
					}
					openedFilePath = jfc.getSelectedFile().toPath();
			    
			    try {
			    	mdm.saveDocument(mdm.getCurrentDocument(), openedFilePath);
					}
					catch(IllegalArgumentException f) {
						String[] options = new String[1];
						options[0] = this.getValue("OK").toString();
						JOptionPane.showOptionDialog((DefaultMultipleDocumentModel)mdm, this.getValue("pathOpened"),
								this.getValue("warning").toString(),
				                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
						return;
					}
			}
		};
		saveAsDocumentAction.putValue("OK", alp.getString("OK"));
		saveAsDocumentAction.putValue("pathOpened", alp.getString("pathOpened"));
		saveAsDocumentAction.putValue("title", alp.getString("title"));
		saveAsDocumentAction.putValue("porukaSaveAs",alp.getString("porukaSaveAs"));
		saveAsDocumentAction.putValue("porukaNotSaved",alp.getString("porukaNotSaved"));
		saveAsDocumentAction.putValue("warning",alp.getString("warning"));
		alp.addLocalizationListener(new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				saveAsDocumentAction.putValue("OK", alp.getString("OK"));
				saveAsDocumentAction.putValue("pathOpened", alp.getString("pathOpened"));
				saveAsDocumentAction.putValue("title", alp.getString("title"));
				saveAsDocumentAction.putValue("porukaSaveAs",alp.getString("porukaSaveAs"));
				saveAsDocumentAction.putValue("porukaNotSaved",alp.getString("porukaNotSaved"));
				saveAsDocumentAction.putValue("warning",alp.getString("warning"));
			};
		});
}	

	private void createActions() {
		openDocumentAction.putValue(
				Action.NAME, 
				openDocumentAction.getValue("open"));
		openDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control O")); 
		openDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_O); 
		openDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to open new file."); 
		
		copyAction.putValue(
				Action.NAME, 
				"Copy");
		copyAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control C")); 
		copyAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_C); 
		copyAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Copy selected text."); 
		
		cutAction.putValue(
				Action.NAME, 
				"Cut");
		cutAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control X")); 
		cutAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_X); 
		cutAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Cut selected text."); 
		
		pasteAction.putValue(
				Action.NAME, 
				"Paste");
		pasteAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control V")); 
		pasteAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_V); 
		pasteAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Paste text."); 
		
		statisticalAction.putValue(
				Action.NAME, 
				"Stats");
		statisticalAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control I")); 
		statisticalAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_I); 
		statisticalAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to open get statisticalinfo about file."); 
		
		saveDocumentAction.putValue(
				Action.NAME, 
				"Save");
		saveDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control S")); 
		saveDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_S); 
		saveDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to save current file to disk."); 
		
		closeDocumentAction.putValue(
				Action.NAME, 
				"Close");
		closeDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control W")); 
		closeDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_W); 
		closeDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to close current file."); 
		
		saveAsDocumentAction.putValue(
				Action.NAME, 
				"SaveAs");
		saveAsDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control A")); 
		saveAsDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_A); 
		saveAsDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to save current file to disk."); 
		
		loadDocumentAction.putValue(
				Action.NAME, 
				"Load");
		loadDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_L); 
		loadDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to load file."); 
		loadDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control L")); 
		
		/*deleteSelectedPartAction.putValue(
				Action.NAME, 
				"Delete selected text");
		deleteSelectedPartAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("F2")); 
		deleteSelectedPartAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_D); 
		deleteSelectedPartAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to delete the selected part of text."); 
		*/
		ascendingAction.putValue(
				Action.NAME, 
				"Ascending");
		ascendingAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control F2")); 
		ascendingAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_A); 
		ascendingAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to sort lines in ascending in selected part of text."); 
		
		descendingAction.putValue(
				Action.NAME, 
				"Descending");
		descendingAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control F1")); 
		descendingAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_D); 
		descendingAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to sort lines in descending in selected part of text.");
		
		toggleCaseAction.putValue(
				Action.NAME, 
				"Invert case");
		toggleCaseAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control F3")); 
		toggleCaseAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_T); 
		toggleCaseAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to toggle character case in selected part of text or in entire document."); 
		
		upperCaseAction.putValue(
				Action.NAME, 
				"To UpperCase");
		upperCaseAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control F4")); 
		upperCaseAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_E); 
		upperCaseAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to turn character case to UpperCase in selected part of text."); 
		
		lowerCaseAction.putValue(
				Action.NAME, 
				"To LowerCase");
		lowerCaseAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control F5")); 
		lowerCaseAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_R); 
		lowerCaseAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to turn character case to LowerCase in selected part of text."); 
		
		
		exitAction.putValue(
				Action.NAME, 
				"Exit");
		exitAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control X"));
		exitAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_X); 
		exitAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Exit application."); 
		
		uniqueAction.putValue(
				Action.NAME, 
				"Unique");
		uniqueAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control U"));
		uniqueAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_U); 
		uniqueAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to delete duplicate lines in selected text."); 
	}
	

	public int[] statistical() {
		JTextArea text = mdm.getCurrentDocument().getTextComponent();
		String str = text.getText();
		int[] ljepo = new int[3];
		ljepo[2] = (int) str.lines().count();
		ljepo[1] = (int) str.chars().filter((a)-> a != '\n' && a != '\t' && a != ' ' && a != '\r').count();
		ljepo[0] = (int) str.chars().count();
		return ljepo;
	}

	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		alp.addLocalizationListener(()->
		fileMenu.setText(alp.getString("file")));
		menuBar.add(fileMenu);
		
		sv = new JMenuItem(saveDocumentAction);
		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(closeDocumentAction));
		fileMenu.add(sv);
		fileMenu.add(new JMenuItem(saveAsDocumentAction));
		fileMenu.add(new JMenuItem(loadDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(copyAction));
		fileMenu.add(new JMenuItem(pasteAction));
		fileMenu.add(new JMenuItem(cutAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(statisticalAction));
		fileMenu.add(new JMenuItem(exitAction));
		
		JMenu fileMenu2 = new JMenu("Languages");
		alp.addLocalizationListener(()->
		fileMenu2.setText(alp.getString("languages")));
		menuBar.add(fileMenu2);
		
		JMenu tools = new JMenu("Tools");
		alp.addLocalizationListener(()->
		tools.setText(alp.getString("tools")));
		
		JMenu changeCase = new JMenu("Change case");
		alp.addLocalizationListener(()->
		changeCase.setText(alp.getString("changeCase")));
		
		uppercase = new JMenuItem(upperCaseAction);
		uppercase.setEnabled(false);
		alp.addLocalizationListener(()->
		uppercase.setText(alp.getString("uppercase")));
		changeCase.add(uppercase);
		lowercase = new JMenuItem(lowerCaseAction);
		lowercase.setEnabled(false);
		alp.addLocalizationListener(()->
		lowercase.setText(alp.getString("lowercase")));
		changeCase.add(lowercase);
		invertcase = new JMenuItem(toggleCaseAction);
		invertcase.setEnabled(false);
		alp.addLocalizationListener(()->
		invertcase.setText(alp.getString("invertcase")));
		changeCase.add(invertcase);
		
		JMenu sort = new JMenu(alp.getString("sort"));
		alp.addLocalizationListener(()->
		changeCase.setText(alp.getString("sort")));
		
		ascending = new JMenuItem(ascendingAction);
		ascending.setEnabled(false);
		alp.addLocalizationListener(()->
		ascending.setText(alp.getString("ascending")));
		sort.add(ascending);
		
		descending = new JMenuItem(descendingAction);
		descending.setEnabled(false);
		alp.addLocalizationListener(()->
		descending.setText(alp.getString("descending")));
		sort.add(descending);
		
		unique = new JMenuItem(uniqueAction);
		alp.addLocalizationListener(()->
		unique.setText(alp.getString("unique")));
		unique.setEnabled(false);
		
		tools.add(changeCase);
		tools.add(sort);
		tools.add(unique);
		menuBar.add(tools);
		
		JMenuItem en = new JMenuItem("en");
		en.addActionListener((t)->
		 LocalizationProvider.getInstance().setLanguage("en")
				);
		JMenuItem de = new JMenuItem("de");
		de.addActionListener((t)->
		 LocalizationProvider.getInstance().setLanguage("de")
				);
		JMenuItem hr = new JMenuItem("hr");
		hr.addActionListener((t)->
		 LocalizationProvider.getInstance().setLanguage("hr")
				);

		fileMenu2.add(en);
		fileMenu2.add(de);
		fileMenu2.add(hr);
		
		this.setJMenuBar(menuBar);
	}

	private void createToolbars() {
		JToolBar toolBar = new JToolBar("Alati");
		alp.addLocalizationListener(()->
		toolBar.setToolTipText(alp.getString("tools")));
		toolBar.setFloatable(true);
		
		toolBar.add(new JButton(openDocumentAction));
		toolBar.add(new JButton(saveDocumentAction));
		toolBar.add(new JButton(closeDocumentAction));
		toolBar.add(new JButton(saveAsDocumentAction));
		toolBar.add(new JButton(loadDocumentAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(copyAction));
		toolBar.add(new JButton(pasteAction));
		toolBar.add(new JButton(cutAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(statisticalAction));
		toolBar.add(new JButton(exitAction));
		
		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				LocalizationProvider.getInstance().setLanguage("en");
				new JNotepadPP().setVisible(true);
			}
		});
	}

}