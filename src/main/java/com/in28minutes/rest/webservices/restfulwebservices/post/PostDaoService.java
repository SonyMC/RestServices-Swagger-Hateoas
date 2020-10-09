package com.in28minutes.rest.webservices.restfulwebservices.post;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.in28minutes.rest.webservices.restfulwebservices.exception.UserNotFoundException;
import com.in28minutes.rest.webservices.restfulwebservices.user.User;
import com.in28minutes.rest.webservices.restfulwebservices.user.UserDaoService;

@Component
public class PostDaoService {
	
	private static List<Post> posts = new ArrayList<>();
	
	
	@Autowired
	private UserDaoService userService;
	

	private static int postId;
	
	
	public enum Status{
		SUCCESS,FAILURE;
	}
	
	static{
		posts.add(new Post(1,11,"William Blake","Burning bright, in the forests of the night..."));
		posts.add(new Post(2,21,"WB Yeats","But, I being poor , have only my dreams. Tread softly on them..."));
		posts.add(new Post(3,31,"Wb Yeats","The years to come seemed a waste of breath. a waste of breath the years behind, In balance with this life, thsi death..."));
		posts.add(new Post(4,41,"Robert Frost","Miles to go before I sleep ..."));
	}

	//  post count 
	public int postCount(){
		return(posts.size());
	}
	
	
	// post id 
	public int postID(User user){
		String id1 = Integer.toString(user.getId());
		String id2 = Integer.toString(postCount());
		String fullIDString = id1.concat(id2);
		postId = Integer.valueOf(fullIDString);
		return postId;

		
	}
	
	
	// get all posts 
	public List<Post> findAllPosts(){
		return posts;
		
	}
	
	
	// get all posts belinging to a particular user 
	public List<Post> findAllPostsbyUser(int userId){
		
		List<Post> userPost = new ArrayList<>();
		
		// Find the user 
		User user = userService.findOne(userId);
		// Now get all postsw belonginfg to that user 
	    if(user != null){
			for(Post post:posts){
				if(post.getUserID() == userId)
					userPost.add(post);			
			} 
	    }else{
	    	throw new UserNotFoundException("User id: " + userId + " does not exist. Forst create user , then try posting again!!!");
	    }
    
		return userPost;
		
	}
	
	

	
	
	// Retireive a particular post 
	public Post findOnePost(int postId){
		for(Post post:posts){
			if(post.getPostId() == postId)
				return post;			
		}
		
		return null;
	}
	

	
	// Remove a particular post
	public Status deletePost(int postId){
		Iterator<Post> iterator = posts.iterator();
		
		while(iterator.hasNext()){
			Post post = iterator.next();
			if(postId == post.getPostId()){
				iterator.remove();
				return Status.SUCCESS;
			}
			
		}
		return Status.FAILURE;
		
	}
	
	
	//save a particular post 
	public Post savePost(Post post){
		// Find the user 
		
		User user = userService.findOne(post.getUserID());
		// Save post , only if user exists 
	    if(user != null){
	    	post.setPostId(postID(user));
	    	posts.add(post);
	    }else{
	    	throw new UserNotFoundException("User id: " + post.getUserID() + " does not exist. Forst create user , then try posting again!!!");
	    }
	    
		return post; 
	
	    }

}	
	

