package unitTestingLowLevel;

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
	private final String author_ = "Stephen King";
	private final String title_ = "The Dark Tower";
	private final String callNumber_ = "KF8840";
	private int bookId_ = 14;
	
	@Before
	public void setUp() throws Exception{
		//new book
		//constructs new book object for testing
		//with above values
		myBook_ = new Book(author_, title_, callNumber_, bookId_);
		
		//mock an implementation of ILoan
		mockLoan_ = mock(ILoan.class);
	}
	
	@After
	public void reset() throws Exception{
		myBook_ = null;
	}
	
	
	
	/* CONSTRUCTOR TESTING */
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
	

	
	/* BORROW TESTING */
	@Test
	public void testBorrow(){
		//borrow myBook with mockLoan
		myBook_.borrow(mockLoan_);
		
		//check the state of the Book has changed to ON_LOAN
		assertEquals("Book State is not ON_LOAN.", EBookState.ON_LOAN, myBook_.getState());
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testBorrowParameter(){
		//ensure that the book can't be borrowed
		//with an illegal or null Loan
		myBook_.borrow(null);
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
	
	@Test (expected = RuntimeException.class)
	public void testBorrowDuplicateLoan(){
		//attempting to borrow a book which is already on loan
		//should throw a Runtime Error.
		
		myBook_.borrow(mockLoan_);
		
		myBook_.borrow(mockLoan_);
	}
	
	
	
	/* GET LOAN TESTING */
	@Test
	public void testGetLoan(){
		//borrow the book
		myBook_.borrow(mockLoan_);
		
		//checks the Loan matches the Mock Loan
		assertTrue(myBook_.getLoan() == mockLoan_);
	}
	
	@Test
	public void testGetLoanAvaiableBook(){
		//attempt to get the Loan Status of a book which 
		//has not been borrowed
		assertTrue(myBook_.getLoan() == null);
	}
	
	
	
	/* RETURN BOOK TESTING */
	@Test(expected=RuntimeException.class)
	public void testReturnNonBorrowedBook(){
	    
		//returning a book which was never ON_LOAN
		//will throw Runtime Error because default state is AVAILABLE
		myBook_.returnBook(false);
	}
	
	@Test
	public void testReturnNonDamagedBook(){
		//change state to ON_LOAN
		myBook_.borrow(mockLoan_);
		
		//returning a book (without damage)
		myBook_.returnBook(false);
		
		//ensure state is changed to AVAILABLE
		assertEquals("State should be changed to AVAILABLE.", EBookState.AVAILABLE, myBook_.getState());
	}
	
	@Test
	public void testReturnDamagedBook(){
		//borrow book
		myBook_.borrow(mockLoan_);
		
		//returning a damaged book
		myBook_.returnBook(true);
		
		//ensure state is changed to DAMAGED
		assertEquals("State should be changed to DAMAGED.", EBookState.DAMAGED, myBook_.getState());
	}
	

	
	/* TEST LOST BOOK */
	@Test(expected=RuntimeException.class)
	public void testAvailableBookLose(){
	    //losing a book without it's state being ON_LOAN
		//attempts to lose a book with it being available..
		//throws Runtime Error
		myBook_.lose();
	}
	
	@Test
	public void testLose(){
		//borrow book with mockLoan
		myBook_.borrow(mockLoan_);
		
		//change state of book to LOST
		myBook_.lose();
		
		//check that the state of the Book is LOST
		assertEquals("State should be changed to LOST.", EBookState.LOST, myBook_.getState());
	}
	
	
	
	/* TEST REPAIR OF BOOK */
	@Test (expected = RuntimeException.class)
	public void testRepairNonDamagedBook(){
		//attempting to repair a book which is not damaged
		myBook_.repair();
	}
	
	
	@Test
	public void testRepair(){
		//borrow book
		myBook_.borrow(mockLoan_);
		
		//return damaged book
		myBook_.returnBook(true);
		
		//repair the damaged book
		myBook_.repair();
		
		//ensure the book's state is changed 
		//back to AVAILABLE after the repair
		assertEquals("State should be changed to AVAILABLE.", EBookState.AVAILABLE, myBook_.getState());
	}
	
	
	
	/* TEST DISPOSAL OF BOOK */
	@Test
	public void testDispose(){
		//dispose of book
		myBook_.dispose();
		
		//change the book's state to DISPOSED
		assertEquals("The books state is changed to DISPOSED.",EBookState.DISPOSED, myBook_.getState());
	}
	
	@Test (expected = RuntimeException.class)
	public void testDisposeDuplicate(){
		//attempting to dispose of a book that's
		//already been disposed
		myBook_.dispose();
		assertEquals(EBookState.DISPOSED, myBook_.getState());
		myBook_.dispose();
	}
	
	
	
	/* GET AUTHOR */
	@Test
	public void testGetAuthor(){
		//simple test, manually setting the Author's Name
		//and getting the respective author
		assertEquals("Stephen King", myBook_.getAuthor());
	}
	
	
	
	/* GET TITLE */
	@Test
	public void testGetTitle(){
		//simple test, manually setting the Title
		//and getting the respective title
		assertEquals("The Dark Tower", myBook_.getTitle());
	}
	
	
	
	/* GET CALL NUMBER */
	@Test
	public void testGetCallNumber(){
		//simple test, manually setting the Call Number
		//and getting the respective Call Number
		assertEquals("KF8840", myBook_.getCallNumber());
	}
	
	
	
	/* GET BOOK ID */
	@Test
	public void testGetBookId(){
		//simple test, manually setting the BookID
		//and getting the respective BookID
		assertEquals(14, myBook_.getID());
	}
	
	
	
	/* GET STATE TESTING*/
	@Test 
	public void testGetStateAvailable(){
		//testing the AVAILABLE state
		assertEquals(EBookState.AVAILABLE, myBook_.getState());
	}
	
	@Test
	public void testGetStateDamaged(){
		//borrow book
		myBook_.borrow(mockLoan_);
		
		//return damaged book
		myBook_.returnBook(true);
		
		//testing the DAMAGED State
		assertEquals(EBookState.DAMAGED, myBook_.getState());
	}
	
	@Test
	public void testGetStateDisposed(){
		//testing the DISPOSED state
		myBook_.dispose();
		assertEquals(EBookState.DISPOSED, myBook_.getState());
	}
	
	@Test
	public void testGetStateLost(){
		//borrow book
		myBook_.borrow(mockLoan_);
				
		//return damaged book
		myBook_.lose();
				
		//testing the DAMAGED State
		assertEquals(EBookState.LOST, myBook_.getState());
	}
	
	/* END TESTING */
}