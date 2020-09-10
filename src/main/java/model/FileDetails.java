package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class FileDetails {
    LinkedList<Word> words;
    public String name, date;
    public String author, path;
    public int countWord, lineCount, sentenceCount, paragraphCount, characterCount;

    public FileDetails(String name, String author, String date, String path){
        words = new LinkedList<>();
        this.name = name;
        this.author = author;
        this.date = date;
        this.path = path;

        // Initializing counters
        countWord = 0;
        lineCount = 0;
        sentenceCount = 0;
        characterCount = 0;
        paragraphCount = 1;
    }



    //Comparing two files by name
    @Override
    public boolean equals(Object obj) {
        FileDetails other = (FileDetails)obj;
        return name.equals(other.name);
    }

    //TODO
    //This class doesn't do anything
    //I'm not sure yet which data structure to use with WORDS
    public void addWord(Word curr, Word prev) {
        if ( words.contains(curr) ) {
                Word word = words.get(words.indexOf(curr));
        }
    }

    //Parse file to words
    //Counting words, lines, sentences and paragraph
    public void parseFile(){
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error with buffer reader of " + name);
            System.exit(1);
        }
        String line;

        // Reading line by line from the
        // file until a null is returned
        try {
            while ((line = reader.readLine()) != null) {
                lineCount += 1;
                if (line.equals("")) {
                    paragraphCount++;
                } else {
                    characterCount += line.length();

                    // \\s+ is the space delimiter in java
                    String[] wordList = line.split("\\s+");

                    // Add words to data base
                    for (int i = 0 ; i < wordList.length ; i++ ) {
                        if ( i == 0 ) {
                            Word current = new Word(wordList[i], sentenceCount, paragraphCount);
                            addWord(current, null);
                        }
                    }

                    countWord += wordList.length;

                    // [!?.:]+ is the sentence delimiter in java
                    String[] sentenceList = line.split("[!?.:]+");

                    sentenceCount += sentenceList.length;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }


        //TEST
        System.out.println("Total word count = " + countWord + "\n");
        System.out.println("Total number of sentences = " + sentenceCount+ "\n");
        System.out.println("Total number of characters = " + characterCount+ "\n");
        System.out.println("Total number of lines = " + lineCount+ "\n");
        System.out.println("Number of paragraphs = " + paragraphCount+ "\n");

    }
}
