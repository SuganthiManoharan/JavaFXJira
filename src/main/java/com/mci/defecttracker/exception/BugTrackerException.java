package com.mci.defecttracker.exception;


  /** Represents the custom exception for the Bug tracker application
  * @see <a href="https://docs.oracle.com/javase/tutorial/essential/exceptions/creating.html">Creating Exception</a>
  * @author MCI DIBSE
  * @author Software Engineering II 
 */
public class BugTrackerException extends Exception{

	private static final long serialVersionUID = 6030663843479984336L;

	public  BugTrackerException()
    {
        super();
    }

    public  BugTrackerException(String message)
    {
        super(message);
    }

    public BugTrackerException(String message, Throwable cause){
        super(message,cause);
    }
 
}
