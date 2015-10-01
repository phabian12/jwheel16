package library.daos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

public class BookDAO implements IBookDAO{

	private int nextId_;
	private Map<Integer, IBook> bookMap_;
	private IBookHelper helper_;
	
	//constructor for helper
	public BookDAO(IBookHelper helper) {
		if (helper == null ) {
			throw new IllegalArgumentException("constructor: helper invalid.");
		}
		else{
			nextId_ = 1;
			bookMap_ = new HashMap<Integer, IBook>();
			this.helper_ = helper;
		}
	}
	
	//constructor for bookmap
	public BookDAO(IBookHelper helper, Map<Integer, IBook> bookMap){
		this(helper);
		if (helper == null){
			throw new IllegalArgumentException("constructor: bookMap invalid.");
		}
		else{
			this.bookMap_ = bookMap;
		}
	}
	
	public IBook addBook(String author, String title, String callNo) {
						//call on the getNextId function
		int bookId = getNextId();
		IBook book = helper_.makeBook(author, title, callNo, bookId);
		bookMap_.put(Integer.valueOf(bookId), book);
		return book;
	}

	
	
	public IBook getBookByID(int id) {
		if (bookMap_.containsKey(Integer.valueOf(id))) {
			return bookMap_.get(Integer.valueOf(id));
		}
		else{
			return null;
		}
	}

	
	
	public List<IBook> listBooks() {
		List<IBook> bookList = new ArrayList<IBook>(bookMap_.values());
		return Collections.unmodifiableList(bookList);
	}

	
	
	public List<IBook> findBooksByAuthor(String author) {
		
		//array list for authors and respective books
		List<IBook> authorResults = new ArrayList<IBook>();
		List<IBook> books = new ArrayList<IBook>(bookMap_.values());
	
		//for loop for finding the corresponding books with authors
		for(int i=0; i< books.size();i++){
			if(books.get(i).getAuthor().equals(author))
				authorResults.add(books.get(i));
		}
		return authorResults;
	}

	
	
	public List<IBook> findBooksByTitle(String title) {
		List<IBook> bookResults = new ArrayList<IBook>();
		List<IBook> books = new ArrayList<IBook>(bookMap_.values());

		//for loop for finding the books in relation to their title
		for(int i=0; i<books.size();i++){
			if(books.get(i).getTitle().equals(title))
				bookResults.add(books.get(i));
		}
		return bookResults;
	}

	
	
	public List<IBook> findBooksByAuthorTitle(String author, String title) {
		
		List<IBook> matchingResults = new ArrayList<IBook>();
		List<IBook> books = new ArrayList<IBook>(bookMap_.values());
		
		for(int i=0; i<books.size();i++){
			if(books.get(i).getAuthor().equals(author) 
					&& books.get(i).getTitle().equals(title))
					matchingResults.add(books.get(i));
		}
		return matchingResults;
	}
	
	
	
	private int getNextId() {
		return nextId_++;
	}
}
