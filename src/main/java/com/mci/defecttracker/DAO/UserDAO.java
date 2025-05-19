package com.mci.defecttracker.DAO;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.mci.defecttracker.entity.Role;
import com.mci.defecttracker.entity.User;
import com.mci.defecttracker.exception.BugTrackerException;
import com.mci.defecttracker.utils.HibernateUtil;
import com.mci.defecttracker.valueobject.RoleValueObject;

/**
 * Represents the DAO class for processing user data
 * 
 * @see <a href="https://de.wikipedia.org/wiki/Data_Access_Object">DAO (Data
 *      Access Object)</a>
 * @author Suganthi Manoharan
 * @author MCI DIBSE
 * @author Software Engineering II
 */
public class UserDAO {
	
	private final Session session = HibernateUtil.getSessionFactory().openSession();;
	private Transaction transaction = null;
	private CriteriaBuilder builder = session.getCriteriaBuilder();
	private final static Log logger = LogFactory.getLog(UserDAO.class);

	/**
	* Fetches user data from the user table in database.
	* 
	* @param username user name of the user to be fetched from database.
	* @return the user data from the user table in database
	* @exception BugTrackerException if exceptions occurs while fetching the user data
	* 
	*/	
	public User getUserByUserName(String username) throws BugTrackerException {
		try {

			Query query = session.createQuery("from User u where u.username = :username");
			query.setParameter("username", username);
			List<User> user = query.list();
			if (user.isEmpty()) {
				return null;

			} else {
				return user.get(0);

			}
		} catch (HibernateException e) {
			logger.error("Error fetching user:", e);
			throw new BugTrackerException("Error validation login",e );
		}
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
	public User createUser(String firstName, String lastName, String email, String username, String password,
			Set<Role> roles) throws BugTrackerException {

		try {

			User user = new User();
			user.setUsername(username);
			user.setFirstName(firstName);
			user.setLastName(lastName);

			user.setRoles(roles);
			
			//TODO : Base64 encoding is not recommended way to store password. Implement hashing with SALT
			//to store passwords. 
			String encoded = Base64.getEncoder().encodeToString(password.getBytes());

			user.setPassword(encoded);
			user.setEmail(email);
			transaction = session.beginTransaction();
			session.save(user);
			transaction.commit();
			return user;
		} catch (Exception e) {
			logger.error("Error creating user:", e);
			throw new BugTrackerException("Error creating user",e );

		}
	}
	
	/**
	* Fetches all the user roles from the role table in database
	* 
	* @return list of roles from role table in database
	* @exception  BugTrackerException if exceptions occurs while fetching the role data
	* 
	*/		
	public List<RoleValueObject> getAllRoles() throws BugTrackerException   {
		try {
			logger.info("Fetching all roles from database :");
			
			//Creating the Role Value Object to display in the JavaFx list box component in create user page
			
			List<Role> roles = session.createQuery("FROM Role").list();
			List<RoleValueObject> roleValueObject = new ArrayList<RoleValueObject>();
			roles.forEach(role -> {
				RoleValueObject roleVO = new RoleValueObject();
				roleVO.setId(role.getId());
				roleVO.setName(role.getName());
				roleValueObject.add(roleVO);
			});
			return roleValueObject;
		} catch (Exception e) {
			logger.error("Error fetching all roles from database:", e);
			throw new BugTrackerException("Error fetching all roles from database:",e );
		}

	}
	
	
	/**
	* Fetches a single role data from the role table in database.
	* 
	* @param roleName name of the role to be fetched from database.
	* @return  the role object from role table in database
	* @exception  BugTrackerException if exceptions occurs while fetching the role data
	* 
	*/		
	public Role getRole(String roleName) throws BugTrackerException {
		try {
			Role role = session.createNamedQuery("getRoleByRoleName", Role.class).setParameter("name", roleName)
					.getSingleResult();
			return role;
		} catch (Exception e) {
			logger.error("Error fetching roles from database:", e);
			throw new BugTrackerException("Error fetching all roles from database:",e );
		}

	}

	/**
	* Performs search on user data based on first name,last name and user name
	* 
	* @param name name of the person to search.
	* @return  the list of user object from user table in database
	* @throws BugTrackerException 
	* 
	*/
	public List<User> searchUserByUserName(String name) throws BugTrackerException {
		try {
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		Root<User> itemRoot = criteriaQuery.from(User.class);
		Predicate predicateForFirstName = criteriaBuilder.like(criteriaBuilder.upper(itemRoot.get("firstName")), '%' + name.toUpperCase()+ '%');
		Predicate predicateForLastName = criteriaBuilder.like(criteriaBuilder.upper(itemRoot.get("lastName")), '%' + name.toUpperCase()+  '%');
		Predicate predicateForUserName = criteriaBuilder.like(criteriaBuilder.upper(itemRoot.get("username")), '%' + name.toUpperCase()+  '%');
		Predicate predicateForUserSearchFinal
		  = criteriaBuilder.or(predicateForFirstName, predicateForLastName,predicateForUserName);
		criteriaQuery.where(predicateForUserSearchFinal);
		List<User> items = session.createQuery(criteriaQuery).getResultList();
		return items;
		} catch (Exception e) {
			logger.error("Error searching for users:", e);
			throw new BugTrackerException("Error searching for users:", e);
		}


	}
	
	/**
	* Creates role in the database
	* 
	* @param rolename name of the person to search.
	* @return  the role object created in the database.
	* 
	*/	
	public Role createRole(String rolename) throws BugTrackerException {
		try {
			Role role = new Role();
			role.setName(rolename);
			transaction = session.beginTransaction();
			session.save(role);
			transaction.commit();
			return role;
		} catch (Exception e) {
			logger.error("Error creating role in database:", e);
			throw new BugTrackerException("Error creating role in database:", e);
		}

	}

	/**
	* Fetches all user data from the user table in database.
	* 
	* @return the list of all user data from the user table in database
	* @exception BugTrackerException if exceptions occurs while fetching the user data
	* 
	*/		
	public List<User> getAllUser() throws BugTrackerException {
		try {
			Query query = session.createQuery("from User u ");
			List<User> user = query.list();
			return user;
		} catch (HibernateException e) {
			logger.error("Error fetching user:", e);
			throw new BugTrackerException("Error validation login",e );
		}
	}

	public Boolean deleteRole(Role role) throws BugTrackerException {
		try {
			transaction = session.getTransaction();
			transaction.begin();
			session.delete(role);
			transaction.commit();
			return true;
		} catch (Exception e) {
			throw new BugTrackerException("Error deleting role:", e);
		}
	}	

}
