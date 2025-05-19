package com.mci.defecttracker.valueobject;

/** 
 * Value object class that is used to display data in JavaFX role list box in  create user view.
 * @author Suganthi Manoharan
 * @author MCI DIBSE
 * @author Software Engineering II 
*/
public class RoleValueObject {
    private Integer id;

    private String name;

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

}
