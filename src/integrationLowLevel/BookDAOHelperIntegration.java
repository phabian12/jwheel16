package integrationLowLevel;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import library.daos.BookHelper;
import library.daos.BookMapDAO;
import library.entities.Book;
import library.interfaces.entities.IBook;

public class BookDAOHelperIntegration {

	private IBook myBook_;
	private BookMapDAO bookMapDao_;
	private BookHelper helper_;

	private String author_;
	private String title_;
	private String callNumber_;
	private List<IBook> bookList_;

	@Before
	public void setUp() throws Exception {
		helper_ = new BookHelper();
		bookMapDao_ = new BookMapDAO(helper_);

		author_ = "Stephen King";
		title_ = "The Dark Tower";
		callNumber_ = "KF8840";
	}

	@After
	public void reset() throws Exception {
		helper_ = null;
		bookMapDao_ = null;
	}

	/* Begin Testing */

	@Test
	public void testBookMapDaoConstructor() {

		// parse helper and assign to the DAO
		bookMapDao_ = new BookMapDAO(helper_);

		// verify the instance and implementation match
		assertTrue(bookMapDao_ instanceof BookMapDAO);
	}

	/*
	 * these methods are all essentially the same however searching for
	 * different values within the Book Entity
	 */

	// finding books by author
	@Test
	public void testBookMapFindByAuthor() {
		// add book
		bookMapDao_.addBook(author_, title_, callNumber_);
		// parse book into the Book Interface
		List<IBook> bookList_ = bookMapDao_.findBooksByAuthor("Stephen King");

		// ensure that details from the search match
		assertEquals(1, bookList_.size());
	}

	// finding books by title
	@Test
	public void testBookMapDaoFindByTitle() {
		// add book
		bookMapDao_.addBook(author_, title_, callNumber_);
		// parse book into the Book Interface
		List<IBook> bookList_ = bookMapDao_.findBooksByTitle("The Dark Tower");

		// ensure that details from the search match
		assertEquals(1, bookList_.size());
	}

	// find books by author and title
	@Test
	public void testBookMapDaoFindByAuthorTitle() {
		// add book
		bookMapDao_.addBook(author_, title_, callNumber_);
		// parse book into the Book Interface
		List<IBook> bookList_ = bookMapDao_.findBooksByAuthorTitle("Stephen King", "The Dark Tower");

		// ensure that details from the search match
		assertEquals(1, bookList_.size());
	}

	// find books by ID
	@Test
	public void testBookMapDaoFindById() {

		// add book
		IBook test1 = bookMapDao_.addBook(author_, title_, callNumber_);

		// parse book into the Book Interface
		myBook_ = bookMapDao_.getBookByID(1);

		// ensure that details from the search match
		assertEquals(test1, myBook_);
	}

	// adding books
	@Test
	public void testAddBook() {
		// construct book
		myBook_ = bookMapDao_.addBook("Stephen King", "The Dark Tower", "KF8840");

		// verify contents match entity
		assertTrue(myBook_ instanceof Book);
	}

}
