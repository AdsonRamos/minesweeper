package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Square {

	private int x, y;
	public static final int WIDTH = 40;
	public static final int HEIGHT = 40;
	
	private GamePanel game;

	private Rectangle bounds;

	private BufferedImage image;
	
	private BufferedImage flag;
	private BufferedImage question;

	private boolean leftClicked;
	
	private boolean covered = false;
	
	private int rightClickedTimes = 0;
	
	private boolean discovered = false;

	private boolean mine = false;
	private boolean blocked;
	private int minesAdjacent;
	
	private int i, j;

	private boolean mouseColliding = false;
	
	public Square(int x, int y, int i, int j, GamePanel game) {
		this.x = x;
		this.y = y;

		this.i = i;
		this.j = j;
		
		bounds = new Rectangle(x, y, WIDTH, HEIGHT);
		this.game = game;
		
		try {
			flag = ImageIO.read(new File("res/flag.png"));
			question = ImageIO.read(new File("res/question.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void update(int mouseX, int mouseY) {
		if (bounds.contains(mouseX, mouseY)) {
			mouseColliding = true;
		} else {
			mouseColliding = false;
		}
	}

	public boolean mouseClicked(int mouseClickedX, int mouseClickedY, boolean leftClicked) {
		
		if (bounds.contains(mouseClickedX, mouseClickedY)) {
			if(!leftClicked) {
				rightClickedTimes++;
				if(rightClickedTimes == 1) {
					game.n--;
				} else if(rightClickedTimes == 2) {
					game.n++;
				}
				if(rightClickedTimes > 2) {
					rightClickedTimes = 0;
					covered = false;
				}
			}
			return this.leftClicked = leftClicked;
		} else {
			return false;
		}
	}

	public void discover() {
		discovered = true;
	}
	
	public boolean isDiscovered() {
		return discovered;
	}
	
	public boolean isCovered() {
		return covered;
	}
	
	public void render(Graphics2D g) {
		if (mouseColliding) {
			g.setColor(Color.GREEN.darker().darker());
		} else {
			g.setColor(Color.GREEN.darker());
		}
		if (!discovered) {
			g.fillRect(x, y, WIDTH, HEIGHT);
			if(rightClickedTimes == 1) {
				g.drawImage(flag, x+4, y+4, null);
				covered  = true;
			} else if(rightClickedTimes == 2) {
				g.drawImage(question, x+4, y+4, null);
				covered = true;
			}
		} else if (discovered && !mine) {
			g.drawRect(x, y, WIDTH, HEIGHT);
			g.drawImage(image, x + 4, y + 4, null);
		} else if (discovered && mine) {
			g.drawRect(x, y, WIDTH, HEIGHT);
			g.drawImage(image, x + 4, y + 4, null);
		}

	}

	public boolean isMine() {
		return mine;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setMine(boolean mine) {
		this.mine = mine;
	}

	public void setAdjacentMines(int mines) {
		try {
			if (mine) {
				image = ImageIO.read(new File("res/bomb.png"));
			}
			else if (mines != 0) {
				this.minesAdjacent = mines;
				image = ImageIO.read(new File("res/0" + this.minesAdjacent + ".png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getMinesAdjacent() {
		return minesAdjacent;
	}
}

