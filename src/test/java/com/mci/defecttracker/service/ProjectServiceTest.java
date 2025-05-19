package com.mci.defecttracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import com.mci.defecttracker.entity.Project;
import com.mci.defecttracker.entity.Role;
import com.mci.defecttracker.entity.User;
import com.mci.defecttracker.exception.BugTrackerException;
import com.mci.defecttracker.service.ProjectService;
import com.mci.defecttracker.service.UserService;

//Configure the database properties in src/test/resource
public class ProjectServiceTest {

	IProjectService projectService = new ProjectService();
	IUserService userservice = new UserService();

	@Test()
	void createProject() throws BugTrackerException {
		User user = userservice.getUser("administrator");
		Project project = projectService.createProject("NewProjectdeleted", "ProjectDesc", user);
		assertNotNull(project.getId());
		// teardown
		projectService.deleteProjectWithProjectId(project.getId());
	}

}
