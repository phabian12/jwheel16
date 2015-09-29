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

	
	//borrow book
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

	
	//get Loan
	public ILoan getLoan() {
			//checking if the book is on loan
			//if the book isn't on loan, the null is returned
		if(!(this.state_.equals(EBookState.ON_LOAN))){
			return null;
		}
			//returns loan information if the book IS on loan
		return this.loanID_;
	}

	
	//return Book 
	public void returnBook(boolean damaged) {
			//if the book is on loan or lost,
			//then a runtime error will occur
		if(!(this.state_.equals(EBookState.ON_LOAN) 
				|| (this.state_.equals(EBookState.LOST)))){
			throw new RuntimeException ("The Book is not currently on Loan.");
		}
		
		//if there is no current book on loan,
		//the loan is declared as null
		loanID_ = null;
		
			//if the book is damaged, change the state to "damaged"
		if (damaged){
			this.state_ = EBookState.DAMAGED;
		}
			// if the book isn't damage, it's declared "available"
		else {
			this.state_ = EBookState.AVAILABLE;
		}
	}


	//setting the book state to Lost if the book is lost
	public void lose() {
		if(!(state_ == EBookState.ON_LOAN)){
			throw new RuntimeException("Book currently not on loan.");
		}
		//if the book is lost, set the state to lost
		this.state_ = EBookState.LOST;
	}
	

	//fix damages to the book)
	public void repair() {
		
		
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
