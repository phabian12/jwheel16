package library.entities;

import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;

public class Book implements IBook{

	//variables
	private String author_;
	private String title_;
	private String callNumber_;
	
	private int bookID_;
	private EBookState state_;
	private ILoan loanID_;
	
	//constructor
	public Book(String author, String title, String callNumber, int bookID){
		if (author != null && !author.isEmpty() && 
			title != null && !title.isEmpty() &&
			callNumber != null && !callNumber.isEmpty() &&
			bookID > 0){
				this.author_ = author;
				this.title_ = title;
				this.callNumber_ = callNumber;
				this.bookID_ = bookID;
				this.state_ = EBookState.AVAILABLE;
				this.loanID_ = null;
			}
		else{
			throw new IllegalArgumentException("Constructor: Bad Parameters");
		}
	}

			
	@Override
	public void borrow(ILoan loan) {
			//ensuring the value of the loan is not null
		if (loan == null){
			throw new IllegalArgumentException(String.format("Borrow: Bad Parameter: Loan Cannot be Null"));
		}
			//throws runtime exception if the book is not available
		else if(!(this.state_.equals(EBookState.AVAILABLE))){
			throw new RuntimeException("Book: State: Book is currently unavailable.");
		}
			//associate loanID with the bookState
		this.loanID_ = loan;
		state_ = EBookState.ON_LOAN;
		
	}

	@Override
	public ILoan getLoan() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void returnBook(boolean damaged) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void repair() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EBookState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCallNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
