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
			//checker provides boolean response
		if (!checker(author, title, callNumber, bookID)){
				throw new IllegalArgumentException();
			}
		this.author_ = author;
		this.title_ = title;
		this.callNumber_ = callNumber;
		this.bookID_ = bookID;
		this.state_ = EBookState.AVAILABLE;
		this.loanID_ = null;
		}
	
	//boolean checking the state of the parameters within the constructor
	private boolean checker(String author, String title, String callNumber, int bookID){
		return (
				author != null && !author.isEmpty() && 
				title != null && !title.isEmpty() &&
				callNumber != null && !callNumber.isEmpty() &&
				bookID > 0
				);
	}
			
	@Override
	public void borrow(ILoan loan) {
		
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
