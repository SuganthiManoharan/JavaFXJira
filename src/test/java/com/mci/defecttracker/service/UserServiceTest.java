package com.mci.defecttracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;

import com.mci.defecttracker.entity.Role;
import com.mci.defecttracker.entity.User;
import com.mci.defecttracker.exception.BugTrackerException;

//Configure the database properties in src/test/resource
public class UserServiceTest {
	IUserService userservice = new UserService();

	@Test
	void testUserSearch() throws BugTrackerException {
		List<User> users = userservice.searchUserByUserName("administrator");
		users.forEach(userEntity -> {
			String userName = userEntity.getUsername();
			assertEquals("administrator", userName);
		});
	}

	@Test
	void testCreateRole() throws BugTrackerException {
		Role role = userservice.createRole("testrole");
		assertNotNull(role);
		//teardown
		userservice.deleteRole(role);
	}
}
