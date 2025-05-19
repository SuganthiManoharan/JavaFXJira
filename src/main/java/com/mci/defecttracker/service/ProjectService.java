package com.mci.defecttracker.service;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mci.defecttracker.DAO.ProjectDAO;
import com.mci.defecttracker.entity.Project;
import com.mci.defecttracker.entity.User;
import com.mci.defecttracker.exception.BugTrackerException;

 /**
 * Represents the Service class for processing project data
 * ProjectService class provides a way for the client to interact 
 * with project related functionalities in the application.
 */
@Path("/rest/projectService")
public class ProjectService implements IProjectService {
	private final static Log logger = LogFactory.getLog(ProjectService.class);

	private ProjectDAO projectDao = new ProjectDAO();

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
	@Override
	public Project createProject(String name, String decription, User user) throws BugTrackerException {
		logger.debug("Creation of projects in Project Service");
		return projectDao.createProject(name, decription, user);
	}
	

	

	/**
	* fetches the project list from the database
	* 
	* @return list of projects. 
	* @exception  BugTrackerException if exceptions occurs while project creation
	* 
	*/		
	@Override
	public List<Project> getAllProjectsForUser(Integer userId) throws BugTrackerException {
		logger.debug("Fetch all projects for logged in user");
		return projectDao.getAllProjectsForUser(userId);

	}
	

	
	@GET
    @Path("/hello")
    public Response processGetReq(@QueryParam("name") String name){
        return Response.status(200).entity("Hello "+(name!=null?name:"World")+"!").build();
    }
	
	@GET
    @Path("/getAllProjects")
    public Response processGetAllProject() throws BugTrackerException{
		 List<Project> projectList = projectDao.getAllProjects();
		 StringBuilder sb=new StringBuilder();  

		 for(Project proj : projectList) {


			 sb.append(proj.getProjectName());

		 }		 
		
		 
        return Response.status(200).entity(sb.toString()).build();
    }





	/**
	* deletes the project entity from the database.
	* 
	* @param projectId the project id of the project record to be deleted in database.
	* @return true if delete is successful false otherwise
	* @exception BugTrackerException if exceptions occurs while project creation
	* 
	*/		
	@Override
	public boolean deleteProjectWithProjectId(int projectId) throws BugTrackerException {
		projectDao.deleteProjectWithProjectId(projectId);
		return true;
	}

	/**
	* updates the project entity in the database.
	* 
	* @param name the project name to be updated
	* @param description the description of the project
	* @param projectId the project id of the project record to be updated in database.
	* @exception  BugTrackerException if exceptions occurs while project update 
	* 
	*/		
	@Override
	public Project updateProject(String name, String description, int projectId) throws BugTrackerException {
		return projectDao.updateProject(name,description,projectId);		
	}
	

}
