package library;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.IBorrowUIListener;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.ICardReaderListener;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;
import library.interfaces.hardware.IScannerListener;

public class BorrowUC_CTL implements ICardReaderListener, IScannerListener, IBorrowUIListener {

	/* BASED ON JIM TULIP'S REFERENCE IMPLEMENTATION */
	
	private ICardReader reader_;
	private IScanner scanner_;
	private IPrinter printer_;
	private IDisplay display_;
	
	// private String state;
	private int scanCount_ = 0;
	private IBorrowUI ui_;
	private EBorrowState state_;
	private IBookDAO bookDAO_;
	private IMemberDAO memberDAO_;
	private ILoanDAO loanDAO_;

	private List<IBook> bookList_;
	private List<ILoan> loanList_;
	private IMember borrower_;

	private JPanel previous_;

	public BorrowUC_CTL(ICardReader reader, IScanner scanner, IPrinter printer, 
						IDisplay display, IBookDAO bookDAO, ILoanDAO loanDAO, 
						IMemberDAO memberDAO){

		this.reader_ = reader;
		reader_.addListener(this);
		this.scanner_ = scanner;
		scanner_.addListener(this);
		this.printer_ = printer;
		this.display_ = display;
		
		this.bookDAO_ = bookDAO;
		this.loanDAO_ = loanDAO;
		this.memberDAO_ = memberDAO;
		
		bookList_ = new ArrayList<IBook>();
		loanList_ = new ArrayList<ILoan>();
		
		this.ui_ = new BorrowUC_UI(this);
		state_ = EBorrowState.CREATED;
	}

	//Before
	public void initialise() {
		previous_ = display_.getDisplay();
		display_.setDisplay((JPanel) ui_, "Borrow UI");
		reader_.setEnabled(true);
		scanner_.setEnabled(false);
		setState(EBorrowState.INITIALIZED);
	}

	//After
	public void close() {
		display_.setDisplay(previous_, "Main Menu");
	}

	public EBorrowState getState(){
		return state_;
	}
	
	
	//
	@Override
	public void cardSwiped(int memberID) {
		//ensure state is initialized
		if(!state_.equals(EBorrowState.INITIALIZED)){
			//throw exception if isn't correctly initialized
			throw new RuntimeException("BorrowUC_CTL: cardSwiped: Incorrect State" + state_);
		}
		
		borrower_ = memberDAO_.getMemberByID(memberID);
		if (borrower_ == null){
			ui_.displayErrorMessage("memberID not found");
			return;
		}
		
		//cases
		boolean hasOverdueLoan = borrower_.hasOverDueLoans();
		boolean hasReachedLoanLimit = borrower_.hasReachedLoanLimit();
		boolean hasFines = borrower_.hasFinesPayable();
		boolean isOverFineLimit = borrower_.hasReachedFineLimit();
		
		//prerequisites for BORROWING_RESTRICTED state
		boolean borrowingRestricted = (hasOverdueLoan || hasReachedLoanLimit || hasFines);
		
		if (borrowingRestricted){
			setState(EBorrowState.BORROWING_RESTRICTED);
		}
		else{
			setState(EBorrowState.SCANNING_BOOKS);
		}
		
		//call the displayMemberDetails function
		displayMemberDetails();
		
		//if member has overdue loans
		if(hasOverdueLoan){
			ui_.displayOverDueMessage();
		}
		
		//if member has reached their loan limit
		if(hasReachedLoanLimit){
			ui_.displayAtLoanLimitMessage();
		}
		
		//if the member has fines
		if(hasFines){
			float owedAmount = borrower_.getFineAmount();
			ui_.displayOutstandingFineMessage(owedAmount);
		}
		
		//if the member is over their fine limit
		if(isOverFineLimit){
			float owedAmount = borrower_.getFineAmount();
			ui_.displayOverFineLimitMessage(owedAmount);			
		}
	
		//display the existing loans of the borrower
		String existingLoans_ = buildLoanListDisplay(borrower_.getLoans());
		ui_.displayExistingLoan(existingLoans_);
		
		
	}
	
	//function for compiling and displaying member details
	private void displayMemberDetails(){
		//retrieve values and assign to variables
		int borrowerId = borrower_.getID();
		String borrowerName = borrower_.getFirstName() + " " + borrower_.getLastName();
		String borrowerPhoneNo = borrower_.getContactPhone();
		
		//display details
		ui_.displayMemberDetails(borrowerId, borrowerName, borrowerPhoneNo);
	}

	@Override
	public void bookScanned(int barcode) {
		//ensure program is in correct state
		if (state_ != EBorrowState.SCANNING_BOOKS){
			throw new RuntimeException("BorrowUC_CTL: bookScanner: Illegal Action Within State");
		}
		
		//declare book, retrieving book by barcode
		IBook testBook_ = bookDAO_.getBookByID(barcode);
		
		//ensure book can't be null
		if(testBook_ == null){
			ui_.displayErrorMessage("Book cannot be found.");
			return;
		}
		
		//instance where book is unavailable
		if(testBook_.getState() != EBookState.AVAILABLE){
			ui_.displayErrorMessage("Book: " + testBook_.getID() + "is unavailable." + testBook_.getState());
			return;
		}
		
		//instance where book has already been scanned
		if(bookList_.contains(testBook_)){
			ui_.displayErrorMessage("Book: " + testBook_.getID() + "has already been scanned.");
			return;
		}
		
		//increment scanCounter
		scanCount_++;
		
		//add book to list of Books
		bookList_.add(testBook_);
		
		//create new loan
		ILoan myLoan_ = loanDAO_.createLoan(borrower_, testBook_);
		//add to loan list
		loanList_.add(myLoan_);
		
		//display respective books and related loans
		ui_.displayScannedBookDetails(testBook_.toString());
		ui_.displayPendingLoan(buildLoanListDisplay(loanList_));
		
		//instance where the user has reached their loan limit
		if (scanCount_ >= IMember.LOAN_LIMIT) {
	          setState(EBorrowState.CONFIRMING_LOANS);
	          reader_.setEnabled(false);
	          scanner_.setEnabled(false);
	          ui_.displayConfirmingLoan(buildLoanListDisplay(loanList_));
	      }
	}
	
	public void setState(EBorrowState state) {
		System.out.println("Setting state: " + state);
		
		this.state_ = state;
		ui_.setState(state);

		switch (state) {
		
		case INITIALIZED:
			reader_.setEnabled(true);
			scanner_.setEnabled(false);
			break;
			
		case SCANNING_BOOKS:
			reader_.setEnabled(false);
			scanner_.setEnabled(true);
			this.bookList_ = new ArrayList<IBook>();
			this.loanList_ = new ArrayList<ILoan>();
			scanCount_ = borrower_.getLoans().size();
			
			//clear currentBook display
			ui_.displayScannedBookDetails("");			
			//clear pending loan display
			ui_.displayPendingLoan("");			
			break;
			
		case CONFIRMING_LOANS:
			reader_.setEnabled(false);
			scanner_.setEnabled(false);
			//display pending loans
			ui_.displayConfirmingLoan(buildLoanListDisplay(loanList_));
			break;
			
		case COMPLETED:
			reader_.setEnabled(false);
			scanner_.setEnabled(false);
			for (ILoan loan : loanList_) {
				loanDAO_.commitLoan(loan);
			}
			printer_.print(buildLoanListDisplay(loanList_));
			close();
			break;
			
		case CANCELLED:
			reader_.setEnabled(false);
			scanner_.setEnabled(false);
			close();
			break;
			
		case BORROWING_RESTRICTED:
			reader_.setEnabled(false);
			scanner_.setEnabled(false);
			ui_.displayErrorMessage(String.format("Member %d cannot borrow at this time.", borrower_.getID()));
			break;
			
		default:
			throw new RuntimeException("Unknown state");
		}
	}

	@Override
	public void cancelled() {
		setState(EBorrowState.CANCELLED);
	}

	@Override
	public void scansCompleted() {
		setState(EBorrowState.CONFIRMING_LOANS);		
	}

	@Override
	public void loansConfirmed() {
		setState(EBorrowState.COMPLETED);				
	}

	@Override
	public void loansRejected() {
		System.out.println("Loans Rejected");
		setState(EBorrowState.SCANNING_BOOKS);		
	}

	private String buildLoanListDisplay(List<ILoan> loans) {
		StringBuilder bld = new StringBuilder();
		for (ILoan ln : loans) {
			if (bld.length() > 0) bld.append("\n\n");
			bld.append(ln.toString());
		}
		return bld.toString();		
	}

}
