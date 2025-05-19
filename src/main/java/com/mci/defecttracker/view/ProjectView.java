package com.mci.defecttracker.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mci.defecttracker.controller.ProjectController;
import com.mci.defecttracker.entity.Project;
import com.mci.defecttracker.exception.BugTrackerException;
import com.mci.defecttracker.service.UserService;
import com.mci.defecttracker.valueobject.ProjectValueObject;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;

/** Represents a Project View Screen.
 * @author Suganthi Manoharan
*/
public class ProjectView {
	
	ObservableList<ProjectValueObject> projectList = null;
	TableView<ProjectValueObject> table = new TableView<ProjectValueObject>();
	ProjectController projectController = new ProjectController();
	private final static Log logger = LogFactory.getLog(ProjectView.class);

	/**
	* Builds the components for the update project screen
	* @param prjVO project value object to be updated 
	* @param projectListTab The Javafx tab instance of the project view
	* @return the grid pane  view of create project screen
	*/	
	public GridPane getProjectUpdateView(ProjectValueObject prjVO, Tab projectListTab) {

		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(25, 25, 25, 25));

		Text sceneTitle = new Text("Update Project");
		sceneTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		pane.add(sceneTitle, 0, 0, 2, 1);

		final Text projectTextMessage = new Text();
		projectTextMessage.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		projectTextMessage.setFill(Color.GREEN);
		pane.add(projectTextMessage, 0, 1, 2, 1);

		Label projectName = new Label("Name:");
		pane.add(projectName, 0, 2);
		final TextField nameTextField = new TextField();
		nameTextField.setText(prjVO.getProjectName());
		pane.add(nameTextField, 1, 2);

		Label desc = new Label("Description");
		pane.add(desc, 0, 3);
		final TextField descriptionField = new TextField();
		descriptionField.setText(prjVO.getDescription());
		pane.add(descriptionField, 1, 3);

		Button update = new Button("Update");
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		Button backButton = new Button("Back to Project");

		hbox.getChildren().addAll(update, backButton);
		hbox.setSpacing(2);
		pane.add(hbox, 1, 5);
		// handler for update project
		update.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				String firstName = nameTextField.getText();
				String desc = descriptionField.getText();
				if (firstName.equals("") || desc.equals("")) {
					Alert a = new Alert(AlertType.ERROR);
					a.setContentText("Please enter project name");
					a.show();
					return;
				}
				try {
					projectController.updateProject(firstName, desc, prjVO.getProjectId());
					Alert a = new Alert(AlertType.INFORMATION);
					a.setContentText("Project updated successfully");
					a.show();
				} catch (BugTrackerException e) {
					logger.error("Error updating projects",  e.getCause());
					e.printStackTrace();
				}
			}
		});

		// back to project list view screen
		backButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				getProjectListView(projectListTab);

			}
		});
		projectListTab.setContent(pane);
		return pane;
	}
	
	/**
	* Builds the components for the displaying the List of projects
	* @return the grid pane  view of List project screen
	*/
	public GridPane getProjectListView(Tab projectListTab) {
		
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(25, 25, 25, 25));
	    final HBox hb = new HBox();

		Text sceneTitle = new Text("Project List");
		sceneTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		pane.add(sceneTitle, 0, 0, 2, 1);

		setTableappearance();
		table.setEditable(true);
		try {
			projectList = projectController.getAllProjectsForUser();
		} catch (BugTrackerException e) {
			logger.error("Error getting all projects",  e.getCause());
			e.printStackTrace();			

		}
		table.setItems(projectList);

		
		TableColumn<ProjectValueObject, String> projectName = new TableColumn<ProjectValueObject, String>(
				"Project Name");
		
		//Setting the value of cell to variable(projectName) value from ProjectValueObject object that is mapped to the table column 
		projectName.setCellValueFactory(new PropertyValueFactory("projectName"));
		projectName.setCellFactory(TextFieldTableCell.forTableColumn());		

		//Start code - If the column should be made editable
		projectName.setEditable(true);
		projectName.setOnEditCommit(new EventHandler<CellEditEvent<ProjectValueObject, String>>() {

			@Override
			public void handle(CellEditEvent<ProjectValueObject, String> t) {
				ProjectValueObject projectValueObject = (ProjectValueObject) t.getTableView().getItems()
						.get(t.getTablePosition().getRow());
				projectValueObject.setProjectName(t.getNewValue());
			}
		});		
		//End Code - If the column should be made editable
		
		TableColumn<ProjectValueObject, String> projectDescription = new TableColumn<ProjectValueObject, String>(
				"Project Description");
		projectDescription.setCellValueFactory(new PropertyValueFactory("description"));
		//projectDescription.setEditable(true);

		table.getColumns().setAll(projectName, projectDescription);
		
		final TextField addProjectName = new TextField();
		addProjectName.setPromptText("Project Name");
		final TextField addProjectDescrption = new TextField();
		addProjectDescrption.setPromptText("Project Description");

        final Button createProjectButton = new Button("Create");
        
        //Connects the event handler with Create Button.
        createProjectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

				String firstName = addProjectName.getText();
				String desc = addProjectDescrption.getText();
				if(firstName.equals(""))
				{
					Alert a = new Alert(AlertType.ERROR);
					a.setContentText("Please enter project name");
					a.show();
					return;
				}
				if(desc.equals(""))
				{
					Alert descAlert = new Alert(AlertType.ERROR);
					descAlert.setContentText("Please enter project description");
					descAlert.show();
					return;
				}				
				try {
					//Adding new project entity to the database on clicking create button
					Project proj = projectController.createProject(firstName, desc);
					
					//ProjectValueObject is the model used for UI only for the JavaFx component Table View.
					//It holds value of the Project entity(name,description and project Id) from the database.
					//Creating new ProjectValueObject that was created in the database.
					ProjectValueObject po = new ProjectValueObject(proj.getProjectName(), proj.getDescription(), proj.getId());
					
					//By adding the new value object to the ObservableList projectList, the JavaFx component Table View automatically updates with the new project.
					projectList.add(po);

				} catch (BugTrackerException e1) {
					logger.error("Error creating projects.Contact administrator",  e1.getCause());
					e1.printStackTrace();	
				}
				table.setItems(projectList);
				addProjectName.clear();
				addProjectDescrption.clear();

            }
        });
        
        hb.getChildren().addAll(addProjectName, addProjectDescrption, createProjectButton);
        hb.setSpacing(3);
        
		addDeleteButtonToTable();
		addIssuesButtonToTable();
		addEditButton(projectListTab);
		
		pane.add(table, 1, 2);
		pane.add(hb, 1, 3);
		projectListTab.setContent(pane);

		return pane;
	}
	
	/**
	* adds the edit button to the project list table
	*/ 
   private void addEditButton(Tab projectListTab) {

		TableColumn<ProjectValueObject, Void> colBtn = new TableColumn("Edit Project");

		Callback<TableColumn<ProjectValueObject, Void>, TableCell<ProjectValueObject, Void>> cellFactory = new Callback<TableColumn<ProjectValueObject, Void>, TableCell<ProjectValueObject, Void>>() {
			@Override
			public TableCell<ProjectValueObject, Void> call(final TableColumn<ProjectValueObject, Void> param) {
				final TableCell<ProjectValueObject, Void> cell = new TableCell<ProjectValueObject, Void>() {

					private final Button btn = new Button("Edit");

					{
						btn.setOnAction((ActionEvent event) -> {
							ProjectValueObject projectValueObject = getTableView().getItems().get(getIndex());

							getProjectUpdateView(projectValueObject,projectListTab);
						});
					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(btn);
						}
					}
				};
				return cell;
			}
		};

		colBtn.setCellFactory(cellFactory);

		table.getColumns().add(colBtn);

	
   }


   /**
   * Sets the appearance of the table
   */
   private void setTableappearance() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefWidth(600);
        table.setPrefHeight(600);
    }
   
	
	/**
	* adds the delete button to the project list table
	*/ 
	private void addDeleteButtonToTable() {
		TableColumn<ProjectValueObject, Void> colBtn = new TableColumn("Delete Projects");

		Callback<TableColumn<ProjectValueObject, Void>, TableCell<ProjectValueObject, Void>> cellFactory = new Callback<TableColumn<ProjectValueObject, Void>, TableCell<ProjectValueObject, Void>>() {
			@Override
			public TableCell<ProjectValueObject, Void> call(final TableColumn<ProjectValueObject, Void> param) {
				final TableCell<ProjectValueObject, Void> cell = new TableCell<ProjectValueObject, Void>() {

					private final Button projectDeleteButton = new Button("Delete");

					{
						projectDeleteButton.setOnAction((ActionEvent event) -> {
							ProjectValueObject projectValueObject = getTableView().getItems().get(getIndex());							
							try {
								projectController.delete(projectValueObject.getProjectId());
								Alert a = new Alert(AlertType.INFORMATION);
								a.setContentText("Project deleted successfully");
								a.show();
								projectList = projectController.getAllProjectsForUser();

								table.setItems(projectList);
								
								return;
							} catch (BugTrackerException e) {
								e.printStackTrace();
							}
						});
					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(projectDeleteButton);
						}
					}
				};
				return cell;
			}
		};

		colBtn.setCellFactory(cellFactory);

		table.getColumns().add(colBtn);

	}
	
	
	/**
	* adds the Issues button to the project list table
	*/	
	private void addIssuesButtonToTable() {
		TableColumn<ProjectValueObject, Void> createAndViewBugButton = new TableColumn("Create and view ticket");

		Callback<TableColumn<ProjectValueObject, Void>, TableCell<ProjectValueObject, Void>> cellFactory = new Callback<TableColumn<ProjectValueObject, Void>, TableCell<ProjectValueObject, Void>>() {
			@Override
			public TableCell<ProjectValueObject, Void> call(final TableColumn<ProjectValueObject, Void> param) {
				final TableCell<ProjectValueObject, Void> cell = new TableCell<ProjectValueObject, Void>() {

					private final Button btn = new Button("Ticket");

					{
						btn.setOnAction((ActionEvent event) -> {
							ProjectValueObject projectValueObject = getTableView().getItems().get(getIndex());
						});
					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(btn);
						}
					}
				};
				return cell;
			}
		};

		createAndViewBugButton.setCellFactory(cellFactory);

		table.getColumns().add(createAndViewBugButton);
	}
}
