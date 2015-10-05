package integrationHighLevel;


import static org.mockito.Mockito.*;

import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import library.BorrowUC_CTL;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;
import library.panels.borrow.ABorrowPanel;

public class swipeCardIntegration {
	
	
	//daos
	private IBookDAO bookDAO_;
	private IMemberDAO memberDAO_;
	private ILoanDAO loanDAO_;
	
	//entity
	private IMember testBorrower_;
	private int testMemberId_;
	private ILoan testLoan_;
	
	//interfaces
	private ICardReader reader_;
	private IDisplay display_;
	private IPrinter printer_;
	private IScanner scanner_;
	
	//ui and control class
	private ABorrowPanel ui_;
	private BorrowUC_CTL control_;
	
	@Before
	public void setUp() throws Exception {
		
		bookDAO_ = mock(IBookDAO.class);
		memberDAO_ = mock(IMemberDAO.class);
		loanDAO_ = mock(ILoanDAO.class);
		
		testBorrower_ = mock(IMember.class);
		testMemberId_ = 1;
		testLoan_ = mock(ILoan.class);
		
		reader_ = mock(ICardReader.class);
		display_ = mock(IDisplay.class);
		printer_ = mock(IPrinter.class);
		scanner_ = mock(IScanner.class);
		
		ui_ = mock(ABorrowPanel.class);
		control_ = new BorrowUC_CTL(reader_, scanner_, printer_, display_, bookDAO_, loanDAO_, memberDAO_);
	}
	
	
	@After
	public void reset() throws Exception{
		bookDAO_ = null;
		memberDAO_ = null;
		loanDAO_ = null;
		reader_ = null;
		display_ = null;
		printer_ = null;
		scanner_ = null;
		ui_ = null;
		control_ = null;
	}
	
	@Test 
	public void testSuccessfulCardSwipe(){
		
		//when the memberDAO object receives the member's ID
		when(memberDAO_.getMemberByID(testMemberId_)).thenReturn(testBorrower_);
		
		//if the member does not have a good standing with their account
		//they are declined borrowing priviledges 
		when(testBorrower_.hasOverDueLoans() && testBorrower_.hasReachedLoanLimit()
				&& testBorrower_.hasFinesPayable() 
				&& testBorrower_.hasReachedFineLimit()).thenReturn(false);
		
		//when the member's details are searched
		//return their respective information
		when(testBorrower_.getID()).thenReturn(testMemberId_);
		when(testBorrower_.getFirstName()).thenReturn("");
		when(testBorrower_.getLastName()).thenReturn("");
		when(testBorrower_.getContactPhone()).thenReturn("");
	}
	
	//this is where I was stumped, everything I tried I kept getting errors
	//and just couldn't seem to progress beyond simple testing
	
	@Test
	public void sampleTest(){
	
		//through trial and error member1 and book 10 will always work 
		
		
	}

}
