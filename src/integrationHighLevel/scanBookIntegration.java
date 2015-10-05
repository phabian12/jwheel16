package integrationHighLevel;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import library.BorrowUC_CTL;
import library.BorrowUC_UI;
import library.daos.BookHelper;
import library.daos.BookMapDAO;
import library.daos.LoanHelper;
import library.daos.LoanMapDAO;
import library.daos.MemberHelper;
import library.daos.MemberMapDAO;
import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;
import library.panels.borrow.ABorrowPanel;

public class scanBookIntegration {

	// required interfaces
	ICardReader reader_;
	IScanner scanner_;
	IPrinter printer_;
	IDisplay display_;
	IBorrowUI ui_;

	// control interface
	BorrowUC_CTL control_;

	// daos
	IBookDAO bookDAO_;
	IMemberDAO memberDAO_;
	ILoanDAO loanDAO_;

	// required dates
	Date borrowDate_;
	Date dueDate_;

	// calendar
	Calendar calendar_;

	@Before
	public void setUp() throws Exception {

		this.ui_ = mock(ABorrowPanel.class);
		this.reader_ = mock(ICardReader.class);
		this.scanner_ = mock(IScanner.class);
		this.printer_ = mock(IPrinter.class);
		this.display_ = mock(IDisplay.class);

		control_ = new BorrowUC_CTL(reader_, scanner_, printer_, display_, bookDAO_, loanDAO_, memberDAO_);

		bookDAO_ = new BookMapDAO(new BookHelper());
		memberDAO_ = new MemberMapDAO(new MemberHelper());
		loanDAO_ = new LoanMapDAO(new LoanHelper());

		/* TEST DATA */
		IBook[] book_ = new IBook[15];
		IMember[] member_ = new IMember[6];

		book_[0] = bookDAO_.addBook("author1", "title1", "callNo1");
		book_[1] = bookDAO_.addBook("author1", "title2", "callNo2");
		book_[2] = bookDAO_.addBook("author1", "title3", "callNo3");
		book_[3] = bookDAO_.addBook("author1", "title4", "callNo4");
		book_[4] = bookDAO_.addBook("author2", "title5", "callNo5");
		book_[5] = bookDAO_.addBook("author2", "title6", "callNo6");
		book_[6] = bookDAO_.addBook("author2", "title7", "callNo7");
		book_[7] = bookDAO_.addBook("author2", "title8", "callNo8");
		book_[8] = bookDAO_.addBook("author3", "title9", "callNo9");
		book_[9] = bookDAO_.addBook("author3", "title10", "callNo10");
		book_[10] = bookDAO_.addBook("author4", "title11", "callNo11");
		book_[11] = bookDAO_.addBook("author4", "title12", "callNo12");
		book_[12] = bookDAO_.addBook("author5", "title13", "callNo13");
		book_[13] = bookDAO_.addBook("author5", "title14", "callNo14");
		book_[14] = bookDAO_.addBook("author5", "title15", "callNo15");

		member_[0] = memberDAO_.addMember("fName0", "lName0", "0001", "email0");
		member_[1] = memberDAO_.addMember("fName1", "lName1", "0002", "email1");
		member_[2] = memberDAO_.addMember("fName2", "lName2", "0003", "email2");
		member_[3] = memberDAO_.addMember("fName3", "lName3", "0004", "email3");
		member_[4] = memberDAO_.addMember("fName4", "lName4", "0005", "email4");
		member_[5] = memberDAO_.addMember("fName5", "lName5", "0006", "email5");

		Calendar calendar_ = Calendar.getInstance();
		Date dateNow = calendar_.getTime();

		// assigning the members different kinds of loans and scenarios,

		// assign member1 overdue loans from testData above
		for (int x = 0; x < 2; x++) {
			ILoan testLoan_ = loanDAO_.createLoan(member_[1], book_[x]);
			loanDAO_.commitLoan(testLoan_);
		}

		// update status of above loan with the For Loop
		calendar_.setTime(dateNow);
		calendar_.add(Calendar.DATE, ILoan.LOAN_PERIOD + 1);
		Date checkDate_ = calendar_.getTime();
		loanDAO_.updateOverDueStatus(checkDate_);

		// assign member2 in testData with maximum unpaid fines
		member_[2].addFine(10.0f);

		// assign member3 with maxed out loans
		for (int y = 4; y < 9; y++) {
			ILoan testLoan_ = loanDAO_.createLoan(member_[3], book_[y]);
			loanDAO_.commitLoan(testLoan_);
		}

		// assign member 4 a medium fine
		member_[4].addFine(5.0f);

		// assign member 5 some loans
		for (int x = 7; x < 9; x++) {
			ILoan loan = loanDAO_.createLoan(member_[5], book_[x]);
			loanDAO_.commitLoan(loan);
		}
	}

	@After
	public void reset() throws Exception {
		this.ui_ = null;
		this.reader_ = null;
		this.scanner_ = null;
		this.printer_ = null;
		this.display_ = null;
		this.control_ = null;
	}

	// the data is prepared for testing but my testing is limited
	// struggling to progress past this sort of testing...
	// keep getting a lot of errors which I'm baffled at the cause of ...
	// I'll just leave it like this I think
	@Test
	public void testSwipe() {
		control_.initialise();
		control_.cardSwiped(1);
		control_.bookScanned(10);
	}

}
