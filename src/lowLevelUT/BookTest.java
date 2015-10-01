package lowLevelUT;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import library.entities.Book;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.ILoan;


public class BookTest{
	
	//test objects
	private static Book myBook_;
	private ILoan mockLoan_;
	
	
	//example book
	//Book exBook_ = new Book("Stephen King", "The Dark Tower", "KF8840", 1);
	
	private final String author_ = "Stephen King";
	private final String title_ = "The Dark Tower";
	private final String callNumber_ = "KF8840";
	private int bookId_ = 1;
	
	@Before
	public void setUp() throws Exception{
		//new book
		myBook_ = new Book(author_, title_, callNumber_, bookId_);
		//new mock loan
		mockLoan_ = mock(ILoan.class);
	}
	
	
	@After
	public void reset() throws Exception{
		myBook_ = null;
	}
	
	
	//constructor testing
	
	//all parameters invalid
	@Test (expected = IllegalArgumentException.class)
	public void testBookConstructorAllInvalid(){
		Book test = new Book(null, null, null, -1);
	}
	
	//author parameter invalid
	@Test (expected = IllegalArgumentException.class)
	public void testBookConstructorAuthorInvalid(){
		Book test = new Book(null, title_, callNumber_, bookId_);
	}
	
	//title parameter invalid
	@Test (expected = IllegalArgumentException.class)
	public void testBookConstructorTitleInvalid(){
		Book test = new Book(author_, null, callNumber_, bookId_);
	}
	
	//call number parameter invalid
	@Test (expected = IllegalArgumentException.class)
	public void testBookConstructorCallNoInvalid(){
		Book test = new Book(author_, title_, null, bookId_);
	}
	
	//book id parameter invalid
	@Test (expected = IllegalArgumentException.class)
	public void testBookConstructorBookIdNoInvalid(){
		Book test = new Book(author_, title_, callNumber_, -99);
	}
	

	@Test
	public void testBorrow(){
		//pass mock loan
		//borrow myBook
		myBook_.borrow(mockLoan_);
		
		//check the state of the Book has changed to ON_LOAN
		assertEquals("Book State is not ON_LOAN.", EBookState.ON_LOAN, myBook_.getState());
	}
	
	
	@Test
	public void testBorrowThrowsRuntimeException(){
		
		//ensuring a book can't be borrowed with a Null Loan
		ILoan testLoan = null;
		
		//set state of book to AVAILABLE
		myBook_.setState(EBookState.AVAILABLE);
		try{
			myBook_.borrow(testLoan);
			fail("ERR: Missing Runtime Exception");
		}
		catch(RuntimeException e){
			assertTrue(true);
		}	
	}
	
	@Test
	public void testReturnBookFalse(){
		
		//change state to ON_LOAN
		myBook_.borrow(mockLoan_);
		//returning a book (without damage)
		myBook_.returnBook(false);
		assertEquals("State should be changed to AVAILABLE", EBookState.AVAILABLE, myBook_.getState());
		
		
	}
	
	@Test
	public void testReturnBookTrue(){
		
		//change state to ON_LOAN
		myBook_.borrow(mockLoan_);
		//returning a damaged book
		myBook_.returnBook(true);
		assertEquals("State should be changed to DAMAGED", EBookState.DAMAGED, myBook_.getState());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}