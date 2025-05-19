package com.mci.defecttracker.service;

import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mci.defecttracker.DAO.UserDAO;
import com.mci.defecttracker.entity.Role;
import com.mci.defecttracker.entity.User;
import com.mci.defecttracker.exception.BugTrackerException;
import com.mci.defecttracker.utils.CacheManager;
import com.mci.defecttracker.valueobject.RoleValueObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represents the service class for processing user data UserService class
 * provides a way for the client to interact with User related functionalities
 * in the application.
 * 
 * @author MCI DIBSE
 * @author Software Engineering II
 */
public class UserService implements IUserService {
	private final static Log logger = LogFactory.getLog(UserService.class);
	private UserDAO userDao = new UserDAO();

	/**
	* Validates the login credentials when user tries to login.
	* 
	* @param username user name entered by the user.
	* @param password password entered by the user.
	* @return true if login validation is successful and false otherwise
	* @exception  BugTrackerException if validation exception occurs
	* 
	*/	
	@Override
	public boolean validateLogin(String username, String password) {

		try {
			logger.debug("Validation of username and password in UserService");

			logger.debug("Fetching user details from database for username:" + username);

			User user = userDao.getUserByUserName(username);

			Base64.getEncoder().encodeToString(password.getBytes());

			if (user != null && user.getUsername().equals("")) {
				return false;
			} else if (user != null) {

				String encodedPassword = user.getPassword();
				byte[] decodedBytes = Base64.getDecoder().decode(encodedPassword);
				logger.debug("Decoded password");

				String decodedString = new String(decodedBytes);
				logger.debug("Comparing the password");

				if (password.equals(decodedString)) {

					// Setting the session user in cache
					CacheManager.getInstance().setSessionUser(user);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}

		} catch (Exception e) {
			logger.error("Error validating user:", e);
			return false;
		}
	}
	
	/**
	* Fetches a the user record from user table in database
	* 
	* @param username the username of the user record.
	* @return user record from user table in database if user exist null otherwise.
	* @exception  BugTrackerException if exceptions occurs while fetching the role data
	* 
	*/		
	@Override
	public User getUserbyUsername(String username) throws BugTrackerException {
		User user = userDao.getUserByUserName(username);
		return user;
	}
	
	/**
	* Inserts the user data in the database
	* 
	* @param firstName first name of the user record.
	* @param lastName last name of the user record.
	* @param email email of the user.          
	* @param password password of the user.
	* @param roles roles of the user
	* @return true if user creation is successful false otherwise
	* @exception  BugTrackerException if exceptions occurs while user creation
	* 
	*/		
	@Override
	public Boolean createUser(String firstName, String lastName, String email, String username, String password,
			Set<String> roles) throws BugTrackerException {
		logger.debug("Creation of User");

		Set<Role> roleSet = new HashSet<Role>();
		if(roles!=null) {
		roles.forEach(roleName -> {
			Role role;
			try {
				role = userDao.getRole(roleName);
				roleSet.add(role);
			} catch (BugTrackerException e) {
				
			}
		});
		}
		
		User user = userDao.createUser(firstName, lastName, email, username, password, roleSet);

		return true;

	}
	


	/**
	* Fetches all the user roles from the role table in database
	* 
	* @return list of roles from role table in database
	* @exception  BugTrackerException if exceptions occurs while fetching the role data
	* 
	*/	
	@Override
	public List<RoleValueObject> getAllRoles() throws BugTrackerException {			
		return userDao.getAllRoles();

	}

	/**
	* Fetches a single role data from the role table in database.
	* 
	* @param roleName name of the role to be fetched from database.
	* @return  the role from role table in database
	* @exception  BugTrackerException if exceptions occurs while fetching the role data
	* 
	*/	
	@Override
	public Role getRole(String roleName) throws BugTrackerException {
		return userDao.getRole(roleName);

	}

	/**
	* Fetches user data from the user table in database.
	* 
	* @param username user name of the user to be fetched from database.
	* @return  the user data from the user table in database
	* @exception  BugTrackerException if exceptions occurs while fetching the user data
	* 
	*/
	@Override
	public User getUser(String username) throws BugTrackerException {
		User user = userDao.getUserByUserName(username);
		return user;

	}
	
	/**
	* Performs search on user data based on first name,last name and user name
	* 
	* @param name name of the person to search.
	* @return  the list of user object from user table in database
	* 
	*/	
	@Override
	public List<User> searchUserByUserName(String username) throws BugTrackerException {
		List<User> user = userDao.searchUserByUserName(username);
		return user;

	}
	
	/**
	* Creates role in the database
	* 
	* @param rolename name of the person to search.
	* @return  the role object created in the database.
	* 
	*/		
	@Override
	public Role createRole(String rolename) throws BugTrackerException {
		Role role = userDao.createRole(rolename);
		return role;
	}
	
	/**
	* Fetches all user data from the user table in database.
	* 
	* @return the list of all user data from the user table in database
	* @exception BugTrackerException if exceptions occurs while fetching the user data
	* 
	*/	
	@Override
	public List<User> getAllUser() throws BugTrackerException {
		return userDao.getAllUser();
	}
	
	@Override
	public boolean deleteRole(Role role) throws BugTrackerException {
		return userDao.deleteRole(role);
	}
	

}
