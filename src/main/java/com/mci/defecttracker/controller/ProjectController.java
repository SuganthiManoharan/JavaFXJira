package com.mci.defecttracker.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mci.defecttracker.entity.Project;
import com.mci.defecttracker.entity.User;
import com.mci.defecttracker.exception.BugTrackerException;
import com.mci.defecttracker.service.IProjectService;
import com.mci.defecttracker.service.ProjectService;
import com.mci.defecttracker.utils.CacheManager;
import com.mci.defecttracker.valueobject.ProjectValueObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents the controller class for processing project data It controls the
 * data flow into model object and updates the view whenever data changes. It
 * keeps project view and project service separate.
 * 
 * @author Suganthi Manoharan
 * @author MCI DIBSE
 * @author Software Engineering II
 */
public class ProjectController {
	
	private IProjectService projectService = new ProjectService();
	private final static Log logger = LogFactory.getLog(ProjectController.class);

	/**
	* Inserts the project data in the database
	* 
	* @param name the name of the project record.
	* @param description the description of the project.

	* @return true if project creation is successful false otherwise
	* @exception  BugTrackerException if exceptions occurs while project creation
	* 
	*/		
	public Project createProject(String name, String description) throws BugTrackerException {

		logger.debug("Creation of projects");

		// Fetching the (logged in) session user from the cache manager
		User user = CacheManager.getInstance().getSessionUser();
		Project proj = projectService.createProject(name, description, user);
		return proj;

	}

	/**
	 * Updates the project data in the database
	 * 
	 * @param name        the name of the project record.
	 * @param description the description of the project.
	 * @param user        the user object of logged in user+-+
	 * 
	 * @return Project entity updated
	 * @exception BugTrackerException if exceptions occurs while project creation
	 * 
	 */
	public Project updateProject(String name, String description, int projectId) throws BugTrackerException {
		logger.debug("Creation of projects");
		return projectService.updateProject(name, description, projectId);
	}
	
	/**
	* fetches the project list from the database
	* 
	* @return Javafx observable list that can be displayed in the Project JavaFX table view 
	* @exception  BugTrackerException if exceptions occurs while project creation
	* 
	*/	
	public ObservableList<ProjectValueObject> getAllProjectsForUser() throws BugTrackerException {

		User user = CacheManager.getInstance().getSessionUser();

		List<Project> projectList = projectService.getAllProjectsForUser(user.getId());

		// Creating a JavaFx Observable List object to populate the JavaFX table view
		ObservableList<ProjectValueObject> projectVOList = FXCollections.observableArrayList();

		// populating the list to display in the table
		projectList.forEach(project -> {

			ProjectValueObject projVO = new ProjectValueObject(project.getProjectName(), project.getDescription(),
					project.getId());

			projectVOList.add(projVO);
		});

		return projectVOList;

	}

	/**
	* Deletes the project entity from the database
	* @param project the project entity to be deleted 
	* 
	* @return true if delete was successful false otherwise.
	* @exception  BugTrackerException if exceptions occurs while project creation
	* 
	*/	
	public boolean delete(int projectId) throws BugTrackerException {
		return projectService.deleteProjectWithProjectId(projectId);
	}

}
