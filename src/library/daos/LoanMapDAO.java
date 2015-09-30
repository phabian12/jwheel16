package library.daos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.ILoanHelper;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

public class LoanMapDAO implements ILoanDAO {

	private int nextID_;
	private Map<Integer, ILoan> loanMap_;
	private ILoanHelper helper_;
	private Calendar calendar_;
	

	public LoanMapDAO(ILoanHelper helper) {
		if (helper == null ) {
			throw new IllegalArgumentException(
				String.format("LoanMapDAO : constructor : helper cannot be null."));
		}
		nextID_ = 0;
		this.helper_ = helper; 
		loanMap_ = new HashMap<Integer, ILoan>();
		calendar_ = Calendar.getInstance();
	}

	public LoanMapDAO(ILoanHelper helper, Map<Integer,ILoan> loanMap) {
		this(helper);
		if (loanMap == null ) {
			throw new IllegalArgumentException(
				String.format("LoanMapDAO : constructor : loanMap cannot be null."));
		}
		this.loanMap_ = loanMap;
	}


	@Override
	public ILoan getLoanByID(int id) {
		if (loanMap_.containsKey(Integer.valueOf(id))) {
			return loanMap_.get(Integer.valueOf(id));
		}
		return null;
	}

	@Override
	public ILoan getLoanByBook(IBook book) {
		if (book == null ) {
			throw new IllegalArgumentException(
				String.format("LoanMapDAO : getLoanByBook : book cannot be null."));
		}
		for (ILoan loan : loanMap_.values()) {
			IBook tempBook = loan.getBook();
			if (book.equals(tempBook)) {
				return loan;
			}
		}
		return null;
	}

	@Override
	public List<ILoan> listLoans() {
		List<ILoan> list = new ArrayList<ILoan>(loanMap_.values());
		return Collections.unmodifiableList(list);
	}

	@Override
	public List<ILoan> findLoansByBorrower(IMember borrower) {
		if (borrower == null ) {
			throw new IllegalArgumentException(
				String.format("LoanMapDAO : findLoansByBorrower : borrower cannot be null."));
		}
		List<ILoan> list = new ArrayList<ILoan>();
		for (ILoan loan : loanMap_.values()) {
			if (borrower.equals(loan.getBorrower())) {
				list.add(loan);
			}
		}
		return Collections.unmodifiableList(list);
	}

	@Override
	public List<ILoan> findLoansByBookTitle(String title) {
		if (title == null || title.isEmpty()) {
			throw new IllegalArgumentException(
				String.format("LoanMapDAO : findLoansByBookTitle : title cannot be null or blank."));
		}
		List<ILoan> list = new ArrayList<ILoan>();
		for (ILoan loan : loanMap_.values()) {
			String tempTitle = loan.getBook().getTitle();
			if (title.equals(tempTitle)) {
				list.add(loan);
			}
		}
		return Collections.unmodifiableList(list);
	}

	@Override
	public void updateOverDueStatus(Date currentDate) {
		for (ILoan loan : loanMap_.values()) {
			loan.checkOverDue(currentDate);
		}
	}

	@Override
	public List<ILoan> findOverDueLoans() {
		List<ILoan> list = new ArrayList<ILoan>();
		for (ILoan loan : loanMap_.values()) {
			if (loan.isOverDue()) {
				list.add(loan);
			}
		}
		return Collections.unmodifiableList(list);
	}

	private int getNextId() {
		return ++nextID_;
	}


	@Override
	public ILoan createLoan(IMember borrower, IBook book) {
		if (borrower == null || book == null) {
			throw new IllegalArgumentException(
				String.format("LoanMapDAO : createLoan : borrower and book cannot be null."));
		}
		Date borrowDate = new Date();
		calendar_.setTime(borrowDate);
		calendar_.add(Calendar.DATE, ILoan.LOAN_PERIOD);
		Date dueDate = calendar_.getTime();
		ILoan loan = helper_.makeLoan(book, borrower, borrowDate, dueDate);
		return loan;
	}

	
	@Override
	public void commitLoan(ILoan loan) {
		int id = getNextId();
		loan.commit(id);		
		loanMap_.put(id, loan);		
	}

}
