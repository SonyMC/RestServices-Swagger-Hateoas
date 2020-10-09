package com.in28minutes.rest.webservices.restfulwebservices.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.in28minutes.rest.webservices.restfulwebservices.exception.UserNotCreatedException;
import com.in28minutes.rest.webservices.restfulwebservices.exception.UserNotFoundException;
import com.in28minutes.rest.webservices.restfulwebservices.user.User;
import com.in28minutes.rest.webservices.restfulwebservices.user.UserDaoService;
import com.in28minutes.rest.webservices.restfulwebservices.user.UserDaoService.Status;

@RestController
public class UserResourceController {
	
	
	@Autowired
	private UserDaoService userDaoService;

	
	//GET /users
	// retrieveAllUsers 
	
	@GetMapping(path="/users")
	@ResponseStatus(HttpStatus.FOUND)
	public List<User> retrieveAllUsers(){
		
		List<User> users = userDaoService.findAll();
		if (users.isEmpty()){
			throw new UserNotFoundException("No users found!!!");
		}
		
		
		return users;
		
	}

	
	
	// retrieveUser(int id)
	@GetMapping(path="/users/user&post/{id}")
	@ResponseStatus(HttpStatus.FOUND)
	public CollectionModel<User> retrieveUserAndPosts(@PathVariable int id){
		
		List<User> singleUserList = new ArrayList<>();	// need this to return a collection model 	
		
		User user = userDaoService.findOne(id); 
		
		if(user == null){
			throw new UserNotFoundException("User ID: " + id + " not found!!!");
		}
		
		// add the single user to the list 
		singleUserList.add(user);
		
		
		//Now iterate the loop and add the hateoas link 
		for(User singleUser:singleUserList){
			Link selfLink = linkTo(methodOn(this.getClass()).retrieveUserAndPosts(id)).withSelfRel();
			singleUser.add(selfLink.withRel("Current Link:"));
		}
		
	
		// Now create the self link  pointing to all posts by this user 		
		Link link2 = linkTo(methodOn(PostResourceController.class).retrieveAllUserPosts(id)).withSelfRel().withRel("User Posts: ");
		
		
		// Create the collection model 
		CollectionModel<User> result = CollectionModel.of(singleUserList, link2);
	
		return result;
	}

	
		
	// retrieveUser(int id)
	@GetMapping(path="/users/user/{id}")
	@ResponseStatus(HttpStatus.FOUND)
	public RepresentationModel<User> retrieveUser(@PathVariable int id){
		
		
		
	//	User user = userDaoService.findOne(id);
		RepresentationModel<User> modelUser = userDaoService.findOne(id); // because we extended USer class
		
		if(modelUser == null){
			throw new UserNotFoundException("User ID: " + id + " not found!!!");
		}
		
		//HATEOAS - Hypermedia As the Engine OF the Application State 
		// retrieve all resources 

		// Note : REsource is now Entity Model -> refer Hateoas documentatin  

		
		//Resource was replaced by EntityModel, and ControllerLinkBuilder was replaced by WebMvcLinkBuilder
		//EntityModel – represents RepresentationModel containing only single entity and related links.
		
		
		// create the self link  pointing to desired method 		
		Link linkto = linkTo(methodOn(this.getClass()).retrieveAllUsers()).withSelfRel();
		
	
		// The add method is inherirted from the RepresentationModel class which was inherited bu User class
		modelUser.add(linkto.withRel("all-users"));
				

		return modelUser;
	}

		
		
	// deleteUser(int id)
	@GetMapping(path="/users/deleteuser/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> deleteUser(@PathVariable int id){
		
		Status status = userDaoService.deleteUser(id);
		
		if(status == Status.FAILURE){
			throw new UserNotFoundException("User ID: " + id + "  does not exist to be deleted!!!");
		}
		
		return ResponseEntity.noContent().build();
		
	}
	
	// create a user using POST
	// input -> details of the user
	// output -> CREATED & Return teh create uri
	
		@PostMapping("/users")
		@ResponseStatus(HttpStatus.CREATED)
		public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
		
			
		// We do not want Request to provide id 
		if(user.getId()!= null){
			throw new UserNotCreatedException("User could not be Created. Please check Request Format!!!");
		}
			
			

		// Save the user 
		User savedUser = userDaoService.saveUser(user);
		
		
		if(savedUser == null){
			throw new UserNotCreatedException("User could not be Created. Please check Request Format!!!");
		}
		
		
	
		
	//	return Status and uri .Can be done using REsponse entity 
	// location uri needs to be created first ; take current uri and append new user id 
	// /users/{id} ->  replace id with user.getId	
		URI location = ServletUriComponentsBuilder
						.fromCurrentRequest()
						.path("/user/{id}")
						.buildAndExpand(savedUser.getId())
						.toUri();
		
		return ResponseEntity.created(location).build();
				
		
		
	}
	
	
		
	
}
