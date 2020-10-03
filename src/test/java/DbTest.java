import model.Book;
import model.WordLocation;
import org.junit.jupiter.api.Test;
import service.BookService;
import service.DbConnection;
import service.FileParser;
import service.WordService;

import java.util.*;

public class DbTest {

	@Test
	public void wordLoadTest() {
		String bookPath = "D:\\uni\\sql_seminar\\text_books\\59774-0.txt";
		FileParser parser = new FileParser();
		List<WordLocation> wordLocationList = parser.parseFile(new Book(bookPath));
		Date start = new Date();
//		Map<String, Long> words = WordService.getAllWordsId();
		List<WordLocation> wordLocationListCopy = new LinkedList<>();
		for (WordLocation wordLocation : wordLocationList) {
			if(!wordLocation.getWord().isEmpty())
//			Long wordId = words.get(wordLocation.getWord());
//			if (wordId == null)
//				wordId = WordService.insertWord(wordLocation.getWord());
//			if (wordId > 0) {
//				wordLocation.setWordId(wordId);
				wordLocationListCopy.add(wordLocation);
//			}
		}
		WordService.addWordLocationList(wordLocationListCopy, 1);
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


}
