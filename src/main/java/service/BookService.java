package service;

import model.Book;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class BookService {
	private final static Connection connection = DbConnection.getInstance().getConnection();

	private final static String SQL_INSERT = "INSERT INTO book (title,author,translator, release_date) " +
			"VALUES (?,?,?,?)";

	private final static String SQL_FIND_BY_DETAILS_PREFIX = "SELECT book_id, title,author,translator, " +
			"release_date FROM book WHERE ";

	public static long insertBook(Book book) {
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_INSERT,
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, book.getTitle());
			statement.setString(2, book.getAuthor());
			statement.setString(3, book.getTranslator());
			statement.setDate(4, new Date(book.getReleaseDate().getTime()));

			System.out.println(statement.toString());

			int affectedRows = statement.executeUpdate();

			if (affectedRows != 0) {
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						book.setId(generatedKeys.getLong(1));
						statement.close();
						return book.getId();
					} else {
						throw new SQLException("Creating book failed, no ID obtained.");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return -1;
	}

	public static List<Book> findBookByDetails(String title, String author, String translator, Date releaseFrom, Date releaseTo) {
		List<Book> books = new LinkedList<>();
		StringBuilder sb = new StringBuilder(SQL_FIND_BY_DETAILS_PREFIX);
		if (title != null)
			sb.append("title LIKE '%").append(title).append("%' AND ");
		if (author != null)
			sb.append("author LIKE '%").append(author).append("%' AND ");
		if (translator != null)
			sb.append("translator LIKE %").append(translator).append("%' AND ");
		if (releaseFrom != null)
			sb.append("release_date >= '").append(releaseFrom).append("' AND ");
		if (releaseFrom != null)
			sb.append("release_date <= '").append(releaseTo).append("' AND ");

		String sql = sb.toString();
		//remove last AND
		sql = sql.substring(0, sql.length() - 4);
		System.out.println(sql);
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
				books.add(parseBook(rs));
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return books;
	}

	private static Book parseBook(ResultSet rs) throws SQLException {
		long id = rs.getLong("book_id");
		String title = rs.getString("title");
		String author = rs.getString("author");
		String translator = rs.getString("translator");
		Date releaseDate = new Date(rs.getTimestamp("release_date").getTime());
		Book book = new Book(title, author, translator, releaseDate);
		book.setId(id);
		return book;
	}
}
