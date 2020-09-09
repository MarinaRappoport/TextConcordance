import gui.MainMenu;
import service.FilesManager;

import javax.swing.*;

public class Main {
    static FilesManager filesManager;

    public static void main(String[] args) {


        //start main menu
        MainMenu menu = new MainMenu();
        menu.setExtendedState(JFrame.MAXIMIZED_BOTH);
        menu.setVisible(true);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        filesManager = FilesManager.getInstance();


    }
}
