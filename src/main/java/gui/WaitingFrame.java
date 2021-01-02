package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Waiting frame is used when uploading new books to data base
 * including message and sand clock animation
 */
public class WaitingFrame extends JFrame {
	private JLabel label, gifLabel;
	private JPanel panel;
	private Icon sandClock;

	private static final Color DEFAULT = new Color(206, 200, 200, 2);
	private static final Border BORDER = BorderFactory.createLineBorder(DEFAULT, 35);
	private static final Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT, 20);

	/**
	 * Constructor for waiting frame
	 *
	 * @param title - title for frame
	 */
	public WaitingFrame(String title) {
		panel = new JPanel();
		panel.setBorder(BORDER);
		panel.setLayout(new BorderLayout());

		label = new JLabel(title);
		label.setFont(MY_FONT);

		gifLabel = new JLabel();

		try {
			sandClock = new ImageIcon(getClass().getResource("/sand clock loading animation.gif"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		gifLabel.setIcon(sandClock);

		panel.add(label, BorderLayout.NORTH);
		panel.add(gifLabel, BorderLayout.CENTER);

		add(panel, BorderLayout.CENTER);
	}

}
