package com.in28minutes.rest.webservices.restfulwebservices.doc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



// url :
//	(1) http://localhost:8083/v2/api-docs
//	(1) http://localhost:8083/swagger-ui.html

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	
	  public static final Contact DEFAULT_CONTACT = new Contact("Sony Mathew", "localhost:8083/in28minutes.com", "haha@laugh.com");
	  public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
			  "Awesome API Title", "Api Documentation", "1.0", 
			  "urn:termsOfService", DEFAULT_CONTACT, 
			  "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList<VendorExtension>());
	  
	
	 
	  
	  private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<String>(Arrays.asList("application/json","application/xml"));
	  
	  
	  
// Configure Bean - Docket; specify Swagger 2; all the paths; all the APIs
	
	@Bean
	public Docket api(){
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(DEFAULT_API_INFO)
				.produces(DEFAULT_PRODUCES_AND_CONSUMES);
				

	}

//		@Bean
//		public Docket api(){
//	        return new Docket(DocumentationType.SWAGGER_2)  
//	                .select()                                  
//	                .apis(RequestHandlerSelectors.any())              
//	                .paths(PathSelectors.any())                          
//	                .build();
//					
//	
//		}
		
	  
	
	// The below method is needed because we are using Springfox-Swagger2 + Hateoas. Else the startup will fail with link resolver issue.
	@Primary
	@Bean
	public LinkDiscoverers discoverers() {
	    List<LinkDiscoverer> plugins = new ArrayList<>();
	    plugins.add(new CollectionJsonLinkDiscoverer());
	    return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
	   
	}
	
	
	
/*
 * 2.7.0 not rendering json for /v2/api-docs
 * Refer https://github.com/springfox/springfox/issues/1835
 * jackson-dataformat-xml will cause an issue in because it tries to priortise xml over json. I couldn't remove it because it's needed; I just overrode and added a RequestMappingHandlerAdapter to force it to use JSON as the media type
*/
	@Bean
	public RequestMappingHandlerAdapter requestHandler() {
	    RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
	    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
	    List<MediaType> mediaTypeList = new ArrayList<>();
	    mediaTypeList.add(MediaType.APPLICATION_JSON);
	    converter.setSupportedMediaTypes(mediaTypeList);
	    adapter.getMessageConverters().add(converter);
	    return adapter;
	}	
	

	
	//These methods are optional and can be used intead of using default values above
//    @Bean
//    public Docket postsApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("{ApplicationName}")
//                .apiInfo(buildApiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.regex("/.*"))
//                .build();
//    }
//    
//    
//    private ApiInfo buildApiInfo() {
//    	Contact contact = new Contact();
//    	contact.setName("Allianz");
//    	contact.setUrl("localhost:8083/in28minutes.com");
//    	contact.setEmail("complaintbox@shred.com");
//        
//    	return new ApiInfo("RestServicesAPI", "API for creating user contacts and posts", "1.0", "lookbehind.com", "Sony", "open", "fatchance.com");
//    	
// 
//    }

	
	
}
