package integrationHighLevel;

import java.util.Calendar;
import java.util.Date;

import library.BorrowUC_CTL;
import library.interfaces.IBorrowUI;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;

public class scanBookIntegration {

	ICardReader reader_;
	IScanner scanner_;
	IPrinter printer_;
	IDisplay display_;
	IBorrowUI ui_;

	BorrowUC_CTL control_;

	IBookDAO bookDAO_;
	IMemberDAO memberDAO_;
	ILoanDAO loanDAO_;

	Date borrowDate_;
	Date dueDate_;
	
	Calendar calendar_;
}
