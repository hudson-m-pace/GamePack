import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;

public class Tile extends JPanel {
	private int x, y, value, max;
	private JLabel text;
	public Tile(int x, int y, int value, int max) {
		super(new GridBagLayout());
		setBorder(BorderFactory.createLineBorder(Color.RED, 2));
		this.x = x;
		this.y = y;
		this.value = value;
		this.max = max;
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.VERTICAL;
		c.weightx = 1;
		c.weighty = 1;
		if (value != max) {
			text = new JLabel(value + "");
		}
		else {
			text = new JLabel("");
		}
		text.setFont(new Font("Serif", Font.PLAIN, 50));
		add(text, c);
	}

	public int getValue() {
		return value;
	}
	public int getGridX() {
		return x;
	}
	public int getGridY() {
		return y;
	}
	public void setValue(int value) {
		this.value = value;
		if (value == max) {
			text.setText("");
		}
		else {
			text.setText(value + "");
		}
	}
	public void changeBorderColor() {
		setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
	}
}