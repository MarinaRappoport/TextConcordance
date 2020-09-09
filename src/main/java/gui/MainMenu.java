package gui;

import gui.AddFileFrame;
import model.FileDetails;
import service.FilesManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

//GUI of the main menu
public class MainMenu extends JFrame {
    JTextArea statTextArea;
    JList filesList;
    JPanel buttons, detailsAndStat, filesDetailsPanel, filesDetailsAndList;
    JButton loadFile, showWords, showExp, showGroups;
    FilesManager filesManager;
    ArrayList<String> allFiles;
    ArrayList<String > selectedFiles;

    final Color PRIMARY_COLOR = new Color(18, 163, 134, 99);
    final Color SECONDARY_COLOR = new Color(18, 163, 134, 190);
    final Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,20);
    final Border BORDER = BorderFactory.createLineBorder(SECONDARY_COLOR, 2);
    final Border MATT_BORDER = new MatteBorder(0,0,1,0,PRIMARY_COLOR);

    public MainMenu(){

        //Create an instance of the files data base
        filesManager = FilesManager.getInstance();
        FilesManager.setMainMenu(this);

        //Set files list
        allFiles = new ArrayList<>();
        filesList = new JList<>();
        filesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        filesList.setBorder(BORDER);

        filesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if ( !filesList.getSelectedValuesList().isEmpty() && (e.getValueIsAdjusting())) {
                    selectedFiles = (ArrayList<String>) filesList.getSelectedValuesList();
                    System.out.println(selectedFiles);
                }
            }
        });

        filesDetailsAndList = new JPanel();
        filesDetailsAndList.setBackground(Color.WHITE);

        filesDetailsPanel = new JPanel();
        filesDetailsPanel.setLayout(new GridLayout(0,4,2,2));
        TitledBorder detailsTitle = BorderFactory.createTitledBorder(BORDER, "Files Details", 0,
                0, new Font("Font", Font.BOLD,16));
        filesDetailsAndList.setBorder(detailsTitle);

        createFilesDetailsTitles();

        statTextArea = createTextArea("Statistics");

        //create the panel that contains the details and statistics
        detailsAndStat = new JPanel();
        detailsAndStat.setLayout(new GridLayout(2,1,0,0));
        JScrollPane statSP = new JScrollPane(statTextArea);
        filesDetailsAndList.add(filesDetailsPanel, BorderLayout.CENTER);
        filesDetailsAndList.add(filesList, BorderLayout.EAST);
        detailsAndStat.add(filesDetailsAndList);
        detailsAndStat.add(statSP);

        //create buttons panels
        buttons = new JPanel();
        buttons.setLayout( new GridLayout(4, 1, 4, 12) );
        buttons.setBackground(PRIMARY_COLOR);
        buttons.setBorder(BorderFactory.createMatteBorder(100, 7, 100, 7, PRIMARY_COLOR));

        //create buttons
        loadFile = new JButton("Load New File");
        loadFile.setFont(MY_FONT);
        loadFile.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadFile.setPreferredSize(new Dimension(200, 40));

        loadFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AddFileFrame addFileFrame = new AddFileFrame();
                addFileFrame.setSize(700, 330);
                addFileFrame.setVisible(true);
                addFileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });

        showWords = new JButton("Show Words");
        showWords.setFont(MY_FONT);
        showWords.setAlignmentX(Component.CENTER_ALIGNMENT);
        showWords.setPreferredSize(new Dimension(200, 40));

        showExp = new JButton("Show Expressions");
        showExp.setFont(MY_FONT);
        showExp.setAlignmentX(Component.CENTER_ALIGNMENT);
        showExp.setPreferredSize(new Dimension(200, 40));

        showGroups = new JButton("Show Groups");
        showGroups.setFont(MY_FONT);
        showGroups.setAlignmentX(Component.CENTER_ALIGNMENT);
        showGroups.setPreferredSize(new Dimension(200, 40));

        buttons.add(loadFile);
        buttons.add(showWords);
        buttons.add(showGroups);
        buttons.add(showExp);

        add(buttons, BorderLayout.WEST);
        add(detailsAndStat, BorderLayout.CENTER);


    }

    public void updateStatistics() {

    }

    //Create files details panels titles
    public void createFilesDetailsTitles(){
        JLabel nameT = new JLabel("File Name");
        JLabel authorT = new JLabel("Author Name");
        JLabel dateT = new JLabel("Date Of Release");
        JLabel pathT = new JLabel("Path");

        nameT.setBorder(MATT_BORDER);
        authorT.setBorder(MATT_BORDER);
        dateT.setBorder(MATT_BORDER);
        pathT.setBorder(MATT_BORDER);

        nameT.setFont(MY_FONT);
        authorT.setFont(MY_FONT);
        dateT.setFont(MY_FONT);
        pathT.setFont(MY_FONT);

        nameT.setBackground(Color.WHITE);
        authorT.setBackground(Color.WHITE);
        dateT.setBackground(Color.WHITE);
        pathT.setBackground(Color.WHITE);

        nameT.setOpaque(true);
        authorT.setOpaque(true);
        dateT.setOpaque(true);
        pathT.setOpaque(true);

        filesDetailsPanel.add(nameT);
        filesDetailsPanel.add(authorT);
        filesDetailsPanel.add(dateT);
        filesDetailsPanel.add(pathT);

        if ( filesManager.isEmpty() )
            filesDetailsPanel.add( new JLabel("No files to show"));
    }

    public void updateFileDetails(ArrayList<FileDetails> files) {

        //Remove previous details and recreate all
        allFiles.clear();
        filesDetailsPanel.removeAll();
        createFilesDetailsTitles();

        //Iterating on files ArrayList and add all details to filesDetailsPanel
        Iterator<FileDetails> iter = files.iterator();
        while (iter.hasNext()){
            FileDetails current = iter.next();

            allFiles.add(current.name);
            filesList.setListData(allFiles.toArray());

            JLabel name = new JLabel(current.name);
            name.setBorder(MATT_BORDER);
            JLabel author = new JLabel(current.author);
            author.setBorder(MATT_BORDER);
            JLabel date = new JLabel(current.date);
            date.setBorder(MATT_BORDER);
            JLabel path = new JLabel(current.path);
            path.setBorder(MATT_BORDER);

            filesDetailsPanel.add(name);
            filesDetailsPanel.add(author);
            filesDetailsPanel.add(date);
            filesDetailsPanel.add(path);
        }

        filesDetailsAndList.revalidate();
        filesDetailsPanel.revalidate();
    }


    public JTextArea createTextArea(String name) {
        JTextArea tArea = new JTextArea();
        tArea.setEditable(false);
        tArea.setColumns(20);
        tArea.setRows(7);
        TitledBorder title = BorderFactory.createTitledBorder(BORDER, name, 0, 0, new Font("Font", Font.BOLD,16));
        tArea.setBorder(title);

        return tArea;
    }


    public void updateDetails(int countWord, int sentenceCount, int characterCount, int lineCount, int paragraphCount) {
    }
}
