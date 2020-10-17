package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class WaitingFrame extends JFrame {
    JLabel label, gifLabel;
    JPanel panel;
    Icon sandClock;

    final Color DEFAULT = new Color(206, 200, 200, 2);
    final Border BORDER = BorderFactory.createLineBorder(DEFAULT, 35);
    final Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,20);

    public WaitingFrame(){
        panel = new JPanel();
        panel.setBorder(BORDER);
        panel.setLayout(new BorderLayout());

        label = new JLabel("Please wait for all files to be uploaded . . . .");
        label.setFont(MY_FONT);

        gifLabel = new JLabel();

        try {
            sandClock = new ImageIcon( getClass().getResource("/sand clock loading animation.gif"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        gifLabel.setIcon(sandClock);

        panel.add(label, BorderLayout.NORTH);
        panel.add(gifLabel, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);

    }

}
