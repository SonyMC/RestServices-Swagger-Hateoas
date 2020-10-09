package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {
	
	private static List<User> users = new ArrayList<>();
	
	private static int userCount = 4;
	
	public enum Status{
		SUCCESS,FAILURE;
	}
	
	
	static{
		users.add(new User(1,"Jackychan",new Date()));
		users.add(new User(2,"Babychan",new Date()));
		users.add(new User(3,"Palliachan",new Date()));
		users.add(new User(4,"Bachan",new Date()));
	}
	
	
	public List<User> findAll(){
		return users;
		
	}
	
	
	
	public User saveUser(User user){
		    
		userCount = users.size() + 1;
		user.setId(userCount);
		users.add(user);
		return user;
	}
	
	
	
	public User findOne(int id){
		
		for(User user:users){
			if(user.getId() == id)
				return user;
		
					
		}
		
		return null;
	}
	
	
	public Status deleteUser(int id){
		Iterator<User> iterator = users.iterator();
		
		while(iterator.hasNext()){
			User user = iterator.next();
			if(id == user.getId()){
				iterator.remove();
				return Status.SUCCESS;
			}
			
		}
		return Status.FAILURE;
		
	}

}
