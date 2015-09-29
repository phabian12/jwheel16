package library.daos;

import java.util.List;

import library.interfaces.daos.IBookDAO;
import library.interfaces.entities.IBook;

public class BookDAO implements IBookDAO{

	public IBook addBook(String author, String title, String callNo) {
		return null;
	}
	public IBook getBookByID(int id) {
		return null;
	}
	public List<IBook> listBooks() {
		return null;
	}
	public List<IBook> findBooksByAuthor(String author) {
		return null;
	}
	public List<IBook> findBooksByTitle(String title) {
		return null;
	}
	public List<IBook> findBooksByAuthorTitle(String author, String title) {
		return null;
	}
}
