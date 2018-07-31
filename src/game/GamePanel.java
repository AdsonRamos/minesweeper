package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

	/**
	 * 
	 * @author Adson Ramos
	 * 
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Square[][] squares;

	private int mouseMovementX, mouseMovementY;
	private int mouseClickedX, mouseClickedY;

	private BufferedImage clock;
	private BufferedImage bomb;

	private int count = 0, seconds = 0, minutes = 0;

	private Timer timer;

	private Random r;

	public static final int N = 12;
	public int n = N;

	private boolean gameOver = false;

	private boolean win = false;

	private File recordFile;

	private FileWriter writer;

	private int currRecord;

	private Scanner s;
	
	private int nClicks = 0;

	public GamePanel() throws IOException {
		super();
		this.setBackground(Color.WHITE);

		addMouseListener(this);
		addMouseMotionListener(this);

		r = new Random();

		recordFile = new File("res/records.dat");

		if (!recordFile.exists()) {
			recordFile.createNewFile();
		} else {
			s = new Scanner(recordFile);
			if (s.hasNextLine()) {
				currRecord = s.nextInt();
				s.close();
			} else {
				currRecord = Integer.MAX_VALUE;
			}
		}


		clock = ImageIO.read(new File("res/timer.png"));
		bomb = ImageIO.read(new File("res/bomb2.png"));

		setMines();

		setMatrixMines();

		timer = new Timer(20, this);
		timer.start();
	}

	private void setMines() {

		squares = new Square[10][10];

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				squares[i][j] = new Square(40 * j + 25 + 5 * j, 40 * i + 20 + 5 * i, i, j, this);
			}
		}

		n = N;

		int aux = 0;

		while (aux < 12) {
			int i = r.nextInt(10);
			int j = r.nextInt(10);
			if (!squares[i][j].isMine()) {
				squares[i][j].setMine(true);
				aux++;
			}
		}
		
		/*for(int i = 0; i < squares.length; i++) {
			for(int j = 0; j < squares[i].length; j++) {
				if(squares[i][j].isMine()) System.out.print("* ");
				else System.out.print("- ");
			}
			System.out.println();
		}*/
	}

	private void setMatrixMines() {
		int n;
		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares[0].length; j++) {
				n = 0;
				if ((i - 1 >= 0) && (j - 1 >= 0) && squares[i - 1][j - 1].isMine())
					n++;
				if ((i - 1 >= 0) && squares[i - 1][j].isMine())
					n++;
				if ((i - 1 >= 0) && (j + 1 < squares[i].length) && squares[i - 1][j + 1].isMine())
					n++;
				if ((j - 1 >= 0) && squares[i][j - 1].isMine())
					n++;
				if ((j + 1 < squares[i].length) && squares[i][j + 1].isMine())
					n++;
				if ((i + 1 < squares.length) && (j - 1 >= 0) && squares[i + 1][j - 1].isMine())
					n++;
				if ((i + 1 < squares.length) && squares[i + 1][j].isMine())
					n++;
				if ((i + 1 < squares.length) && (j + 1 < squares[i].length) && squares[i + 1][j + 1].isMine())
					n++;
				squares[i][j].setAdjacentMines(n);

			}
		}
	}

	public void update() {
		updateSquares();

		if (gameOver) {
			
			Object[] options = { "Sair", "Jogar novamente" };
			this.repaint();
			int dialog = JOptionPane.showOptionDialog(null, "O que você deseja fazer?", "Derrota",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (dialog == 1) {
				respawn();
			} else {
				System.exit(0);
			}
		} else {
			int count = 0;
			for (int i = 0; i < squares.length; i++) {
				for (int j = 0; j < squares[i].length; j++) {
					if (squares[i][j].isDiscovered() && !squares[i][j].isMine()) {
						count++;
					}
				}
			}
			if (count == 88) {
				win = true;
				Object[] options = { "Sair", "Jogar novamente" };
				this.repaint();
				int dialog = 0;
				if (minutes * 60 + seconds < currRecord) {
					dialog = JOptionPane.showOptionDialog(null,
							"Você possui o novo record.\n " + "O que você deseja fazer?", "Vitória",
							JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					try {
						writer = new FileWriter(recordFile);

						writer.write((int) (minutes * 60 + seconds) + "\n");
						writer.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					dialog = JOptionPane.showOptionDialog(null, "O que você deseja fazer?", "Vitória",
							JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				}

				if (dialog == 1) {
					respawn();
				} else {
					System.exit(0);
				}
				count = 0;
			}
		}
	}

	private void respawn() {
		setMines();
		setMatrixMines();
		this.repaint();
		gameOver = false;
		count = 0;
		minutes = 0;
		seconds = 0;
		nClicks = 0;
	}

	private void updateSquares() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (squares[i][j] == null)
					return;
				squares[i][j].update(mouseMovementX, mouseMovementY);
			}
		}
	}

	private void verifyClick(int mouseClickedX, int mouseClickedY, boolean leftClicked) {
		A: for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (squares[i][j].mouseClicked(mouseClickedX, mouseClickedY, leftClicked)) {
					if (squares[i][j].isCovered())
						return;
					nClicks++;
					if (!squares[i][j].isMine() && squares[i][j].getMinesAdjacent() == 0) {
						// executa o flood
						floodfill(squares, i, j);
					} else if (squares[i][j].getMinesAdjacent() != 0) {
						squares[i][j].discover();
					} else if (squares[i][j].isMine()) {
						if(nClicks == 1) {
							respawn();
							verifyClick(mouseClickedX, mouseClickedY, leftClicked);
						} else {
							for (int m = 0; m < squares.length; m++) {
								for (int n = 0; n < squares[0].length; n++) {
									if (squares[m][n].isMine())
										squares[m][n].discover();
								}
							}
							gameOver = true;
						}
						
						break A;
					}
				}
			}
		}
	}

	private void floodfill(Square[][] squares, int i, int j) {
		if (squares[i][j].isDiscovered() || squares[i][j].isMine())
			return;
		else if (squares[i][j].getMinesAdjacent() != 0) {
			squares[i][j].discover();
			return;
		} else {
			squares[i][j].discover();
		}
		if ((i - 1 >= 0) && (j - 1 >= 0))
			floodfill(squares, i - 1, j - 1);
		if (i - 1 >= 0)
			floodfill(squares, i - 1, j);
		if ((i - 1 >= 0) && (j + 1 < squares[i].length))
			floodfill(squares, i - 1, j + 1);
		if (j - 1 >= 0)
			floodfill(squares, i, j - 1);
		if (j + 1 < squares[i].length)
			floodfill(squares, i, j + 1);
		if ((i + 1 < squares.length) && (j - 1 >= 0))
			floodfill(squares, i + 1, j - 1);
		if (i + 1 < squares.length)
			floodfill(squares, i + 1, j);
		if ((i + 1 < squares.length) && (j + 1 < squares[i].length))
			floodfill(squares, i + 1, j + 1);
		return;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(clock, 550, 50, null);
		g2.drawRect(530, 120, 100, 40);
		g2.setFont(new Font("Arial", Font.BOLD, 27));

		if (seconds < 10) {
			g2.drawString(minutes + ":0" + seconds, 545, 150);
		} else {
			g2.drawString(minutes + ":" + seconds, 545, 150);
		}

		// draw chronometer

		drawMap(g2);

		g2.drawImage(bomb, 560, 300, null);
		g2.setColor(Color.BLACK);
		g2.drawRect(557, 360, 40, 40);

		g2.setFont(new Font("Arial", Font.BOLD, 22));
		g2.drawString("" + n, 565, 387);
		// draw n bombs

		g.dispose();
	}

	private void drawMap(Graphics2D g) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				squares[i][j].render(g);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mouseClickedX = e.getX();
		mouseClickedY = e.getY();

		verifyClick(mouseClickedX, mouseClickedY, e.getButton() == MouseEvent.BUTTON1);
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
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseMovementX = e.getX();
		mouseMovementY = e.getY();

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		count++;
		if (count % 50 == 0) {
			seconds++;
		}
		if (seconds == 60) {
			seconds = 0;
			minutes++;
		}

		update();
		repaint();
	}
}
