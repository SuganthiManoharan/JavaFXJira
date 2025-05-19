package com.mci.defecttracker.service;

import java.util.List;

import com.mci.defecttracker.entity.Project;
import com.mci.defecttracker.entity.User;
import com.mci.defecttracker.exception.BugTrackerException;

public interface IProjectService {

	/**
	* Inserts the project data in the database
	* 
	* @param name        the name of the project record.
	* @param description the description of the project.
	* @param user        the user object of logged in user	
	* @return true if project creation is successful false otherwise
	* @exception  BugTrackerException if exceptions occurs while project creation
	* 
	*/
	Project createProject(String name, String decription, User user) throws BugTrackerException;

	/**
	* fetches the project list from the database
	* 
	* @return list of projects. 
	* @exception  BugTrackerException if exceptions occurs while project creation
	* 
	*/
	List<Project> getAllProjectsForUser(Integer userId) throws BugTrackerException;

	/**
	* deletes the project entity from the database.
	* 
	* @param projectId the project id of the project record to be deleted in database.
	* @return true if delete is successful false otherwise
	* @exception BugTrackerException if exceptions occurs while project creation
	* 
	*/
	boolean deleteProjectWithProjectId(int projectId) throws BugTrackerException;

	/**
	* updates the project entity in the database.
	* 
	* @param name the project name to be updated
	* @param description the description of the project
	* @param projectId the project id of the project record to be updated in database.
	* @exception  BugTrackerException if exceptions occurs while project update 
	* 
	*/
	Project updateProject(String name, String description, int projectId) throws BugTrackerException;

}