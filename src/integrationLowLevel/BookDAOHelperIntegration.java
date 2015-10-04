package integrationLowLevel;

import java.util.List;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import library.daos.BookHelper;
import library.daos.BookMapDAO;
import library.interfaces.entities.IBook;

public class BookDAOHelperIntegration {
	
	private IBook myBook_;
	private BookMapDAO bookDao_;
	private BookHelper helper_;
	
	private String author_;
	private String title_;
	private String callNumber_;
	private List<IBook> bookList_;
	
	@Before
	public void setUp() throws Exception{
		helper_ = new BookHelper();
		bookDao_ = new BookMapDAO(helper_);
		
		author_ = "Stephen King";
		title_ = "The Dark Tower";
		callNumber_ = "KF8840";
	}
	
	@After
	public void reset() throws Exception{
		helper_ = null;
		bookDao_ = null;
	}

}
