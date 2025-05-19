package com.mci.defecttracker.valueobject;

import java.util.Collection;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.mci.defecttracker.entity.Project;
import com.mci.defecttracker.entity.Role;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** 
 * Value object class that holds the user dates .
 * @author Suganthi Manoharan
 * @author MCI DIBSE
 * @author Software Engineering II 
*/
public class UserValueObject {
	
    private IntegerProperty id = new SimpleIntegerProperty();
	private StringProperty firstName = new SimpleStringProperty();

    private StringProperty lastName = new SimpleStringProperty();;

    private StringProperty userName= new SimpleStringProperty();;

    public String getUserName() {
		return userName.get();
	}

	public void setUserName(String uName) {
		userName.set(uName);
	}

	public IntegerProperty getId() {
		return id;
	}

	public void setId(Integer userId) {
		if(userId!=null)
			id.set(userId.intValue());
		else
			id.set(0);
	}

	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(String firstnme) {
		firstName.set(firstnme);
	}

	public String getLastName() {
		return lastName.get();
	}

	public void setLastName(String lName) {
		lastName.set(lName);
	}

    
 
}
