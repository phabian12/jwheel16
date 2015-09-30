package library.entities;

import java.text.DateFormat;
import java.util.Date;

import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ELoanState;

public class Loan implements ILoan {

	private int loanId_;
	private final IMember borrower_;
	private final IBook book_;
	private Date borrowDate_;
	private Date dueDate_;
	private ELoanState state_;
	
	public Loan(IBook book, IMember borrower, Date borrowDate, Date returnDate) {
		if (!sane(book, borrower, borrowDate, returnDate)) {
			throw new IllegalArgumentException("Loan: constructor : bad parameters");
		}
		this.book_ = book;
		this.borrower_ = borrower;
		this.borrowDate_ = borrowDate;
		this.dueDate_ = returnDate;	
		this.state_ = ELoanState.PENDING;
	}
	
	
	private boolean sane(IBook book, IMember borrower, Date borrowDate, Date returnDate) {
		return  ( book != null && 
				  borrower != null && 
				  borrowDate != null && 
				  returnDate != null && 
				  borrowDate.compareTo(returnDate) <= 0);
	}

	@Override
	public void commit(int loanId) {
		if (!(state_ == ELoanState.PENDING)) {
			throw new RuntimeException(
					String.format("Loan : commit : incorrect state transition  : %s -> %s\n", 
							state_, ELoanState.CURRENT));
		}
		if (loanId <= 0) {
			throw new RuntimeException(
					String.format("Loan : commit : id must be a positive integer  : %d\n", 
							loanId));
		}
		this.loanId_ = loanId;
		state_ = ELoanState.CURRENT;
		book_.borrow(this);
		borrower_.addLoan(this);
	}

	@Override
	public void complete() {
		if (!(state_ == ELoanState.CURRENT || state_ == ELoanState.OVERDUE)) {
			throw new RuntimeException(
					String.format("Loan : complete : incorrect state transition  : %s -> %s\n",
							state_, ELoanState.COMPLETE));
		}
		state_ = ELoanState.COMPLETE;		
	}

	@Override
	public boolean isOverDue() {
		return (state_ == ELoanState.OVERDUE);
	}

	@Override
	public boolean checkOverDue(Date currentDate) {
		if (!(state_ == ELoanState.CURRENT || state_ == ELoanState.OVERDUE )) {
			throw new RuntimeException(
					String.format("Loan : checkOverDue : incorrect state transition  : %s -> %s\n",
							state_, ELoanState.OVERDUE));
		}
		if (currentDate.compareTo(dueDate_) > 0) {
			state_ = ELoanState.OVERDUE;
		}
		return isOverDue();
	}

	@Override
	public IMember getBorrower() {
		return borrower_;
	}

	@Override
	public IBook getBook() {
		return book_;
	}

	@Override
	public int getID() {
		return loanId_;
	}
	
	public ELoanState getState() {
		return state_;
	}

	@Override
	public String toString() {
		return (String.format("Loan ID:  %d\nAuthor:   %s\nTitle:    %s\nBorrower: %s %s\nBorrowed: %s\nDue Date: %s", 
				loanId_, book_.getAuthor(), book_.getTitle(), borrower_.getFirstName(), borrower_.getLastName(),
				DateFormat.getDateInstance().format(borrowDate_),
				DateFormat.getDateInstance().format(dueDate_)));
	}
}