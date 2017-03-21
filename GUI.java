package games.conways;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class GUI {

	private static int X = 150;
	private static int Y = 150;
	private int counter = 0;
	
	private boolean[][] b_tiles = new boolean[X][Y];
	private int[] times = {10000, 5000, 1000, 500, 250, 100, 50, 25, 10, 5, 1};
	
	private JFrame f = new JFrame();
	private JPanel main = new JPanel(new BorderLayout());
	private JPanel world = new JPanel();
	private JScrollPane world_pane = new JScrollPane(world);
	private JPanel settings = new JPanel();
	private JLabel counter_label = new JLabel(Integer.toString(counter));
	private JButton start = new JButton("Start");
	private JButton reset = new JButton("Reset");
	private JButton next = new JButton("Next");
	private JButton b_cl_back = new JButton("Background");
	private JButton b_cl_lines = new JButton("Lines");
	private JButton b_cl_cells = new JButton("Living Cells");
	private JSlider time_sl = new JSlider(0, 10, 3);
	private JLabel time_l = new JLabel("Speed");
	
	private Object[][] table_objects = new Object[X][Y];
	private String[] columnNames = new String[X];
	
	private Color cl_back = Color.GRAY;
	private Color cl_lines = Color.WHITE;
	private Color cl_cells = Color.YELLOW;
	
	private JTable table = new JTable();
		
	private Logic logic = new Logic();
	
	private ActionListener al = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(reset)){
				for(int x = 0; x < X; x++){
					for(int y = 0; y < Y; y++){
						b_tiles[y][x] = false;
					}
				}
				if(t.isRunning()){
					t.stop();
					start.setText("Start");
				}
				counter = 0;
				counter_label.setText(Integer.toString(counter));
				world.repaint();
			}else if(e.getSource().equals(start)){
				if(t.isRunning()){
					t.stop();
					start.setText("Start");
				}else{
					t.start();
					start.setText("Stop");
				}
			}else if(e.getSource().equals(t)){
				b_tiles = logic.getNextResult(b_tiles);
				world.repaint();
				counter++;
				counter_label.setText(Integer.toString(counter));
			}else if(e.getSource().equals(next)){
				if(t.isRunning()){
					t.stop();
					start.setText("Start");
				}
				b_tiles = logic.getNextResult(b_tiles);
				world.repaint();
				
				counter++;
				counter_label.setText(Integer.toString(counter));
			}else if(e.getSource().equals(b_cl_back)){
				cl_back = JColorChooser.showDialog(null, "Choose Background Color", null);
				world.setBackground(cl_back);
				settings.setBackground(cl_back);
				world.repaint();
			}else if(e.getSource().equals(b_cl_lines)){
				cl_lines = JColorChooser.showDialog(null, "Choose Lines Color", null);
				table.setBorder(BorderFactory.createLineBorder(cl_lines));
		        table.setGridColor(cl_lines);
				world.repaint();
			}else if(e.getSource().equals(b_cl_cells)){
				cl_cells = JColorChooser.showDialog(null, "Choose Cell Color", null);
				world.repaint();
			}
		}
	};
	
	private ChangeListener cl = new ChangeListener() {
		
		@Override
		public void stateChanged(ChangeEvent e) {
			if(e.getSource().equals(time_sl)){
				t.setDelay(times[time_sl.getValue()]);
			}
		}
	};
	
	private MouseListener ml = new MouseAdapter() {
		@Override
    	public void mouseClicked(MouseEvent e) {
    		int row = table.rowAtPoint(e.getPoint());
    		int col = table.columnAtPoint(e.getPoint());
    		b_tiles[row][col] = b_tiles[row][col] ? false : true;
    		world.repaint();
    	}
	};
	
	private Timer t = new Timer(times[3], al);
	
	public GUI() {
		loadGUI();
		f.setSize(1000, 1000);
		f.setTitle("Conway's Game Of Life");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		world.setBackground(cl_back);
		
		time_sl.addChangeListener(cl);
		
		settings.setLayout(new FlowLayout());
		settings.setBackground(cl_back);
		settings.add(b_cl_back);
		settings.add(b_cl_lines);
		settings.add(b_cl_cells);
		settings.add(next);
		settings.add(start);
		settings.add(reset);
		settings.add(counter_label);
		settings.add(time_l);
		settings.add(time_sl);
		
		b_cl_back.addActionListener(al);
		b_cl_lines.addActionListener(al);
		b_cl_cells.addActionListener(al);
		next.addActionListener(al);
		start.addActionListener(al);
		reset.addActionListener(al);
		
		f.add(main);
		main.add(world_pane, BorderLayout.CENTER);
		main.add(settings, BorderLayout.SOUTH);
		System.out.println("Finished1");
		f.setVisible(true);
		System.out.println("Finished2");
	}

	private void loadGUI() {
		world.removeAll();
		world.setLayout(new GridLayout(1, 0));
		
		table.setModel(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(13);
        table.setTableHeader(null);
        table.addMouseListener(ml);
        table.setBorder(BorderFactory.createLineBorder(cl_lines));
        table.setGridColor(cl_lines);
        
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < X; i++) {
            if (i < columnModel.getColumnCount()) {
                columnModel.getColumn(i).setMaxWidth(9);
            }
            else break;
        }
		
        MyCellRenderer mcr = new MyCellRenderer();
        for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex ++) {
                    table.getColumnModel().getColumn(columnIndex).setCellRenderer(mcr);
        }
        
        world.add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		System.out.println("Loaded GUI");
	}
	
	DefaultTableModel tableModel = new DefaultTableModel(table_objects, columnNames){
		private static final long serialVersionUID = 8699653150398411715L;
		
		@Override
		public boolean isCellEditable(int row, int column){
			return false;
		}		
	};
	
	public class MyCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 3791458192959541675L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(!b_tiles[row][column]){
            	cellComponent.setBackground(cl_back);
            }else{
            	cellComponent.setBackground(cl_cells);
            }
            ((JComponent) cellComponent).setBorder(BorderFactory.createEmptyBorder());
            return cellComponent;
        }
    }
}
