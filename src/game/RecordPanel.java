package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class RecordPanel extends JPanel implements ActionListener, KeyListener, MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = MenuPanel.WIDTH;
	public static final int HEIGHT = MenuPanel.HEIGHT;
	
	private Timer timer;
	
	private BufferedImage background;
	private BufferedImage voltar;
	private BufferedImage deletar;
	private BufferedImage selector;
	
	private int currRecord = 0;
	
	private int choice = 0;
	
	private File recordFile;
	
	private Scanner s;
	
	private GameWindow window;
	
	public RecordPanel(GameWindow window) {

		this.window = window;
		
		setFocusable(true);
		addKeyListener(this);
		
		try {
			background = ImageIO.read(new File("res/background.png"));
			recordFile = new File("res/records.dat");
			voltar = ImageIO.read(new File("res/voltar.png"));
			deletar = ImageIO.read(new File("res/deletar.png"));
			selector = ImageIO.read(new File("res/selector.png"));
			s = new Scanner(recordFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		if(s.hasNext()) {
			currRecord = s.nextInt();
		}
		
		timer = new Timer(20, this);
		timer.start();
	}

	
	public void render(Graphics2D g) {
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.drawImage(background, 0, 0, null);
		g.drawImage(deletar, 400, 380, null);
		g.drawImage(voltar, 412, 420, null);
		
		if(choice == 0) {
			g.drawImage(selector, 370, 380, null);
		} else if(choice == 1) {
			g.drawImage(selector, 382, 421, null);
		}
		
		
		g.setFont(new Font("Trebuchet MS", Font.PLAIN, 40));
		g.setColor(new Color(31, 88, 14));
		if(currRecord == 0) {
			g.drawString("(EMPTY)", WIDTH / 2 - 80, HEIGHT / 2);
		} else {
			if(currRecord > 60) {
				int minutes = currRecord / 60;
				int seconds = currRecord - minutes*60;
				g.drawString("Melhor tempo: " + minutes+" min e "+seconds+" seg", 80, HEIGHT / 2);
			} else {
				g.drawString("Melhor tempo: " + currRecord+"s", 180, HEIGHT / 2);				
			}
		}
		
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		render((Graphics2D) g);
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			choice--;
			if(choice < 0) {
				choice = 1;
			}
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			choice++;
			if(choice > 1) choice = 0;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(choice == 0) {
				Object[] options = { "Não", "Sim" };
				this.repaint();
				int dialog = JOptionPane.showOptionDialog(null, "Deseja realmente apagar os recordes?", "Atenção!",
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (dialog == 1) {
					currRecord = 0;
					try {
						PrintWriter pw  = new PrintWriter(recordFile);
						pw.close();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			} else if(choice == 1) {
				toMenu();
			}
		}
	}

	private void toMenu() {
		window.removeKeyListener(window.record);
		window.remove(window.record);
		
		MenuPanel menu = new MenuPanel(window);
		window.menu = menu;
		
		window.pack();
		window.add(menu);
		window.addKeyListener(menu);
		window.setSize(WIDTH, HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.requestFocus();
		window.setVisible(true);
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}
	
	
}
