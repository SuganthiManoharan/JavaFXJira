package com.mci.defecttracker.utils;
import com.mci.defecttracker.entity.User;

/** 
 * Singleton class to store the logged in user details.
 * @see <a href="https://de.wikipedia.org/wiki/Singleton_(Entwurfsmuster)">Singleton (design pattern)</a>
 * @author Suganthi Manoharan
 * @author MCI DIBSE
 * @author Software Engineering II 
*/
public final class CacheManager {
	
    private static CacheManager instance;
    private static Object monitor = new Object();
    
    //Logged in user
    private User sessionUser;

    public User getSessionUser() {
		return sessionUser;
	}

	public void setSessionUser(User sessionUser) {
		this.sessionUser = sessionUser;
	}

	private CacheManager() {
    }
	
	/**
	* Creates the Singleton instance of the class
	*  
	* @return  CacheManager instance of class
	*/    
    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (monitor) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }
    
    public void cleanUserSession() {
    	sessionUser = null;
    }
}
