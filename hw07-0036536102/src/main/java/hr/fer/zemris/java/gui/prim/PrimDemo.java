package hr.fer.zemris.java.gui.prim;
/**
 * Razred koji predstavlja prikaz dvije liste u koje se dodaju prim brojevi.
 */
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class PrimDemo extends JFrame {

	private static final long serialVersionUID = 1L;

	public PrimDemo() {
		setLocation(20, 50);
		setSize(300, 200);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		initGUI();
	}

	/**
	 * Model koji u listu dodaje prim brojeve.
	 * @author Petra
	 *
	 * @param <T>
	 */
	static class PrimListModel<T> implements ListModel<Integer> {
		private int prim = 1;
		private List<Integer> elementi = new ArrayList<>();
		private List<ListDataListener> promatraci = new ArrayList<>();
		
		/**
		 * Dodavanje data listenera.
		 */
		@Override
		public void addListDataListener(ListDataListener l) {
			promatraci.add(l);
		}
		/**
		 * Uklanjanje data listenera.
		 */
		@Override
		public void removeListDataListener(ListDataListener l) {
			promatraci.remove(l);
		}
		/**
		 * Vraca velicinu liste.
		 */
		@Override
		public int getSize() {
			return elementi.size();
		}
		/**
		 * Dohvaca element na predanoj poziciji.
		 */
		@Override
		public Integer getElementAt(int index) {
			return elementi.get(index);
		}
		/**
		 * Dodaje element u listu.
		 * @param element
		 */
		public void add(Integer element) {
			int pos = elementi.size();
			elementi.add(element);
			
			ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, pos, pos);
			for(ListDataListener l : promatraci) {
				l.intervalAdded(event);
			}
		}
		/**
		 * racuna iduci prim broj.
		 */
		public void next() {
			if(prim == 1) {
				this.add(Integer.valueOf(prim));
				prim = 2;
				return;
			}
			if(prim == 2) {
				this.add(Integer.valueOf(prim));
				prim = 0;
				return;
			}
			if(prim == 0) {
				prim = 2;
				}
			int djelitelji = 0;
			for(int i = this.prim+1;true;i++) {
				for(int j = 2;j < i;j++) {
					if(i%j==0) {
						djelitelji++;
					}
				}
				if(djelitelji == 0) {
					this.prim = i;
					break;
				}
				djelitelji = 0;
			}
			this.add(Integer.valueOf(prim));
			
		}
	}
	
	/**
	 * Inicijalizacija GUI-ja.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		PrimListModel<Integer> model = new PrimListModel<>();
		
		JList<Integer> list1 = new JList<>(model);
		JList<Integer> list2 = new JList<>(model);
		
		JPanel bottomPanel = new JPanel(new GridLayout(1, 0));

		JButton dodaj = new JButton("Sljedeći");
		bottomPanel.add(dodaj);
		
		Random rand = new Random();
		dodaj.addActionListener(e -> {
			model.next();
		});

		JPanel central = new JPanel(new GridLayout(1, 0));
		central.add(new JScrollPane(list1));
		central.add(new JScrollPane(list2));
		
		cp.add(central, BorderLayout.CENTER);
		cp.add(bottomPanel, BorderLayout.PAGE_END);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new PrimDemo();
			frame.pack();
			frame.setVisible(true);
		});
	}
}
