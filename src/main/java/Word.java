import java.util.ArrayList;

public class Word {
    String thisWord;
    ArrayList<WordLocation> wordLocations;

    public Word(String thisWord, int line, int paragraph){
        this.thisWord = thisWord;

    }

    @Override
    public boolean equals(Object obj) {
        Word other = (Word)obj;
        return thisWord.equals(other.thisWord);
    }
}
