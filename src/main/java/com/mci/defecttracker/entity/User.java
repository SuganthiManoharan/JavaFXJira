package com.mci.defecttracker.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

 /** Represents the user table in the database
 * @author Suganthi Manoharan
 * @author MCI DIBSE
 * @author Software Engineering II 
 */
@NamedQueries(  
    {  
        @NamedQuery(  
        name = "getUserByUserName",  
        query = "from User u where u.username = :username"  
        )  
    }  
)  

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String username;
    
    private String password;

    // join column  example
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;


    
//    @ManyToMany(mappedBy = "users")
//    private Collection<Project> projects;
//
//
//    public Collection<Project> getProjects() {
//		return projects;
//	}
//
//	public void setProjects(Collection<Project> projects) {
//		this.projects = projects;
//	}

	public List<Project> getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(List<Project> createdByUser) {
		this.createdByUser = createdByUser;
	}

	@OneToMany(mappedBy = "createdByUser")
    private List<Project> createdByUser;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return  firstName + ' ' + lastName;
    }


}
