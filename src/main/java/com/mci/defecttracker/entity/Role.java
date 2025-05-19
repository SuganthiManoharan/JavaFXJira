package com.mci.defecttracker.entity;

import javax.persistence.*;
import java.util.Collection;

/** Represents the role table in the database
* @author Suganthi Manoharan
* @author MCI DIBSE
* @author Software Engineering II 
*/
@NamedQueries(  
	    {  
	        @NamedQuery(  
	        name = "getRoleByRoleName",  
	        query = "from Role r where r.name = :name"  
	        )  
	    }  
	) 

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    //many users can have many roles
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return name;
    }

}
