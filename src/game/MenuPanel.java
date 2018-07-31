package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MenuPanel extends JPanel implements ActionListener, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 700;
	public static final int HEIGHT = 520;
	
	private BufferedImage background;
	private BufferedImage title;
	private BufferedImage start;
	private BufferedImage records;
	private BufferedImage exit;
	private BufferedImage selector;
	
	private int menuChoice;
	
	private Timer timer;
	
	private GameWindow window;
	
	public MenuPanel(GameWindow window) {
		
		setFocusable(true);
		addKeyListener(this);
		
		this.window = window;
		
		menuChoice = 0;
		
		try {
			background = ImageIO.read(new File("res/background.png"));
			title = ImageIO.read(new File("res/title.png"));
			start = ImageIO.read(new File("res/start.png"));
			records = ImageIO.read(new File("res/records.png"));
			exit = ImageIO.read(new File("res/exit.png"));
			selector = ImageIO.read(new File("res/selector.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		timer = new Timer(20, this);
		timer.start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		render((Graphics2D) g);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		update();
		repaint();
	}
	
	private void render(Graphics2D g) {
		g.drawImage(background, 0, 0, null);
		g.drawImage(title, 113, 54, null);
		g.drawImage(start, 291, 192, null);
		g.drawImage(records, 266, 233, null);
		g.drawImage(exit, 310, 274, null);
		
		if(menuChoice == 0) {
			g.drawImage(selector, 260, 188, null);
		} else if(menuChoice == 1) {
			g.drawImage(selector, 235, 230, null);
		} else if(menuChoice == 2) {
			g.drawImage(selector, 279, 271, null);
		}
		
	}
	
	private void update() {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			menuChoice++;
			if(menuChoice > 2) {
				menuChoice = 0;
			}
		} else if(e.getKeyCode() == KeyEvent.VK_UP) {
			menuChoice--;
			if(menuChoice < 0) {
				menuChoice = 2;
			}
		} else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(menuChoice == 0) {
				startGame();
			} else if(menuChoice == 1) {
				toRecords();
			} else if(menuChoice == 2) {
				System.exit(JFrame.EXIT_ON_CLOSE);
			}
		}
	}

	private void startGame() {
		window.remove(window.menu);
		
		GamePanel game = null;
		try {
			game = new GamePanel();
		} catch (IOException e) {
			window.game = game;
			e.printStackTrace();
		}
		
		window.pack();
		window.setSize(WIDTH, HEIGHT);
		window.addMouseListener(game);
		window.addMouseMotionListener(game);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.requestFocus();
		window.setContentPane(game);
		window.setVisible(true);
		
		
		
		this.repaint();
	}

	private void toRecords() {
		window.removeKeyListener(window.menu);
		window.remove(window.menu);
		
		RecordPanel record = new RecordPanel(window);
		window.record = record;
		
		window.pack();
		window.add(record);
		window.addKeyListener(record);
		window.setSize(WIDTH, HEIGHT);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.requestFocus();
		window.setVisible(true);
		window.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}
