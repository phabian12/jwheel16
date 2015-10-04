package integrationLowLevel;

import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

import java.util.Date;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import library.entities.Book;
import library.entities.Member;
import library.entities.Loan;

public class BookEntityToEntityIntegration {

	// entities for testing
	private IBook myBook_;
	private IMember myMember_;
	private ILoan myLoan_;

	// dates required for testing
	Date borrowDate_;
	Date dueDate_;

	// calendar
	Calendar calendar_;

	// example book
	private final String author_ = "Stephen King";
	private final String title_ = "The Dark Tower";
	private final String callNumber_ = "KF8840";
	private int bookID_ = 1;

	@Before
	public void setUp() {

		//initialize borrow date
		borrowDate_ = new Date();

		//declare calendar values
		calendar_ = Calendar.getInstance();
		calendar_.setTime(borrowDate_);
		calendar_.add(Calendar.DATE, ILoan.LOAN_PERIOD);

		//assign respective calendar time into the dueDate variable
		dueDate_ = calendar_.getTime();

		// construct new test entity objects
		myBook_ = new Book(author_, title_, callNumber_, bookID_);
		myMember_ = new Member("firstName_", "lastName_", "contactPhone_", "email_", 1);
		myLoan_ = new Loan(myBook_, myMember_, borrowDate_, dueDate_);
	}
	
	@After
	public void reset(){
		//reset entities to null after testing is complete
		myMember_ = null;
		myBook_ = null;
		myLoan_ = null;
	}

	
	/* Get Loan Testing */
	
	@Test
	public void testGetLoan(){
		//borrow test book, passing loan entity
		myBook_.borrow(myLoan_);
		
		//ensure both parsed objects match each other
		assertTrue(myLoan_ == myBook_.getLoan());
	}
	
	@Test
	public void testGetLoanUnavailable(){
		//check state of loan
		myLoan_ = myBook_.getLoan();
		
		//ensure null is returned
		//if book wasn't on loan
		assertTrue(myLoan_ == null);
	}
	
	
	/* Borrow Book Testing */
	
	@Test
	public void testBorrow(){
		//borrow book passing loan
		myBook_.borrow(myLoan_);
		
		//check the state of the book is changed to ON_LOAN
		assertEquals(EBookState.ON_LOAN, myBook_.getState());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testBorrowNullLoan(){
		//declare loan as a null value
		myLoan_ = null;
		
		//attempt to borrow book with null loan
		//will throw exception
		myBook_.borrow(myLoan_);
	}
	
	/* End Testing */
}
