package games.conways;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	private JLabel[][] jl_tiles = new JLabel[X][Y];
	private JButton start = new JButton("Start");
	private JButton reset = new JButton("Reset");
	private JButton next = new JButton("Next");
	private JButton b_cl_back = new JButton("Background");
	private JButton b_cl_lines = new JButton("Lines");
	private JButton b_cl_cells = new JButton("Living Cells");
	private JSlider time_sl = new JSlider(0, 10, 3);
	private JLabel time_l = new JLabel("Speed");
	
	private Color cl_back = Color.GRAY;
	private Color cl_lines = Color.WHITE;
	private Color cl_cells = Color.YELLOW;
		
	private Logic logic = new Logic();
	
	private MouseAdapter ma = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(!t.isRunning()){
				Point loc = new Point(e.getPoint().x - jl_tiles[0][0].getLocation().x, e.getPoint().y - jl_tiles[0][0].getLocation().y);
				JLabel label;
				if((label = (JLabel)world.getComponentAt(loc)) != null){
					for(int x = 1; x < X-1; x++){
						for(int y = 1; y < Y-1; y++){
							if(jl_tiles[y][x].equals(label)){
								if(!b_tiles[y][x]){
									b_tiles[y][x] = true;
									jl_tiles[y][x].setBackground(cl_cells);
								}else{
									b_tiles[y][x] = false;
									jl_tiles[y][x].setBackground(cl_back);
								}
								return;
							}
						}
					}
				}
			}
		}
	};
	
	private ActionListener al = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(reset)){
				for(int x = 0; x < X; x++){
					for(int y = 0; y < Y; y++){
						jl_tiles[y][x].setBackground(cl_back);
						b_tiles[y][x] = false;
					}
				}
				if(t.isRunning()){
					t.stop();
					start.setText("Start");
				}
				counter = 0;
				counter_label.setText(Integer.toString(counter));
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
				for(int x = 0; x < X; x++){
					for(int y = 0; y < Y; y++){
						if(b_tiles[y][x]){
							jl_tiles[y][x].setBackground(cl_cells);
						}else{
							jl_tiles[y][x].setBackground(cl_back);
						}
					}
				}
				counter++;
				counter_label.setText(Integer.toString(counter));
			}else if(e.getSource().equals(next)){
				if(t.isRunning()){
					t.stop();
					start.setText("Start");
				}
				b_tiles = logic.getNextResult(b_tiles);
				for(int x = 0; x < X; x++){
					for(int y = 0; y < Y; y++){
						if(b_tiles[y][x]){
							jl_tiles[y][x].setBackground(cl_cells);
						}else{
							jl_tiles[y][x].setBackground(cl_back);
						}
					}
				}
				counter++;
				counter_label.setText(Integer.toString(counter));
			}else if(e.getSource().equals(b_cl_back)){
				cl_back = JColorChooser.showDialog(null, "Choose Background Color", null);
				world.setBackground(cl_back);
				settings.setBackground(cl_back);
				for(int y = 0; y < Y; y++){
					for(int x = 0; x < X; x++){
						if(!b_tiles[y][x]){
							jl_tiles[y][x].setBackground(cl_back);
						}
					}
				}
			}else if(e.getSource().equals(b_cl_lines)){
				cl_lines = JColorChooser.showDialog(null, "Choose Lines Color", null);
				for(int y = 0; y < Y; y++){
					for(int x = 0; x < X; x++){
						jl_tiles[y][x].setBorder(border);
					}
				}
				world.repaint();
			}else if(e.getSource().equals(b_cl_cells)){
				cl_cells = JColorChooser.showDialog(null, "Choose Cell Color", null);
				for(int y = 0; y < Y; y++){
					for(int x = 0; x < X; x++){
						if(b_tiles[y][x]){
							jl_tiles[y][x].setBackground(cl_cells);
						}
					}
				}
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
	
	private LineBorder border = new LineBorder(cl_lines, 1){
		
		@Override
		public void paintBorder(final Component c, final Graphics g, final int x, final int y, final int width, final int height) {
		    super.lineColor = cl_lines;
		    super.paintBorder(c, g, x, y, width, height);
		}
	};
	
	private Timer t = new Timer(times[3], al);
	
	private JFrame l = new JFrame("Conway's Game Of Life");
	private JProgressBar progressbar = new JProgressBar();
	
	
	public GUI() {
		l.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		progressbar.setValue(0);
		progressbar.setBorder(BorderFactory.createTitledBorder("Loading tiles..."));
		progressbar.setStringPainted(true);
		l.add(progressbar);
		l.setSize(300, 100);
		l.setVisible(true);
		
		
		loadGUI();
		f.setSize(1000, 1000);
		f.setTitle("Conway's Game Of Life");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		world.setBackground(cl_back);
		world.addMouseListener(ma);
		
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
		l.dispose();
	}

	private void loadGUI() {
		world.removeAll();
		world.setLayout(new GridLayout(Y, X));	
		int index = 0;
		for(int y = 0; y < Y; y++){
			for(int x = 0; x < X; x++){
				jl_tiles[y][x] = new JLabel();
				jl_tiles[y][x].setMinimumSize(new Dimension(5, 5));
				jl_tiles[y][x].setMaximumSize(new Dimension(20, 20));
				jl_tiles[y][x].setPreferredSize(new Dimension(10, 10));
				jl_tiles[y][x].setSize(10, 10);
				jl_tiles[y][x].setOpaque(true);
				jl_tiles[y][x].setBackground(cl_back);
				jl_tiles[y][x].setBorder(border);
				world.add(jl_tiles[y][x]);
				index++;
				progressbar.setValue(index/X*Y*100);
			}
		}
		System.out.println("Loaded GUI");
	}
}
