package unitTestingLowLevel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import library.daos.BookHelper;
import library.interfaces.entities.IBook;

public class BookHelperTest {

	//test objects
	private BookHelper helper_;
	private IBook myBook_;
	
	//example book
	private final String author_ = "Stephen King";
	private final String title_ = "The Dark Tower";
	private final String callNumber_ = "KF8840";
	private int bookId_ = 14;
	
	/* Before Testing */
	@Before
	public void setUp() throws Exception{
		//construct new helper for testing
		helper_ = new BookHelper();
		
		//construct new mock class for a book
		myBook_ = mock(IBook.class);
	}
	
	/* After Testing */
	@After
	public void reset() throws Exception{
		helper_ = null;
		myBook_ = null;
	}
	
	/* TEST MAKE BOOK */
	
	@Test
	public void testMakeBook(){
		
		//use the helper to construct a book
		//and assign it to the myBook_ object
		myBook_ = helper_.makeBook(author_, title_, callNumber_, bookId_);
		
		//check that the book is compiled correctly
		assertTrue(myBook_ instanceof IBook);
	}
}
