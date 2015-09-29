package library.daos;

import java.util.ArrayList;
import java.util.List;

import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

public class BookDAO implements IBookDAO{

	private IBookHelper helper_;
	private List<IBook> bookList_;
	private int bookCounter_;
	
	//constructor
	public BookDAO(IBookHelper helper){
		//throws exception if helper is empty
		if(helper == null){
			throw new IllegalArgumentException("Helper Object cannot be null.");
		}
		else{
			helper_ = helper;
			bookList_ = new ArrayList<>();
			bookCounter_ = 1;
		}
	}
	
	
	//adding a new book to collection
	public IBook addBook(String author, String title, String callNo) {
		IBook newBook = helper_.makeBook(author, title, callNo, bookCounter_++);
		bookList_.add(newBook);
		
		return newBook;
	}
	
	
	//return book by it's unique ID from collection
	public IBook getBookByID(int bookID) {
		for (IBook newBook : bookList_)
			if(newBook.getID() == bookID){
				return newBook;
			}
		return null;
	}
	
	
	//return the list of books from collection
	public List<IBook> listBooks() {
		return bookList_;
	}
	
	
	//returns a list of books written by an author
	public List<IBook> findBooksByAuthor(String author) {
		List<IBook> returnedBooks = new ArrayList<>();
		for (IBook books : bookList_)
			if(author.compareTo(books.getAuthor()) == 0){
				returnedBooks.add(books);
			}
		//return empty list if no books found
		return returnedBooks;
	}
	
	
	//return list of titles by title
	public List<IBook> findBooksByTitle(String title) {
		List<IBook> returnedTitles = new ArrayList<IBook>();
		for (IBook books : bookList_){
			if (title.compareTo(books.getTitle()) == 0)
				returnedTitles.add(books);
		}
		//returns empty list if no books found
		return returnedTitles;
	}
	
	
	//return books with same author/title
	public List<IBook> findBooksByAuthorTitle(String author, String title) {
		List<IBook> results = new ArrayList<>();
		for (IBook books : bookList_)
			if (title.compareTo(books.getTitle()) == 0
				&& author.compareTo(books.getAuthor()) == 0)
					results.add(books);
		//return empty list if no books found
		return results;
	}
}
