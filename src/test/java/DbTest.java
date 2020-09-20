import model.Book;
import org.junit.jupiter.api.Test;
import service.BookService;
import service.WordService;

import java.util.Date;
import java.util.List;

public class DbTest {

	@Test
	public void testWordService(){
		System.out.println(WordService.insertWord("am"));
		System.out.println(WordService.insertWord("but"));
		System.out.println(WordService.insertWord("buy"));
		System.out.println(WordService.insertWord("am"));

		System.out.println(WordService.findWordByValue("am"));
		System.out.println(WordService.findWordByValue("buy"));
		System.out.println(WordService.findWordByValue("no"));
	}

	public void testBookService(){
		Book book = new Book("Book2", "Author2", null, new Date());
		BookService.insertBook(book);

		List<Book> books = BookService.findBookByDetails("Book", null, null, null, null);
		System.out.println("Found " + books.size() + " books");
	}
}
