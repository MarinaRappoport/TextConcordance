package gui;

import model.Book;
import model.WordLocation;
import service.FileParser;
import service.FilesManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;

//GUI of adding file window
public class AddFileFrame extends JFrame {
    private JButton addFile, ok, prev, next;
    private int currBook;
    private JLabel title, releaseDate, author, translator, chooseFile, currPage, note;
    private JPanel mainPanel, pagesPanel, bottomPanel, okPanel;
    private JTextField dateTF, authorTF, titleTF, translatorTF;
    private FilesManager filesManager;
	private Map<Book, List<WordLocation>> bookMap;

    //final Font TITLE_FONT = new Font("Title", Font.TRUETYPE_FONT,22);
    final Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,20);
    final Color PRIMARY_COLOR = new Color(18, 163, 134, 99);
    final Color SECONDARY_COLOR = new Color(18, 163, 134, 190);
    final Color DEFAULT = new Color(206, 200, 200, 2);
    final Border BORDER = BorderFactory.createLineBorder(DEFAULT, 35);

    public AddFileFrame(){
	    bookMap = new HashMap<>();
        filesManager = FilesManager.getInstance();
        currBook = 0;

        mainPanel = new JPanel();
        mainPanel.setBorder(BORDER);
        mainPanel.setLayout(new GridLayout(6, 2, 20, 20));

        bottomPanel = new JPanel();
        bottomPanel.setBorder(BORDER);
        bottomPanel.setLayout(new BorderLayout());

        okPanel = new JPanel();
        okPanel.setBorder(BorderFactory.createMatteBorder(1, 90, 1, 90, DEFAULT));

        pagesPanel = new JPanel();
        pagesPanel.setBorder(BORDER);
        pagesPanel.setLayout(new GridLayout(1,3,30,15));

        //Create labels and text fields in the main panel :

        title = new JLabel("Title");
        title.setFont(MY_FONT);
        author = new JLabel("Author");
        author.setFont(MY_FONT);
        translator = new JLabel("Translator");
        translator.setFont(MY_FONT);
        releaseDate = new JLabel("Release date");
        releaseDate.setFont(MY_FONT);
        chooseFile = new JLabel("Choose files");
        chooseFile.setFont(MY_FONT);
        note = new JLabel("*Note : Press enter to save changes");

        //Choose file with JFileChooser
        addFile = new JButton("Select files");
        addFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("File selection:");
                jfc.setMultiSelectionEnabled(true);
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File[] files = jfc.getSelectedFiles();

                    Arrays.asList(files).forEach(x -> {
	                    Book newBook = new Book(x.getAbsolutePath());
	                    List<WordLocation> list = new FileParser().parseFile(newBook);
	                    bookMap.put(newBook, list);
                    });

                    updateBookDetails();

                }
                }

        });

        ActionListener textFieldListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Object[] books = bookMap.keySet().toArray();

                ((Book)books[currBook -1]).setTitle(titleTF.getText());
                ((Book)books[currBook -1]).setTranslator(translatorTF.getText());
                ((Book)books[currBook -1]).setDate(dateTF.getText());
                ((Book)books[currBook -1]).setAuthor(authorTF.getText());

            }
        };

        titleTF = new JTextField();
        translatorTF = new JTextField();
        dateTF = new JTextField();
        authorTF = new JTextField();

        titleTF.addActionListener(textFieldListener);
        translatorTF.addActionListener(textFieldListener);
        dateTF.addActionListener(textFieldListener);
        authorTF.addActionListener(textFieldListener);

        //TODO : check date format

        //Create labels and buttons on the bottom panel :

        ActionListener btnListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( e.getSource() == prev ) {
                    if ( currBook > 0 )
                        currBook -= 1;
                } else {
                    if ( currBook < bookMap.size() )
                        currBook += 1;
                }

                updateBookDetails();
            }
        };

        prev = new JButton("previous");
        prev.addActionListener(btnListener);
        next = new JButton("next");
        next.addActionListener(btnListener);
        currPage = new JLabel(currBook + "/0");


        ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (Map.Entry<Book, List<WordLocation>> entry : bookMap.entrySet()){
                    //TODO alert for waiting and for DONE
                    filesManager.addFile(entry.getKey(), entry.getValue());
                }
            }

        });
        getRootPane().setDefaultButton(ok);

        mainPanel.add(chooseFile);
        mainPanel.add(addFile);
        mainPanel.add(title);
        mainPanel.add(titleTF);
        mainPanel.add(author);
        mainPanel.add(authorTF);
        mainPanel.add(translator);
        mainPanel.add(translatorTF);
        mainPanel.add(releaseDate);
        mainPanel.add(dateTF);
        mainPanel.add(note);

        okPanel.add(ok);

        pagesPanel.add(prev);
        pagesPanel.add(currPage);
        pagesPanel.add(next);
        pagesPanel.setBorder(BORDER);

        bottomPanel.add(pagesPanel, BorderLayout.CENTER);
        bottomPanel.add(okPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);


    }

    private void updateBookDetails() {
        if ( ( currBook == 0 ) && !bookMap.isEmpty() )
            currBook = 1;

        Object[] books = bookMap.keySet().toArray();

        currPage.setText(currBook + "/" + bookMap.size() );
        titleTF.setText(((Book)books[currBook -1]).getTitle());
	    authorTF.setText(((Book)books[currBook -1]).getAuthor());
	    translatorTF.setText(((Book)books[currBook -1]).getTranslator());
		dateTF.setText(((Book)books[currBook -1]).getDate());
    }
}
