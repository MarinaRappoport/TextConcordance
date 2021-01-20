package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Waiting frame is used when uploading new books to data base
 * including message and sand clock animation
 */
public class WaitingFrame extends JFrame {
	private JLabel label, gifLabel;
	private JPanel panel;
	private Icon sandClock;


	/**
	 * Constructor for waiting frame
	 *
	 * @param title - title for frame
	 */
	public WaitingFrame(String title) {
		panel = new JPanel();
		panel.setBorder(GuiConstants.BORDER);
		panel.setLayout(new BorderLayout());

		label = new JLabel(title);
		label.setFont(GuiConstants.MY_FONT);

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
