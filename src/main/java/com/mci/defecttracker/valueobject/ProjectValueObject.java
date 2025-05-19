package com.mci.defecttracker.valueobject;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** 
 * Value object class that is used to display data in JavaFX project list view
 * @author Suganthi Manoharan
 * @author MCI DIBSE
 * @author Software Engineering II 
*/
public  class ProjectValueObject {
	
	private final StringProperty projectName;
	private final StringProperty description;
	private final IntegerProperty projectId;

	public ProjectValueObject(String pName, String desc, int pId) {
		super();
		this.projectName = new SimpleStringProperty(pName);
		this.description = new SimpleStringProperty(desc);
		this.projectId = new SimpleIntegerProperty(pId);
	}

	public String getProjectName() {
		return projectName.get();
	}
	
	public void setProjectName(String name) {
		projectName.set(name);
	}

	public Integer getProjectId() {
		return projectId.get();
	}
	
	public void setProjectId(int prjtId) {
		projectId.set(prjtId); 
	}
	
	
	public String getDescription() {
		return description.get();
	}
	public void setDescription(String desc) {
		description.set(desc);
	}
	
}
	
	
	
