package com.mci.defecttracker.controller;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mci.defecttracker.entity.Role;
import com.mci.defecttracker.entity.User;
import com.mci.defecttracker.exception.BugTrackerException;
import com.mci.defecttracker.service.IUserService;
import com.mci.defecttracker.service.UserService;
import com.mci.defecttracker.valueobject.RoleValueObject;
import com.mci.defecttracker.valueobject.UserValueObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents the controller class for processing user data It controls the data
 * flow into model object and updates the view whenever data changes. It keeps
 * User view and user service separate.
 * 
 * @author Suganthi Manoharan
 * @author MCI DIBSE
 * @author Software Engineering II
 */
public class UserController {
	private final static Log logger = LogFactory.getLog(UserController.class);

	private IUserService userService = new UserService();

	/**
	 * Validates the login credentials when user tries to login.
	 * 
	 * @param username user name entered by the user.
	 * @param password password entered by the user.
	 * @return true if login validation is successful and false otherwise
	 * @exception BugTrackerException if validation exception occurs
	 * 
	 */
	public boolean validateLogin(String username, String password) throws BugTrackerException {

		logger.debug("Login validation of username and password in Usercontroller");
		return userService.validateLogin(username, password);
	}

	/**
	 * Inserts the user data in the database
	 * 
	 * @param firstName first name of the user record.
	 * @param lastName  last name of the user record.
	 * @param email     email of the user.
	 * @param password  password of the user.
	 * @param roles     roles of the user
	 * @return true if user creation is successful false otherwise
	 * @exception BugTrackerException if exceptions occurs while user creation
	 * 
	 */
	public Boolean createUser(String firstName, String lastName, String email, String username, String password,
			Set<String> roles) throws BugTrackerException {
		logger.debug("Creation of user");
		return userService.createUser(firstName, lastName, email, username, password, roles);
	}

	/**
	 * Fetches all the user roles from the role table in database
	 * 
	 * @return List of roles from role table in database
	 * @exception BugTrackerException if exceptions occurs while fetching the role
	 *                                data
	 * 
	 */
	public List<RoleValueObject> getAllRoles() throws BugTrackerException {
		return userService.getAllRoles();

	}

	/**
	 * Fetches a single role data from the role table in database.
	 * 
	 * @param roleName name of the role to be fetched from database.
	 * @return the role from role table in database
	 * @exception BugTrackerException if exceptions occurs while fetching the role
	 *                                data
	 * 
	 */
	public Role getRole(String roleName) throws BugTrackerException {
		return userService.getRole(roleName);
	}

	/**
	 * Fetches a the user record from user table in database
	 * 
	 * @param username the username of the user record.
	 * @return user record from user table in database if user exist null otherwise.
	 * @exception BugTrackerException if exceptions occurs while fetching the role
	 *                                data
	 * 
	 */
	public User getUserbyUsername(String username) throws BugTrackerException {
		User user = userService.getUserbyUsername(username);
		return user;
	}
	
	/**
	* Creates role in the database
	* 
	* @param rolename name of the person to search.
	* @return  the role object created in the database.
	* 
	*/
	public Role createRole(String rolename) throws BugTrackerException {
		Role role = userService.createRole(rolename);
		return role;
	}
	
	/**
	* Performs search on user data based on first name,last name and user name
	* 
	* @param name name of the person to search.
	* @return  the ObservableList of user value object that can be mapped to JavaFX table view
	* 
	*/	
	public ObservableList<UserValueObject> searchUserByUserName(String username) throws BugTrackerException {
		List<User> user = userService.searchUserByUserName(username);
		return mapUserEntitytoValueObject(user);
	}

	/**
	* maps the database user entity to user value object
	* 
	* @param user List of user entity object.
	* @return  the ObservableList of user value object that can be mapped to JavaFX table view
	* 
	*/		
	private ObservableList<UserValueObject> mapUserEntitytoValueObject(List<User> user) {
		ObservableList<UserValueObject> userValueObjectList = FXCollections.observableArrayList();
		user.forEach(userEntity -> {
			UserValueObject userVO = new UserValueObject();
			userVO.setId(userEntity.getId());
			userVO.setFirstName(userEntity.getFirstName());
			userVO.setLastName(userEntity.getLastName());
			userVO.setUserName(userEntity.getUsername());
			userValueObjectList.add(userVO);
		});
		return userValueObjectList;
	}

	/**
	 * Fetches all user data from the user table in database.
	 * 
	 * @return the ObservableList of user value object that can be mapped to JavaFX
	 *         table view
	 * @exception BugTrackerException if exceptions occurs while fetching the user
	 *                                data
	 * 
	 */
	public ObservableList<UserValueObject> getAllUser() throws BugTrackerException {
		List<User> user = userService.getAllUser();
		return mapUserEntitytoValueObject(user);
	}
	
}
