import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

public class GamePackMenuChoice extends JPanel {
	private String name;
	private JLabel label;
	private JPanel content;
	private BufferedImage image;
	private JLabel imageLabel;
	private GamePackFrame gamePackFrame;

	public GamePackMenuChoice(GamePackFrame gamePackFrame) {
		this(gamePackFrame, "");
	}
	public GamePackMenuChoice(GamePackFrame gamePackFrame, String name) {
		super(new GridBagLayout());
		this.name = name;
		this.gamePackFrame = gamePackFrame;

		

		setOpaque(true);
		setBorder(BorderFactory.createRaisedBevelBorder());
		setBackground(Color.LIGHT_GRAY);

		label = new JLabel(name);
		label.setFont(label.getFont().deriveFont(15.0f));

		add(label);

		addMouseListener(new MouseListener() {
			public void mouseEntered(MouseEvent e) {
				setBackground(Color.LIGHT_GRAY.darker());
			}
			public void mouseExited(MouseEvent e) {
				setBackground(Color.LIGHT_GRAY);
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseReleased(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
				//System.out.println("Hey there. My name is " + name);
				//launchGame(name);
				if (SwingUtilities.isLeftMouseButton(e)) {
					gamePackFrame.startGame(name);
				}
			}
		});


		//label = new JLabel(name);

		//add(label);
	}

	/*public void launchGame(String gameName) {
		switch(gameName) {
			case "battleship":
				Battleship battleship = new Battleship(gamePackFrame);
				break;
		}
	}*/

	public String getName() {
		return name;
	}

	/*@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) {
			g.drawImage(image, 0, 0, this);
		}
	}*/
}