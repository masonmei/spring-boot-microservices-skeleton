package com.igitras.mvc.apis;

import java.util.Arrays;
import java.util.List;

import com.igitras.mvc.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST endpoint for the user functionality
 * 
 * @author anilallewar
 *
 */
@RestController
@RequestMapping("/")
public class UserController {

	@Value("${mail.domain}")
	private String mailDomain;

	private List<UserDto> users = Arrays.asList(new UserDto("Anil", "Allewar", "1", "anil.allewar@" + mailDomain),
			new UserDto("Rohit", "Ghatol", "2", "rohit.ghatol@" + mailDomain),
			new UserDto("John", "Snow", "3", "john.snow@" + mailDomain));

	/**
	 * Return all users
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	public List<UserDto> getUsers() {
		return users;
	}

	/**
	 * Return user associated with specific user name
	 * 
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "{userName}", method = RequestMethod.GET, headers = "Accept=application/json")
	public UserDto getUserByUserName(@PathVariable("userName") String userName) {
		UserDto userDtoToReturn = null;
		for (UserDto currentUser : users) {
			if (currentUser.getUserName().equalsIgnoreCase(userName)) {
				userDtoToReturn = currentUser;
				break;
			}
		}

		return userDtoToReturn;
	}
}
