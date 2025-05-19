package com.mci.defecttracker.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mci.defecttracker.controller.ProjectController;
import com.mci.defecttracker.controller.UserController;
import com.mci.defecttracker.entity.Role;
import com.mci.defecttracker.entity.User;
import com.mci.defecttracker.exception.BugTrackerException;
import com.mci.defecttracker.utils.CacheManager;
import com.mci.defecttracker.valueobject.RoleValueObject;
import com.mci.defecttracker.valueobject.UserValueObject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.SelectionMode;

/** Represents the main view of the user interface.
 * @author Suganthi Manoharan
 */
public class MainView extends Application {
	
	private UserController userController = new UserController();
	private ProjectController projectController = new ProjectController();
		
	List<RoleValueObject> roleVOList = new ArrayList<RoleValueObject>();
	String userName, password;
	final static Log logger = LogFactory.getLog(MainView.class);
	ProjectView projectView = new ProjectView();
	TableView<UserValueObject> userTable = new TableView<UserValueObject>();

	@Override
	public void start(Stage primaryStage) throws Exception {
		createDefaultUserandRole();
		getLoginView(primaryStage);

	}

	private void createDefaultUserandRole() {
		try {
			User userbyUsername =  userController.getUserbyUsername("administrator");
			if(userbyUsername==null) {
				Role AdminRole = userController.createRole("Admin");
				Role projectLeadRole = userController.createRole("ProjectLead");
				Role testEnggRole = userController.createRole("Test Engineer");
				Set<String> roles = new HashSet<String>();
				roles.add("Admin");
				roles.add("ProjectLead");
				roles.add("Test Engineer");
				userController.createUser("Admin","user","","administrator","password", roles);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Creates the main page javafx component with tabs.
	 * It calls the controller to get all the required data from database	 
	 *
	 * @param  primaryStage  reference to the main JavaFX stage
	 */
	public void getMainView(Stage primaryStage) {

		// create Tabs
		Tab tab = new Tab();
		primaryStage.setTitle("Welcome to Bug Tracker application");
		logger.debug("Building the main page view");

		// add label to the tab
		Label label = new Label("New User");
		tab.setContent(label);
		tab.setClosable(false);
		Group root = new Group();
		Scene scene = new Scene(root, 400, 300, Color.WHITE);

		//Adding the logout link
		TabPane tabPane = new TabPane();
		BorderPane mainPane = new BorderPane();

		Hyperlink link = new Hyperlink();
		link.setText("Logout");
		
		//hander for logout link
		link.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// remove the session user in cache
				User user = CacheManager.getInstance().getSessionUser();


				CacheManager.getInstance().cleanUserSession();
				getLoginView(primaryStage);
			}
		});

		link.setFont(Font.font("SanSerif", 15));

		mainPane.setRight(link);

		//Showing the welcome message in the screen
		Label l = new Label("Welcome " + CacheManager.getInstance().getSessionUser().getUsername());
		l.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		mainPane.setTop(l);
		
		User user = CacheManager.getInstance().getSessionUser();
		Collection<Role> roles = user.getRoles();
		boolean isAdmin = false;

		for(Role role:roles ) {
			if(role.getName().equalsIgnoreCase("Admin")) {
				isAdmin = true;
			}
		}

		
		Tab createUserTab = new Tab();
		buildTabViewCreateUser(createUserTab);
		
		// List Project Tabs
		Tab projectList = new Tab();
		buildTabViewProjectList(projectList);


		// Create project Tabs
		Tab searchUserTab = new Tab();
		buildTabViewSearchUser(searchUserTab);


		// Add all the Tabs to Tab pane
		tabPane.getTabs().add(createUserTab);
		tabPane.getTabs().add(projectList);
		tabPane.getTabs().add(searchUserTab);
		
		mainPane.setCenter(tabPane);

		mainPane.prefHeightProperty().bind(scene.heightProperty());
		mainPane.prefWidthProperty().bind(scene.widthProperty());

		root.getChildren().add(mainPane);

		primaryStage.hide();
		primaryStage.setFullScreen(true);


		// Load css context.css from resource folder for error message
		String css = getClass().getResource("/context.css").toExternalForm();
		scene.getStylesheets().add(css);

		primaryStage.setScene(scene);
		primaryStage.show();

		// add tab
	}

	/**
	* Builds the components for the project list,project create screen
	* @param projectListTab the project list tab object of the main screen
	*/
	private void buildTabViewProjectList(Tab projectListTab) {
		projectListTab.setText("Projects");	
		projectListTab.setClosable(false);
		StackPane tabB_stack = new StackPane();
		tabB_stack.setAlignment(Pos.CENTER);
		projectView.getProjectListView(projectListTab);
		
	}

	/**
	 * Builds the components for the search user tab
	 * @param searchUserTab the search user tab object of the main screen
	 */
	private void buildTabViewSearchUser(Tab searchUserTab) {		
		  searchUserTab.setText("Search users");
		  searchUserTab.setClosable(false);
		  getSearchUserView(searchUserTab);
	}	

	
	private void setTableAppearance() {
		userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		userTable.setPrefWidth(600);
		userTable.setPrefHeight(600);
	}

	/**
	* Builds the components for the user search result table.
	* @param projectListTab the project list tab object of the main screen
	*/
	private void getSearchUserView(Tab searchuserTab) {

		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(25, 25, 25, 25));
		Text sceneTitle = new Text("Search User");
		sceneTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		pane.add(sceneTitle, 0, 0, 2, 1);
		final HBox hb = new HBox();
		final TextField searchUser = new TextField();
		searchUser.setPromptText("Search for User");
		final Button searchButton = new Button("Search");
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					String searchText = searchUser.getText();
					if (searchText.contentEquals("")) {
						ObservableList<UserValueObject> searchUserlist = userController.getAllUser();
						userTable.setItems(searchUserlist);
					}

					ObservableList<UserValueObject> searchUserlist = userController.searchUserByUserName(searchText);
					userTable.setItems(searchUserlist);
				} catch (BugTrackerException e1) {
					logger.error("Error occured while search user. Contact system administrator",  e1.getCause());
					e1.printStackTrace();	
					Alert searchErrorAlert = new Alert(AlertType.ERROR);
					searchErrorAlert.setContentText("Error occured while search user. Contact system administrator");
					searchErrorAlert.show();
				}
			}
		});

		hb.getChildren().addAll(searchUser, searchButton);
		hb.setSpacing(3);

		setTableAppearance();

		TableColumn<UserValueObject, String> firstNameColumn = new TableColumn<UserValueObject, String>("First Name");

		// Setting the value of cell to variable(projectName) value from
		// ProjectValueObject object that is mapped to the userTable column
		firstNameColumn.setCellValueFactory(new PropertyValueFactory("firstName"));

		TableColumn<UserValueObject, String> lastNameColumn = new TableColumn<UserValueObject, String>("Last Name");

		lastNameColumn.setCellValueFactory(new PropertyValueFactory("lastName"));

		TableColumn<UserValueObject, String> userNameColumn = new TableColumn<UserValueObject, String>("User Name");

		userNameColumn.setCellValueFactory(new PropertyValueFactory("userName"));

		userTable.getColumns().setAll(firstNameColumn, lastNameColumn, userNameColumn);
		pane.add(hb, 1, 2);
		pane.add(userTable, 1, 3);
		try {
			ObservableList<UserValueObject> searchUserlist = userController.getAllUser();
			userTable.setItems(searchUserlist);
		} catch (BugTrackerException e1) {
			logger.error("Error occured while search user. Contact system administrator",  e1.getCause());
			e1.printStackTrace();
			Alert loadUserError = new Alert(AlertType.ERROR);
			loadUserError.setContentText("Error occured while search user. Contact system administrator");
			loadUserError.show();			
		}

		searchuserTab.setContent(pane);
	}
		
	
	/**
	* Builds the components for the create user
	* @param createUserTab the create user javafx tab object of the main screen
	*/
	private void buildTabViewCreateUser(Tab createUserTab) {
		createUserTab.setText("Create User");
		createUserTab.setClosable(false);
		// Add something in Tab
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(25, 25, 25, 25));

		Text sceneTitle = new Text("New User creation");
		sceneTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		pane.add(sceneTitle, 0, 0, 2, 1);

		final Text textMessage = new Text();
		textMessage.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		textMessage.setFill(Color.GREEN);
		pane.add(textMessage, 0, 1, 2, 1);

		Label firstName = new Label("First Name:");
		pane.add(firstName, 0, 2);
		final TextField firstNameField = new TextField();
		pane.add(firstNameField, 1, 2);

		Label LastName = new Label("Last Name:");
		pane.add(LastName, 0, 3);
		final TextField lastNameField = new TextField();
		pane.add(lastNameField, 1, 3);

		Label userName = new Label("User Name:");
		pane.add(userName, 0, 4);
		final TextField userNameField = new TextField();
		pane.add(userNameField, 1, 4);

		Label password = new Label("password:");
		pane.add(password, 0, 5);
		final PasswordField passwordField = new PasswordField();
		pane.add(passwordField, 1, 5);

		Label emailAddress = new Label("Email Address:");
		pane.add(emailAddress, 0, 6);
		final TextField emailAddressTextField = new TextField();
		pane.add(emailAddressTextField, 1, 6);

		// JavaFX List box component for displaying the roles
		ListView<String> listView = new ListView<String>();

		ObservableList<String> list = FXCollections.observableArrayList();
		try {
			roleVOList = userController.getAllRoles();
		} catch (BugTrackerException e) {
			logger.error("Error occured" + e.getMessage() + ". Contact system admin",  e.getCause());
			e.printStackTrace();
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("Error occured" + e.getMessage() + ". Contact system admin");
			a.show();
			return;
		}
		listView.setItems(list);

		roleVOList.forEach(roleVO -> {
			list.add(roleVO.getName());
		});

		listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		listView.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {

			}

		});

		Label rolesLabel = new Label("Role:");
		pane.add(rolesLabel, 0, 7);
		pane.add(listView, 1, 7);

		Button createButton = new Button("Create");
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		hbox.getChildren().add(createButton);
		pane.add(hbox, 1, 8);

		// Context menu Validator for form validation error messages
		final ContextMenu userNameValidator = new ContextMenu();
		userNameValidator.setAutoHide(true);

		final ContextMenu firstNameValidator = new ContextMenu();
		firstNameValidator.setAutoHide(true);

		final ContextMenu lastNameValidator = new ContextMenu();
		lastNameValidator.setAutoHide(true);

		final ContextMenu roleValidator = new ContextMenu();
		roleValidator.setAutoHide(true);

		final ContextMenu emailAddressValidator = new ContextMenu();
		emailAddressValidator.setAutoHide(true);

		final ContextMenu passValidator = new ContextMenu();
		passValidator.setAutoHide(true);

		createUserTab.setClosable(false);

		// Event handler for create user button
		createButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {

				// Removing the red background in the input fields
				removeTextErrorHighlight(firstNameField);
				removeTextErrorHighlight(lastNameField);
				removeTextErrorHighlight(emailAddressTextField);
				removeTextErrorHighlight(userNameField);
				removeTextErrorHighlight(passwordField);

				String firstName = firstNameField.getText();
				String lastName = lastNameField.getText();
				String email = emailAddressTextField.getText();
				String username = userNameField.getText();
				String password = passwordField.getText();
				ObservableList<String> selectedItems = listView.getSelectionModel().getSelectedItems();
				Set<String> rolesSet = new HashSet<String>();
				selectedItems.forEach(roles -> {
					rolesSet.add(roles);
				});

				// Validating the form and throw error message if form is invalid

				// variable to keep track if the form is valid
				Boolean isFormValid = true;

				// Check if First Name field has been entered
				if (firstName.equals("")) {
					firstNameValidator.getItems().clear();
					firstNameValidator.getItems().add(new MenuItem("Please enter First Name"));

					// Make the text box red to show error state
					setRed(firstNameField);
					// Show the context error message next to the input field
					firstNameValidator.show(firstNameField, Side.RIGHT, 10, 0);
					isFormValid = false;
				}

				// Check if Last Name field has been entered
				if (lastName.equals("")) {
					setRed(lastNameField);
					lastNameValidator.getItems().clear();
					lastNameValidator.getItems().add(new MenuItem("Please enter lastName"));
					lastNameValidator.show(lastNameField, Side.RIGHT, 10, 10);
					isFormValid = false;
					;
				}

				// Check if username field has been entered
				if (username.equals("")) {
					userNameValidator.getItems().clear();
					userNameValidator.getItems().add(new MenuItem("Please enter username"));
					userNameValidator.show(userNameField, Side.RIGHT, 10, 15);
					setRed(userNameField);

					isFormValid = false;
				}

				if (password.equals("")) {
					setRed(passwordField);

					passValidator.getItems().clear();
					passValidator.getItems().add(new MenuItem("Please enter password"));
					passValidator.show(passwordField, Side.RIGHT, 10, 20);
					isFormValid = false;
					;
				}

				if (email.equals("")) {
					setRed(emailAddressTextField);
					emailAddressValidator.getItems().clear();
					emailAddressValidator.getItems().add(new MenuItem("Please enter Email Address"));
					emailAddressValidator.show(emailAddressTextField, Side.RIGHT, 10, 30);
					isFormValid = false;
					;
				}

				if (selectedItems.isEmpty()) {
					setRedListView(listView);

					roleValidator.getItems().clear();
					roleValidator.getItems().add(new MenuItem("Please select roles"));
					roleValidator.show(listView, Side.RIGHT, 10, 50);
					isFormValid = false;
				}

				// If the form is not valid return
				if (!isFormValid)
					return;

				// Call the UserController to create the user.
				try {
					userController.createUser(firstName, lastName, email, username, password, rolesSet);
				} catch (BugTrackerException e) {
					logger.error("Error occured" + e.getMessage() + ". Contact system admin",  e.getCause());
					e.printStackTrace();
					Alert a = new Alert(AlertType.ERROR);
					a.setContentText("Error occured" + e.getMessage() + ". Contact system admin");
					a.show();


				}
				textMessage.setText("User created Successfully");

				// Clear all the text elements
				firstNameField.clear();
				lastNameField.clear();
				emailAddressTextField.clear();
				userNameField.clear();
				passwordField.clear();
				listView.getSelectionModel().clearSelection();
			}
		});

		createUserTab.setContent(pane);
	}

	/**
	* sets the text field to error state by highlighting the text border with red color
	* @param tf the text field to be highlighted
	*/
	private void setRed(TextField tf) {
		ObservableList<String> styleClass = tf.getStyleClass();

		if (!styleClass.contains("texterror")) {
			styleClass.add("texterror");
		}
	}
	
	/**
	* sets the listbox field to error state by highlighting the listbox border with red color
	* @param listView the listview instance
	*/
	private void setRedListView(ListView<String> listView) {
		ObservableList<String> styleClass = listView.getStyleClass();

		if (!styleClass.contains("texterror")) {
			styleClass.add("texterror");
		}
	}

	/**
	* removes the error state of text field
	* @param tf the text field whose error state has to be removed
	*/	
	private void removeTextErrorHighlight(TextField tf) {
		ObservableList<String> styleClass = tf.getStyleClass();
		styleClass.removeAll("texterror");
	}
	
	/**
	* Builds the User Interface for login screen
	*
	* @param primaryStage  the primary stage of the User Interface.
	*             
	*/
	private void getLoginView(Stage primaryStage) {
		primaryStage.setTitle("Bug tracker Login Page");
		primaryStage.setMaximized(false);
		primaryStage.setFullScreen(false);

		primaryStage.hide();
		BorderPane bp = new BorderPane();
		bp.setPadding(new Insets(10, 50, 50, 50));

		// Adding HBox
		HBox hb = new HBox();
		hb.setPadding(new Insets(20, 20, 20, 30));

		// Adding GridPane
		GridPane gridPane = new GridPane();
		buildLoginGridView(primaryStage, bp, hb, gridPane);

		// Adding BorderPane to the scene and loading CSS
		Scene scene = new Scene(bp);
		primaryStage.setScene(scene);
		// primaryStage.setResizable(false);
		primaryStage.show();
	}

	/**
	* Builds the grid pane for login screen
	*
	* @param primaryStage  the primary stage of the User Interface.
	* @param bp the string to display.
	* @param primaryStage  the string to display.
	* @param primaryStage  the string to display.
	*             
	*/
	private void buildLoginGridView(Stage primaryStage, BorderPane bp, HBox hb, GridPane gridPane) {
		gridPane.setPadding(new Insets(20, 20, 20, 20));
		gridPane.setHgap(5);
		gridPane.setVgap(5);

		// Implementing elements for GridPane
		Label lblUserName = new Label("Username");
		final TextField txtUserName = new TextField();
		Label lblPassword = new Label("Password");
		final PasswordField txtPassword = new PasswordField();
		Button loginButton = new Button("Login");
		final Label lblMessage = new Label();

		// Adding elements to GridPane layout
		gridPane.add(lblUserName, 0, 0);
		gridPane.add(txtUserName, 1, 0);
		gridPane.add(lblPassword, 0, 1);
		gridPane.add(txtPassword, 1, 1);
		gridPane.add(loginButton, 2, 1);
		gridPane.add(lblMessage, 1, 2);

		// Reflection for gridPane
		Reflection r = new Reflection();
		r.setFraction(0.7f);
		gridPane.setEffect(r);

		// DropShadow effect
		DropShadow dropShadow = new DropShadow();
		dropShadow.setOffsetX(5);
		dropShadow.setOffsetY(5);

		Text text = new Text("Bug tracker Login Page");
		text.setFont(Font.font("Courier New", FontWeight.BOLD, 28));
		// text.setEffect(dropShadow);

		// Adding text to HBox
		hb.getChildren().add(text);

		// Add ID's to Nodes
		bp.setId("bp");
		gridPane.setId("root");
		loginButton.setId("btnLogin");
		text.setId("text");

		// Action for btnLogin
		loginButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				userName = txtUserName.getText().toString();
				password = txtPassword.getText().toString();

				Boolean isValidUser=false;
				try {
					isValidUser = userController.validateLogin(userName, password);
				} catch (BugTrackerException e) {
					logger.error("Error occured" + e.getMessage() + ". Contact system admin",  e.getCause());
					e.printStackTrace();
					Alert a = new Alert(AlertType.ERROR);
					a.setContentText("Error occured" + e.getMessage() + ". Contact system admin");
					a.show();
				}
				System.out.println("isValidUser" + isValidUser);
				if (isValidUser) {
					getMainView(primaryStage);
				} else {
					lblMessage.setText("Incorrect user or pw.");
					lblMessage.setTextFill(Color.RED);
				}
				txtUserName.setText("");
				txtPassword.setText("");
			}
		});

		// Add HBox and GridPane layout to BorderPane Layout
		bp.setTop(hb);
		bp.setCenter(gridPane);
	}

}
