package com.in28minutes.rest.webservices.restfulwebservices.post;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import org.springframework.hateoas.CollectionModel;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description="Model for Post!!!")  // for swagger doc
public class Post extends CollectionModel<Post>{
	
	@NotNull
	@ApiModelProperty(notes="Must be provided and shouldbe an integer")
	private Integer userID;
	
	@Null
	@ApiModelProperty(notes="Should not be provided")
	private Integer postId;
	
	@Size(min=3,message="Post Topic should have alteast 3 characters")
	@ApiModelProperty(notes="Post Topic should have alteast 3 characters")
	private String postTopic;
	
	@Size(min=5,message="Post Body should have alteast 5 characters")
	@ApiModelProperty(notes="Post Body should have alteast 5 characters")
	private String postBody;
	
	public Post() {

	}

	
	public Post(int userID, int postId, String postTopic, String postBody) {
		
		this.userID = userID;
		this.postId = postId;
		this.postTopic = postTopic;
		this.postBody = postBody;
	}




	public int getUserID() {
		return userID;
	}




	public void setUserID(int userID) {
		this.userID = userID;
	}




	public Integer getPostId() {
		return postId;
	}




	public void setPostId(int postId) {
		this.postId = postId;
	}




	public String getPostTopic() {
		return postTopic;
	}




	public void setPostTopic(String postTopic) {
		this.postTopic = postTopic;
	}




	public String getPostBody() {
		return postBody;
	}




	public void setPostBody(String postBody) {
		this.postBody = postBody;
	}




	@Override
	public String toString() {
		return "Post [userID=" + userID + ", postId=" + postId + ", postTopic=" + postTopic + ", postBody=" + postBody
				+ "]";
	}


   	
	

}
