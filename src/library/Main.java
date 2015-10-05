package library;

import library.hardware.CardReader;
import library.hardware.Display;
import library.hardware.Printer;
import library.hardware.Scanner;

import java.util.Calendar;
import java.util.Date;

import library.daos.BookHelper;
import library.daos.BookMapDAO;
import library.daos.LoanHelper;
import library.daos.LoanMapDAO;
import library.daos.MemberHelper;
import library.daos.MemberMapDAO;
import library.interfaces.IMainListener;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.panels.MainPanel;

public class Main implements IMainListener {

	private CardReader reader_;
	private Scanner scanner_;
	private Printer printer_;
	private Display display_;
	private IBookDAO bookDAO_;
	private ILoanDAO loanDAO_;
	private IMemberDAO memberDAO_;
	
	public Main() {
		bookDAO_ = new BookMapDAO(new BookHelper());
		loanDAO_ = new LoanMapDAO(new LoanHelper());
		memberDAO_ = new MemberMapDAO(new MemberHelper());

		reader_ = new CardReader();
		scanner_ = new Scanner();
		printer_ = new Printer();
		display_ = new Display();
		
		setupTestData();
	}


	public void showGUI() {		
		reader_.setVisible(true);
		scanner_.setVisible(true);
		printer_.setVisible(true);
		display_.setVisible(true);
	}

	
	@Override
	public void borrowBooks() {
		final BorrowUC_CTL ctl = new BorrowUC_CTL(reader_, scanner_, printer_, display_, 
				 bookDAO_, loanDAO_, memberDAO_);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	ctl.initialise();
            }
        });		
	}

	
	private void setupTestData() {
        IBook[] book = new IBook[15];
		IMember[] member = new IMember[6];
		
		book[0]  = bookDAO_.addBook("author1", "title1", "callNo1");
		book[1]  = bookDAO_.addBook("author1", "title2", "callNo2");
		book[2]  = bookDAO_.addBook("author1", "title3", "callNo3");
		book[3]  = bookDAO_.addBook("author1", "title4", "callNo4");
		book[4]  = bookDAO_.addBook("author2", "title5", "callNo5");
		book[5]  = bookDAO_.addBook("author2", "title6", "callNo6");
		book[6]  = bookDAO_.addBook("author2", "title7", "callNo7");
		book[7]  = bookDAO_.addBook("author2", "title8", "callNo8");
		book[8]  = bookDAO_.addBook("author3", "title9", "callNo9");
		book[9]  = bookDAO_.addBook("author3", "title10", "callNo10");
		book[10] = bookDAO_.addBook("author4", "title11", "callNo11");
		book[11] = bookDAO_.addBook("author4", "title12", "callNo12");
		book[12] = bookDAO_.addBook("author5", "title13", "callNo13");
		book[13] = bookDAO_.addBook("author5", "title14", "callNo14");
		book[14] = bookDAO_.addBook("author5", "title15", "callNo15");
		
		member[0] = memberDAO_.addMember("fName0", "lName0", "0001", "email0");
		member[1] = memberDAO_.addMember("fName1", "lName1", "0002", "email1");
		member[2] = memberDAO_.addMember("fName2", "lName2", "0003", "email2");
		member[3] = memberDAO_.addMember("fName3", "lName3", "0004", "email3");
		member[4] = memberDAO_.addMember("fName4", "lName4", "0005", "email4");
		member[5] = memberDAO_.addMember("fName5", "lName5", "0006", "email5");
		
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
				
		//create a member with overdue loans		
		for (int i=0; i<2; i++) {
			ILoan loan = loanDAO_.createLoan(member[1], book[i]);
			loanDAO_.commitLoan(loan);
		}
		cal.setTime(now);
		cal.add(Calendar.DATE, ILoan.LOAN_PERIOD + 1);
		Date checkDate = cal.getTime();		
		loanDAO_.updateOverDueStatus(checkDate);
		
		//create a member with maxed out unpaid fines
		member[2].addFine(10.0f);
		
		//create a member with maxed out loans
		for (int i=2; i<7; i++) {
			ILoan loan = loanDAO_.createLoan(member[3], book[i]);
			loanDAO_.commitLoan(loan);
		}
		
		//a member with a fine, but not over the limit
		member[4].addFine(5.0f);
		
		//a member with a couple of loans but not over the limit
		for (int i=7; i<9; i++) {
			ILoan loan = loanDAO_.createLoan(member[5], book[i]);
			loanDAO_.commitLoan(loan);
		}
	}

	
	public static void main(String[] args) {
		
        // start the GUI
		final Main main = new Main();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	main.display_.setDisplay(new MainPanel(main), "Main Menu");
                main.showGUI();
            }
        });
	}

	
}
