import gui.ShowWords;
import model.Book;
import model.Group;
import model.WordLocation;
import org.junit.jupiter.api.Test;
import service.*;

import javax.swing.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbTest {

	@Test
	public void wordLoadTest() {
		DbConnection.initSchema();
		String bookPath = "D:\\uni\\sql_seminar\\text_books\\11-0.txt";
		Book b = new Book(bookPath);
		List<WordLocation> wordLocationList = new FileParser().parseFile(b);
		BookService.insertBook(b);
		Date start = new Date();
//		Map<String, Long> words = WordService.getAllWordsId();
		List<WordLocation> wordLocationListCopy = new LinkedList<>();
		for (WordLocation wordLocation : wordLocationList) {
			if (!wordLocation.getWord().isEmpty())
//			Long wordId = words.get(wordLocation.getWord());
//			if (wordId == null)
//				wordId = WordService.insertWord(wordLocation.getWord());
//			if (wordId > 0) {
//				wordLocation.setWordId(wordId);
				wordLocationListCopy.add(wordLocation);
//			}
		}
		WordService.addWordLocationList(wordLocationListCopy, b.getId());
		Date end = new Date();
		long seconds = (end.getTime() - start.getTime()) / (1000);
		System.out.println(seconds + " secs");
	}

	@Test
	public void init() {
		//to init the schema
		DbConnection.initSchema();
	}

	@Test
	public void testWordService() {
		System.out.println(WordService.insertWord("am"));
		System.out.println(WordService.insertWord("but"));
		System.out.println(WordService.insertWord("buy"));
		System.out.println(WordService.insertWord("am"));

		System.out.println(WordService.findWordByValue("am"));
		System.out.println(WordService.findWordByValue("buy"));
		System.out.println(WordService.findWordByValue("no"));
	}

	public void testBookService() {
		Book book = new Book("Book2", "Author2", "", new Date());
		BookService.insertBook(book);

		List<Book> books = BookService.findBookByDetails("Book", null, null, null, null);
		System.out.println("Found " + books.size() + " books");
	}

	@Test
	public void testWordParsing() {
		String word = "“without";
		List<WordLocation> ws = new FileParser().addWordLocationToList(new Book(""),new String[]{word});
		WordLocation w = ws.get(0);
		System.out.println((w.isQuoteBefore() ? "\"" : "") + w.getWord() + (w.isQuoteAfter() ? "\"" : "") +
				(w.getPunctuationMark() == null ? "" : w.getPunctuationMark()));
	}

	@Test
	public void testPreview() {
		List<WordLocation> wordLocations = WordService.findWordInBooks("conversations",null);

		System.out.println(WordService.buildPreview(wordLocations.get(0).getBookId(), wordLocations.get(0).getParagraph()));
	}

	@Test
	public void testWordsAppearance(){
		Map<String, Integer> map = WordService.getWordsAppearances(null);
		System.out.println(map.size());
		for (Map.Entry<String, Integer> e: map.entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
		}

		System.out.println("-----------------------------");
		Map<String, Integer> map2 = WordService.getWordsAppearances(new Long(4));
		System.out.println(map2.size());
		for (Map.Entry<String, Integer> e: map2.entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
		}
	}

	@Test
	public void testFindWordInBooks(){
		List<Book> books = BookService.getAllBooks();

		ArrayList<String> words= new ArrayList<>();
		words.add("will");
		words.add("when");
		words.add("for");

		for(String word : words){
			System.out.println("***");
			System.out.println("Word : " + word);
			for (Book book : books) {
				List<WordLocation> locations = WordService.findWordInBooks(word, book.getId());
				System.out.println("In \'" + book.getTitle() + "\'");
				System.out.println("Number of locations : " + locations.size());
			}
		}
	}

	@Test
	public void testGroupService(){
		String groupName = "animals";
		Map<String, Integer> groupMap = GroupService.getAllGroups();
		int groupId = GroupService.createNewGroup(groupName);
		GroupService.addWordToGroup("cat", groupId);
		GroupService.addWordToGroup("dog", groupId);
		groupMap = GroupService.getAllGroups();
		System.out.println(GroupService.getAllWordsForGroup(groupMap.get(groupName)));
	}
}
