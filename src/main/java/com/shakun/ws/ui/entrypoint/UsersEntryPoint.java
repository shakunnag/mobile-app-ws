package com.shakun.ws.ui.entrypoint;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.shakun.ws.annotations.Secured;
import com.shakun.ws.exception.UserNotFoundException;
import com.shakun.ws.shared.dto.UserDTO;
import com.shakun.ws.ui.model.CreateUserRequestModel;
import com.shakun.ws.ui.model.DeleteUserProfileRest;
import com.shakun.ws.ui.model.ErrorMessages;
import com.shakun.ws.ui.model.RequestOperation;
import com.shakun.ws.ui.model.ResponseStatus;
import com.shakun.ws.ui.model.UpdateUserRequestModel;
import com.shakun.ws.ui.model.UserProfileRest;
import com.shakun.ws.ui.service.UserService;

@Path("/users")
public class UsersEntryPoint {
	@Autowired
	UserService userService;
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public UserProfileRest createUser(CreateUserRequestModel requestObject) {
		UserDTO userDto = new UserDTO();
		UserProfileRest response = new UserProfileRest();
		BeanUtils.copyProperties(requestObject, userDto);
		UserDTO createdUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createdUser, response);

		return response;
	}
	@Secured
	@GET
	@Path("/{userId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public UserProfileRest getUserProfile(@PathParam("userId") String userId) {
		UserProfileRest response = new UserProfileRest();
		UserDTO userProfile = userService.getUser(userId);
		BeanUtils.copyProperties(userProfile, response);
		return response;
	}

	/**
	 * This method returns list of users
	 * 
	 * @param userId
	 * @return list of users
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<UserProfileRest> getUsers(@DefaultValue("0") @QueryParam("start") int start,
			@DefaultValue("5") @QueryParam("limit") int limit) {
		List<UserProfileRest> response = new ArrayList<UserProfileRest>();
		List<UserDTO> listOfUsers = userService.getUsers(start, limit);
		if (null != listOfUsers) {
			for (UserDTO userDto : listOfUsers) {
				UserProfileRest model = new UserProfileRest();
				BeanUtils.copyProperties(userDto, model);
				model.setHref("/users/" + userDto.getUserId());
				response.add(model);
			}
		}
		return response;
	}
	@Secured
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public UserProfileRest updateUserProfile(@PathParam("id") String id, UpdateUserRequestModel userDetails) {
		UserProfileRest returnValue = new UserProfileRest();
		UserDTO storedUser = userService.getUser(id);
		if (null == storedUser) {
			throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND.getErrorMessage());
		}
		if (null != userDetails.getFirstName() && !userDetails.getFirstName().isEmpty()) {
			storedUser.setFirstName(userDetails.getFirstName());
		}
		if (null != userDetails.getLastName() && !userDetails.getLastName().isEmpty()) {
			storedUser.setLastName(userDetails.getLastName());
		}
		userService.updateUserDetails(storedUser);
		BeanUtils.copyProperties(storedUser, returnValue);
		return returnValue;
	}
	@Secured
	@DELETE
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public DeleteUserProfileRest deleteUserProfile(@PathParam("id") String id) {
		DeleteUserProfileRest returnValue = new DeleteUserProfileRest();
		returnValue.setRequestOperation(RequestOperation.DELETE);
		UserDTO storedUser = userService.getUser(id);
		userService.deleteUser(storedUser);
		returnValue.setResponseStatus(ResponseStatus.SUCCESS);;
		return returnValue;
	}
}
