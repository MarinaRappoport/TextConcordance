package gui;

import model.Book;
import service.FilesManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

//GUI of adding file window
public class AddFileFrame extends JFrame {
    JButton addFile, ok;
    JLabel enterDate, enterAuthor , chooseFile;
    JPanel mainPanel;
    JTextField dateTF, authorTF;
    FilesManager filesManager;
    String fileName, filePath;

    //final Font TITLE_FONT = new Font("Title", Font.TRUETYPE_FONT,22);
    final Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,20);
    final Color PRIMARY_COLOR = new Color(18, 163, 134, 99);
    final Color SECONDARY_COLOR = new Color(18, 163, 134, 190);
    final Border BORDER = BorderFactory.createLineBorder(PRIMARY_COLOR, 2);

    public AddFileFrame(){
        filesManager = FilesManager.getInstance();

        mainPanel = new JPanel();
        TitledBorder title = BorderFactory.createTitledBorder(BORDER, "Add New File", 0, 0, new Font("Font", Font.BOLD,22));
        mainPanel.setBorder(title);
        mainPanel.setLayout(new GridLayout(3, 2, 20, 20));

        enterAuthor = new JLabel("Enter author name");
        enterAuthor.setFont(MY_FONT);
        enterDate = new JLabel("Enter date of release");
        enterDate.setFont(MY_FONT);
        chooseFile = new JLabel("Choose a file");
        chooseFile.setFont(MY_FONT);

        //Choose file with JFileChooser
        addFile = new JButton("Select file");
        addFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("File selection:");
                jfc.setMultiSelectionEnabled(false);
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    addFile.setText(selectedFile.getName());
                    filePath = selectedFile.getAbsolutePath();
                    fileName = selectedFile.getName();
                }
                }

        });

        dateTF = new JTextField("--/--/--");
        authorTF = new JTextField();
        //TODO : check date format

        ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book newFile = new Book(fileName, authorTF.getText(), dateTF.getText(), filePath);
                filesManager.addFile(newFile);
            }
        });
        getRootPane().setDefaultButton(ok);

        mainPanel.add(chooseFile);
        mainPanel.add(addFile);
        mainPanel.add(enterAuthor);
        mainPanel.add(authorTF);
        mainPanel.add(enterDate);
        mainPanel.add(dateTF);

        //add(title, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(ok, BorderLayout.SOUTH);


    }
}
