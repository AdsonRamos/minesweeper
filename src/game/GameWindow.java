package game;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GameWindow extends JFrame{

	/**
	 * @author Adson Ramos
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected MenuPanel menu;
	protected GamePanel game;
	protected RecordPanel record;
	
	public GameWindow() {
		super("Campo Minado");

		menu = new MenuPanel(this);
		
		this.setSize(700, 520);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		try {
			this.add(menu);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
		try {
			setIconImage(ImageIO.read(new File("res/bomb_ico.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new GameWindow();
	}

}
