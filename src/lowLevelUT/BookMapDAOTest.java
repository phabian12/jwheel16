package lowLevelUT;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import library.daos.BookHelper;
import library.daos.BookMapDAO;
import library.interfaces.entities.IBook;

public class BookMapDAOTest {
	
	private BookHelper helper_;
	private BookMapDAO bookMapDAO_;
	private int bookListSize_;
	
	/* Before Testing */
	@Before
	public void setUp() throws Exception{

		//initialize
		helper_ = new BookHelper();
		bookMapDAO_ = new BookMapDAO(helper_);
		
		//example books for testing
		bookMapDAO_.addBook("Stephen King", "The Dark Tower", "SK0688");
					/* added two books with different callNumbers to ensure the tests 
					 * can handle multiple copies of the same book */
		bookMapDAO_.addBook("J.K. Rowling", "Harry Potter", "JW9901");
		bookMapDAO_.addBook("J.K. Rowling", "Harry Potter", "RK0712");
		bookMapDAO_.addBook("Charles Dickens", "A Christmas Carol", "CD0420");
		
		//assign the size of the list to bookListSize for later use
		bookListSize_ = bookMapDAO_.listBooks().size();
	}
	
	
	
	/* After Testing */
	@After
	public void reset(){
		helper_ = null;
		bookMapDAO_ = null;
	}
	
	
	
	/* Preliminary Testing */
	@Test
	public void testBookMapDAOHelperNotNull(){
		//ensuring the BookHelper is not null
		new BookMapDAO(helper_);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testBookMapDAOHelperIsNull(){
		//testing when the helper is null
		BookHelper testHelper_ = null;
		new BookMapDAO(testHelper_);
	}
	
	/* AddBook Test */
	@Test
	public void testAddBook(){
		//new test book
		bookMapDAO_.addBook("Anthony Horowitz", "Stormbreaker", "AH0594");
		
		//verify the new book has been added to the list of books
		assert (bookMapDAO_.listBooks().size() == bookListSize_ + 1);
	}
	
	/* Finding Books Testing */
	
	@Test
	public void testBooksByAuthor(){
		
		/* testing the findBooksByAuthor functionality
		 * applying it to all example authors
		 * the number following the string denotes the amount of 
		 * available copies of that book */
		assertEquals(bookMapDAO_.findBooksByAuthor("Stephen King").size(), 1);
		assertEquals(bookMapDAO_.findBooksByAuthor("J.K. Rowling").size(), 2);
		assertEquals(bookMapDAO_.findBooksByAuthor("Charles Dickens").size(), 1);
	}
	
	@Test
	public void testBooksByTitle(){
		
		//testing the functionality of findBooksByTitle
		assertEquals(bookMapDAO_.findBooksByTitle("The Dark Tower").size(), 1);
		assertEquals(bookMapDAO_.findBooksByTitle("Harry Potter").size(), 2);
		assertEquals(bookMapDAO_.findBooksByTitle("A Christmas Carol").size(), 1);
	}
	
	@Test
	public void testBooksById(){
		
		//new test book, assign second(2) example book with 2 Id
		IBook testBook_ = bookMapDAO_.getBookByID(2);
		
		//verify that the second book matches the second author
		assertEquals(testBook_.getAuthor(), "J.K. Rowling");
	}
	
	@Test
	public void testBooksByAuthorTitle(){
		
		//testing the functionality of findBooksByAuthorTitle
		assertEquals(bookMapDAO_.findBooksByAuthorTitle("Stephen King", "The Dark Tower").size(), 1);
		assertEquals(bookMapDAO_.findBooksByAuthorTitle("J.K. Rowling", "Harry Potter").size(), 2);
		assertEquals(bookMapDAO_.findBooksByAuthorTitle("Charles Dickens", "A Christmas Carol").size(), 1);
	}
}
