package gui;

import model.Book;
import service.FilesManager;
import service.WordService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextPreviewComponent extends JPanel {
    private JPanel buttons;
    private JTextArea preview;
    private JButton next, prev;
    private long currentBookId;
    private int currentParagraph;
    private FilesManager filesManager;
    private String[] currentSearch;

    final static Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,18);
    private final static Color DEFAULT = new Color(206, 200, 200, 2);
    private final static Border BORDER = BorderFactory.createLineBorder(DEFAULT, 2);

    public TextPreviewComponent(boolean isPhrase){
        buttons = new JPanel();

        filesManager = FilesManager.getInstance();
        currentBookId = -1;
        currentParagraph = -1;

        preview = new JTextArea(14,150);
        preview.setEditable(false);
        preview.setLineWrap(true);
        preview.setWrapStyleWord(true);
        TitledBorder title = BorderFactory.createTitledBorder
                (BORDER, "Preview", 0, 0, new Font("Font", Font.BOLD,18));
        title.setTitleJustification(TitledBorder.CENTER);
        preview.setBorder(title);
        JScrollPane contextSP =new JScrollPane(preview);
        contextSP.setVisible(true);

        next = new JButton(">");
        prev = new JButton("<");

        ActionListener scrollParagraphs = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( ( currentBookId < 0) || ( currentParagraph < 0 ) ){
                    return;
                }

                Book current = filesManager.getFile(currentBookId);
                if ( e.getSource().equals(next)) {
                    if (current.getParagraphCount() > currentParagraph)
                        if (!isPhrase)
                            createPreview(currentSearch, currentBookId, currentParagraph + 1);
                        else
                            createPhrasePreview(currentSearch, currentBookId, currentParagraph +1);
                }
                else {
                    if ( currentParagraph > 0 )
                        if (!isPhrase)
                            createPreview(currentSearch, currentBookId, currentParagraph - 1);
                        else
                            createPhrasePreview(currentSearch, currentBookId, currentParagraph -1 );
                }
            }
        };

        prev.addActionListener(scrollParagraphs);
        next.addActionListener(scrollParagraphs);

        buttons.add(prev);
        buttons.add(next);

        setLayout(new BorderLayout());
        add(contextSP, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    public void createPreview(String[] word, long bookId, int paragraph){
        currentSearch = word;
        currentBookId = bookId;
        currentParagraph = paragraph;

        String text = WordService.buildPreview(bookId,paragraph);
        preview.setText(text);

        Highlighter highlighter = preview.getHighlighter();
        Highlighter.HighlightPainter painter =
                new DefaultHighlighter.DefaultHighlightPainter(Color.pink);

        String lowerText = text.toLowerCase();
        for (int i = 0; i < word.length; i++) {

            int index = 0;
            while (index >= 0) {
                int p0 = lowerText.indexOf(word[i].toLowerCase(), index);

                if (p0 == -1) {
                    break;
                }

                int p1 = p0 + word[i].length();

                if ((p0 == 0) || (!Character.isLetter(lowerText.charAt(p0 - 1)))) {
                    if (!Character.isLetter(lowerText.charAt(p1))) {
                        try {
                            highlighter.addHighlight(p0, p1, painter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                index = p1;
            }
        }
    }

    public void createPhrasePreview(String[] phrase, long bookId, int paragraph) {
        currentSearch = phrase;
        currentBookId = bookId;
        currentParagraph = paragraph;

        String text = WordService.buildPreview(bookId, paragraph);
        preview.setText(text);

        Highlighter highlighter = preview.getHighlighter();
        Highlighter.HighlightPainter painter =
                new DefaultHighlighter.DefaultHighlightPainter(Color.pink);

        int index = 0;
        while (index >= 0) {
            int p0 = text.indexOf(phrase[0], index);

            if (p0 == -1) {
                break;
            }

            int p1 = p0 + phrase[0].length();

            for ( int j = 1 ; j < phrase.length ; j++ ) {
                //If it's not sub-word of other word
                if ((p0 == 0) || (!Character.isLetter(text.charAt(p0 - 1)))) {
                    if (!Character.isLetter(text.charAt(p1))) {
                        // if next word equals the next word in phrase
                        if ( text.substring(p1+1, p1+1+phrase[j].length()).equals(phrase[j]) ) {
                            p1 += 1 + phrase[j].length();

                            //We have reached a point where the next word is the last word in the phrase
                            if ( j == (phrase.length  - 1) ){
                                try {
                                    highlighter.addHighlight(p0, p1, painter);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                    else break;
                }
            }

            index = p1;

        }

    }

    public void clearText() {
        preview.setText("");
    }
}
