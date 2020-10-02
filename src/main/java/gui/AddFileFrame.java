package gui;

import model.Book;
import service.FilesManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

//GUI of adding file window
public class AddFileFrame extends JFrame {
    JButton addFile, ok, prev, next;
    int currentBookIndex;
    JLabel title, releaseDate, author, translator, chooseFile, currPage;
    JPanel mainPanel, pagesPanel, bottomPanel, okPanel;
    JTextField dateTF, authorTF, titleTF, translatorTF;
    FilesManager filesManager;
    String fileName, filePath;
    ArrayList <Book> books;

    //final Font TITLE_FONT = new Font("Title", Font.TRUETYPE_FONT,22);
    final Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,20);
    final Color PRIMARY_COLOR = new Color(18, 163, 134, 99);
    final Color SECONDARY_COLOR = new Color(18, 163, 134, 190);
    final Color DEFAULT = new Color(206, 200, 200, 2);
    final Border BORDER = BorderFactory.createLineBorder(DEFAULT, 40);

    public AddFileFrame(){
        books = new ArrayList<>();
        filesManager = FilesManager.getInstance();
        currentBookIndex = 0;

        mainPanel = new JPanel();
        mainPanel.setBorder(BORDER);
        mainPanel.setLayout(new GridLayout(5, 2, 20, 20));

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

                        String bookTitle = x.getName();
                        String bookAuthor = "";
                        String bookTranslator = "";
                        String bookRDate = "";

                        Book newBook = new Book(bookTitle, bookAuthor, bookTranslator, bookRDate, x.getAbsolutePath());
                        books.add(newBook);
                    });

                    updateBookDetails();

                    //addFile.setText(selectedFile.getName());
                    //filePath = selectedFile.getAbsolutePath();
                    //fileName = selectedFile.getName();
                }
                }

        });

        titleTF = new JTextField();
        translatorTF = new JTextField();
        dateTF = new JTextField("--/--/--");
        authorTF = new JTextField();
        //TODO : check date format

        //Create labels and buttons on the bottom panel :

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( e.getSource() == prev ) {
                    if ( currentBookIndex > 0 )
                        currentBookIndex -= 1;
                } else {
                    if ( currentBookIndex < books.size() )
                        currentBookIndex += 1;
                }

                updateBookDetails();
            }
        };

        prev = new JButton("previous");
        prev.addActionListener(listener);
        next = new JButton("next");
        next.addActionListener(listener);
        currPage = new JLabel(currentBookIndex + "/0");


        ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Iterator<Book> iter = books.iterator();
                while (iter.hasNext()) {
                    filesManager.addFile(iter.next()); //TODO
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

        okPanel.add(ok);

        pagesPanel.add(prev);
        pagesPanel.add(currPage);
        pagesPanel.add(next);
        pagesPanel.setBorder(BORDER);

        bottomPanel.add(pagesPanel, BorderLayout.CENTER);
        bottomPanel.add(okPanel, BorderLayout.SOUTH);

        //add(title, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);


    }

    private void updateBookDetails() {
        if ( ( currentBookIndex == 0 ) && !books.isEmpty() )
            currentBookIndex = 1;

        currPage.setText(currentBookIndex + "/" + books.size() );
        titleTF.setText(books.get(currentBookIndex-1).getTitle());

    }
}
