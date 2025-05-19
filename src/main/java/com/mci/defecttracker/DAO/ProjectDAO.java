package com.mci.defecttracker.DAO;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.mci.defecttracker.entity.Project;
import com.mci.defecttracker.entity.User;
import com.mci.defecttracker.exception.BugTrackerException;
import com.mci.defecttracker.utils.HibernateUtil;

/**
 * Represents the DAO class for processing project data
 * 
 * @see <a href="https://de.wikipedia.org/wiki/Data_Access_Object">DAO (Data
 *      Access Object)</a>
 * @author Suganthi Manoharan
 * @author MCI DIBSE
 * @author Software Engineering II
 */
public class ProjectDAO {
	
	private final Session session = HibernateUtil.getSessionFactory().openSession();
	private Transaction transaction = null;
	private CriteriaBuilder builder = session.getCriteriaBuilder();
	private final static Log logger = LogFactory.getLog(ProjectDAO.class);

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
	public Project createProject(String projectName, String description, User user) throws BugTrackerException {
		try {
			Project proj = new Project();
			proj.setProjectName(projectName);
			proj.setCreatedByUser(user);
			proj.setDescription(description);
			transaction = session.beginTransaction();
			session.save(proj);
			transaction.commit();
			return proj;
			
		} catch (Exception e) {
			logger.error("Error creating Project:", e);
			throw new BugTrackerException("Error creating Project:", e);
		}

	}
	
	/**
	* Updates the project data in the database
	* 
	* @param name        the name of the project record.
	* @param description the description of the project.
	* @param user        the user object of logged in user

	* @return Project entity updated
	* @exception  BugTrackerException if exceptions occurs while project creation
	* 
	*/	
	
	public Project updateProject(String name, String description, int projectId) throws BugTrackerException {
		try {
		Query query = session.createQuery("from Project where id = :projectId ");
		query.setParameter("projectId", projectId);
		List<Project> projectList = query.list();
		Project proj = projectList.get(0);
		proj.setDescription(description);
		proj.setProjectName(name);
        transaction = session.getTransaction();
        transaction.begin();
		session.saveOrUpdate(proj);
		transaction.commit();
		return proj;
		} catch (Exception e) {
			logger.error("Error creating Project:", e);
			throw new BugTrackerException("Error creating Project:", e);
		}
	}
	
	

	
	/**
	* fetches the project list from the database
	* 
	* @return list of projects. 
	* @exception  BugTrackerException if exceptions occurs while project creation
	* 
	*/			
	public List<Project> getAllProjectsForUser(Integer userId) throws BugTrackerException {
		try {

			// Creating hibernate query to get all projects for logged in user
			Query query = session.createQuery("from Project where Createdbyuser_id = :userId ");
			query.setParameter("userId", userId);
			List<Project> list = query.list();
			return list;

		} catch(Exception e) {			
			throw new BugTrackerException("Error getting all projects:",e );
		}

	}
	
	/**
	* fetches the project list from the database
	* 
	* @return list of projects. 
	* @exception  BugTrackerException if exceptions occurs while project creation
	* 
	*/			
	public List<Project> getAllProjects() throws BugTrackerException {
		try {
			// Creating hibernate query to get all projects for logged in user
			Query query = session.createQuery("from Project");
			List<Project> list = query.list();
			return list;

		} catch(Exception e) {			
			throw new BugTrackerException("Error getting all projects:",e );
		}

	}	

	/**
	* deletes the project entity from the database
	* @param projectId Id of the project to be deleted.
	* 
	* @return true if delete was successful false otherwise.
	* @exception  BugTrackerException if exceptions occurs while project creation
	* 
	*/			
	public boolean deleteProjectWithProjectId(Integer projectId) throws BugTrackerException {
		try {
			Query query = session.createQuery("from Project where id = :projectId ");
			query.setParameter("projectId", projectId);
			List<Project> projectList = query.list();
			Project proj = projectList.get(0);
			transaction = session.getTransaction();
			transaction.begin();
			session.delete(proj);
			transaction.commit();
			return true;
		} catch (Exception e) {
			throw new BugTrackerException("Error deleting project:", e);
		}

	}

	/**
	* Deletes the project entity from the database
	* @param project the project entity to be deleted 
	* 
	* @return true if delete was successful false otherwise.
	* @exception  BugTrackerException if exceptions occurs while project creation
	* 
	*/	
	public boolean deleteProject(Project project) throws BugTrackerException {
		try {
			transaction = session.getTransaction();
			transaction.begin();
			session.delete(project);
			transaction.commit();
			return true;
		} catch (Exception e) {
			throw new BugTrackerException("Error deleting project:", e);
		}

	}
}
