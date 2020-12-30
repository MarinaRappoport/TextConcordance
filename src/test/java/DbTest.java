import model.Book;
import model.DbData;
import model.WordLocation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

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

	@BeforeAll
	public static void init() {
		//to init the schema
		DbConnection.initSchema();
	}

	@Test
	public void testWordService() {
		System.out.println(WordService.insertWord("am"));
		System.out.println(WordService.insertWord("but"));
		System.out.println(WordService.insertWord("buy"));
		System.out.println(WordService.insertWord("am"));

		System.out.println(WordService.findWordIdByValue("am"));
		System.out.println(WordService.findWordIdByValue("buy"));
		System.out.println(WordService.findWordIdByValue("no"));
	}

	@Test
	public void testBookService() throws ParseException {
		List<Book> books = BookService.findBookByDetails(null, "Lewis", null, "18/10/2010", null);
		System.out.println("Found " + books.size() + " books");
		System.out.println("Title: " + books.get(0).getTitle());
	}

	@Test
	public void testWordParsing() {
		String word = "“without";
		List<WordLocation> ws = new FileParser().addWordLocationToList(new Book(""), new String[]{word});
		WordLocation w = ws.get(0);
		System.out.println((w.isQuoteBefore() ? "\"" : "") + w.getWord() + (w.isQuoteAfter() ? "\"" : "") +
				(w.getPunctuationMark() == null ? "" : w.getPunctuationMark()));
	}

	@Test
	public void testPreview() {
		List<WordLocation> wordLocations = WordService.findWordInBooks("conversations", null);

		System.out.println(WordService.buildPreview(wordLocations.get(0).getBookId(), wordLocations.get(0).getParagraph()));
	}

	@Test
	public void testWordsAppearance() {
		Map<String, Integer> map = WordService.getWordsAppearances(null);
		System.out.println(map.size());
		for (Map.Entry<String, Integer> e : map.entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
		}

		System.out.println("-----------------------------");
		Map<String, Integer> map2 = WordService.getWordsAppearances(4);
		System.out.println(map2.size());
		for (Map.Entry<String, Integer> e : map2.entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
		}
	}

	@Test
	public void testFindWordInBooks() {
		List<Book> books = BookService.getAllBooks();

		ArrayList<String> words = new ArrayList<>();
		words.add("will");
		words.add("when");
		words.add("for");

		for (String word : words) {
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
	public void testGroupService() {
		String groupName = "animals";
		Map<String, Integer> groupMap = GroupService.getAllGroupsId();
		int groupId = GroupService.createNewGroup(groupName);
		GroupService.addWordToGroup("cat", groupId);
		GroupService.addWordToGroup("dog", groupId);
		groupMap = GroupService.getAllGroupsId();
		System.out.println(GroupService.getAllWordsForGroup(groupMap.get(groupName)));
	}

	@Test
	public void testTopWordsAppearances() {
		List<Integer> books = new ArrayList<>();
		books.add(1);
		books.add(2);
		Map<String, Integer> map = WordService.getTopWordsAppearances(books, 50);
		System.out.println(map.get("a"));
	}

	@Test
	public void testPhraseService() {
		int id = PhraseService.saveNewPhrase("I suppose so");
		PhraseService.saveNewPhrase("I love you");
		PhraseService.saveNewPhrase("I love mama");
		Map<Integer, String> phrases = PhraseService.getAllPhrases();
		System.out.println(phrases.get(1));
		List<WordLocation> list = PhraseService.findPhraseInBooks(id, null);
	}

	@Test
	public void testWordByLocation() {
		System.out.println(WordService.findWordByLocation(1, 4, 1));
	}

	@Test
	public void testXmlExportImport() throws IOException {
		File folder = new File("C:\\Users\\Marina Rappoport\\Desktop\\text_books");
//		XmlSerializer.exportToXml(folder);
		DbData dbData1 = XmlSerializer.importFromXml(new File(folder, "concordanceDB.xml"));
		DbConnection.getInstance().clearDB();
		WordService.addWords(dbData1.getWords());
		BookService.insertBooks(dbData1.getBooks());
		WordService.addWordLocationList(dbData1.getWordLocations(), null);
		System.out.println("Done");
	}
}
