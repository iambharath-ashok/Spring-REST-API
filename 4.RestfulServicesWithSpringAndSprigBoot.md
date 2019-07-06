# Global Exception Handling

-	Create a standard exception class with POJO
-	Define a custom exception handler class with all the application specific exceptions and any custom exceptions
-	Annotate Global Exception class with @ControllerAdvice class
-	Define exception handler methods 


			public class ExceptionResponse {
				
				private HttpStatus status;
				private String message;
				private Date timestamp;
				private String details;
			
			
			}
			
			@ControllerAdvice
			@RestController
			public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
			
				@ExceptionHandler(Exception.class)
				public void handlerAllExceptions(Exception ex, WebRequest webRequest) {
				
				}
				
				@ExceptionHandler(UserNotFoundException.class)
				public void handlerAllExceptions(UserNotFoundException ex, WebRequest webRequest) {
					ExceptionResponse response = new ExceptionResponse();
					response.setHttpStatus = HttpStatus.NOT_FOUND;
					...
				}
			}
			
##	Validations on REST API


-	We can use Hibernate Validator to define validation on the request 
-	Javax validation api provides annotations on bean to define validation on the request
-	Add @Valid annotation before @RequestBody param

				
				public ResponseEntity<Object>  createUser(@Valid @RequestBody User user) {
					
					User user = userService.saveUser(user);
					
					URI location = ServletUriComponentsBuilder()
							.fromCurrentRequest()
							.path("/{id}")
							.buildAndExpand(user.getId()).toUri();
							
					return ReponseEntity.created(location).build();

				}
				
				
-	Add JSR-303 annotations on top of Java Bean


				public class User {
					
					@Id
					@GeneratedValue(strategy= GenerationType.IDENTITY)
					@Column(name="")
					private Long id;
					
					
					@Size(min = 2, max= 40, message="size should be less that 2")
					private String name;
					
					@Past
					private Date date;
				
				}



##	Implementing HATEOAS for RESTful Services

-	HATEOS stands for Hypermedia As The Engine of Application State
-	HATEOAS is used to provide links of related resource in the response so that client can explore more about the API service
-	Add dependency of HATEOS to the application

			
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-hateos</artifactId>
			

-	Add link to other resources

		@GetMapping("/user/{id}")
		public Resource<User> getUserById(@PathVariable Long id) {
		
			User user = userService.findById(id);
			
			if(user== null)
				throw new UserNotFoundException("user not found for : "+id);
				
			ControllerLinkBuilder linkTo = ControllerLinkBuilder.linkTo(methodOn(this.getClass()).retrieveAllUsers());
			resource.add(linkTo.withRel("all-users"));
		}
	

##	I18n


-	I18n is customizing API response according to Locale
-	18n is locale specific
-	i18n is configured to return response according request origin
-	In order to configure i8n we need to configure few beans

	-	LocaleResolver
	-	LocaleChangeInterceptor
	-	MessageSource
	
	
	
			@Bean
			public LocaleResolver localeResolver() {
			
				SessionLocaleResolver localeResolver = new SessionLocaleResolver();
				localResover.setDefaultLocale(Local.US);
				return localResolver;
			}
			
			@Bean
			public MessageSource messageSource() {
				ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
				messageSource.setBasename("messages");
				return messageSource;
			}
			
			
			@Autowired
			MessageSource messageSource;
			
			
			@GetMapping("/hello-world-i18n")
			public String helloWorldInternationalization(@RequestHeader(name = "Accept-Language") Locale locale) {
				return messageSource.getMessage("good.morning.message", locale);
			}
			
-	Avoiding RequestHeader for each and using LocaleContextHolder to the locale details from Request

			@Bean
			public LocaleResolver localeResolver() {
			
				AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
				localResover.setDefaultLocale(Local.US);
				return localResolver;
			}
			
			
			@GetMapping("/hello-world-i18n")
			public String helloWorldInternationalization() {
				return messageSource.getMessage("good.morning.message", null, LocaleContextHolder.getLocale());
			}


-	Configuring message basename in application.properties


		spring.messages.basename = messages



## Content Negotiation - Implementing Support for XML


-	Add jackson-dataformat-xml dependency to get xml support
-	Add jaxb annotations on top of java beans for serialization and deserialization



## Monitoring Apps with Spring Boot Actuator

-	Add Spring Boot Actuator and HAL browser dependencies
-	Actuator supports lots of monitoring support around the applications
-	HAL - Hypertext Application Language
-	



## Static Filtering

-	Filtering is used when we dont need to send few fields as response
-	Filtering can be done in Dynamic or Static
-	Static filter can be using 


		public class SomeBean {
		
			private String field1;
			private String field2;
			
			@JsonIgnore
			private String field3;
			
		
		
		}

-	Adding on top of class 

		@JsonIgnoreProperties(value = {"field1", "field2"})
		public class SomeBean {
		
			private String field1;
			private String field2;
			private String field3;
		
		}


-	Dynamic Filtering

	-	Dynamic filtering can be done using MappingJacksonValue

##	Versioning RESTful Services - Basic Approach with URIs


-	We can use different uri mapping to different version resource
		
		
		For Ex: /api/v1/person/getall
		/api/v2/person/getall



##	Versioning RESTful Services - Header and Content Negotiation Approach


-	We can implement versioning with Request Header and Content

			
		@GetMapping(value="/person/param", params="version=1")	
		public PersonV1 paramV1() {
		
		}
		
-	curl i -H "Accept:application/json" -H "" -X GET http://localhost:8080/person/param?version=1		
		
		
		@GetMapping(value="/person/param", params="version=2")	
		public PersonV2 paramv2() {
		
		}

-	Headers 

		@GetMapping(value="/person/param", headers="X-API-VERSION=1")	
		public PersonV1 paramV1() {
		
		}

-	curl i -H "Accept:application/json" -H "X-API-VERSION:2" -X GET http://localhost:8080/person/param?version=1		

		
		@GetMapping(value="/person/param", headers="X-API-VERSION=2")	
		public PersonV2 paramV2() {
		
		}


##	 Implementing Basic Authentication with Spring Security


-	Add Spring security jar to application
-	Define custom username and password in application properties file
-	Client needs to send username and password with Basic Auth with credentials

	-	Adding basic auth username and password
	
			security.user.name = username
			security.user.password = password
			
			
			
			


































































-	validation
-	i18n
-	content negotiation
-	filtering
-	versioning
-	security
-	Exception handling 


-	swagger
-	hateos
-	Hal browser

