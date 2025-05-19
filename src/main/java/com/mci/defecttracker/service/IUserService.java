package com.mci.defecttracker.service;

import java.util.List;
import java.util.Set;

import com.mci.defecttracker.entity.Role;
import com.mci.defecttracker.entity.User;
import com.mci.defecttracker.exception.BugTrackerException;
import com.mci.defecttracker.valueobject.RoleValueObject;

public interface IUserService {

	/**
	* Validates the login credentials when user tries to login.
	* 
	* @param username user name entered by the user.
	* @param password password entered by the user.
	* @return true if login validation is successful and false otherwise
	* @exception  BugTrackerException if validation exception occurs
	* 
	*/
	boolean validateLogin(String username, String password);

	/**
	* Fetches a the user record from user table in database
	* 
	* @param username the username of the user record.
	* @return user record from user table in database if user exist null otherwise.
	* @exception  BugTrackerException if exceptions occurs while fetching the role data
	* 
	*/
	User getUserbyUsername(String username) throws BugTrackerException;

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
	Boolean createUser(String firstName, String lastName, String email, String username, String password,
			Set<String> roles) throws BugTrackerException;

	/**
	* Fetches all the user roles from the role table in database
	* 
	* @return list of roles from role table in database
	* @exception  BugTrackerException if exceptions occurs while fetching the role data
	* 
	*/
	List<RoleValueObject> getAllRoles() throws BugTrackerException;

	/**
	* Fetches a single role data from the role table in database.
	* 
	* @param roleName name of the role to be fetched from database.
	* @return  the role from role table in database
	* @exception  BugTrackerException if exceptions occurs while fetching the role data
	* 
	*/
	Role getRole(String roleName) throws BugTrackerException;

	/**
	* Fetches user data from the user table in database.
	* 
	* @param username user name of the user to be fetched from database.
	* @return  the user data from the user table in database
	* @exception  BugTrackerException if exceptions occurs while fetching the user data
	* 
	*/
	User getUser(String username) throws BugTrackerException;

	/**
	* Performs search on user data based on first name,last name and user name
	* 
	* @param name name of the person to search.
	* @return  the list of user object from user table in database
	* 
	*/
	List<User> searchUserByUserName(String username) throws BugTrackerException;

	/**
	* Creates role in the database
	* 
	* @param rolename name of the person to search.
	* @return  the role object created in the database.
	* 
	*/
	Role createRole(String rolename) throws BugTrackerException;
	
	/**
	* Fetches all user data from the user table in database.
	* 
	* @return the list of all user data from the user table in database
	* @exception BugTrackerException if exceptions occurs while fetching the user data
	* 
	*/		
	public List<User> getAllUser() throws BugTrackerException ;

	boolean deleteRole(Role role) throws BugTrackerException;
	

}