# Swagger

##	Configuring Auto Generation of Swagger Documentation

-	Add swagger dependencies

	springfox-swagger2 and springfox-swagger-ui 
	
-	Create Swagger config class


			@Configuration	
			@EnableSwagger
			public class SwaggerConfig {
				
				@Bean
				Docklet docklet() {
				
					return new Docklet(DocumentationType.SWAGGER2);
				}
			}	

-	After above cofiguration spring boot will automatically expose the swagger resources with endpoints /v2/api-docs
-	/v2/api-docs provides the contract for application and its resources
-	/swagger-ui.html provides a UI for client about the API resource and its representations

	
	
##	/v2/api-docs

-	This is a JSON representation of contract that swagger provides for the client
-	api-docs many details like 

	-	swagger version 
	-	Application info
		
		-	application name
		-	contact person
		-	email
		-	licence
		-	details and title etc
		
	-	paths
	
		-	groups the all the application APIs 
		-	Operations 
			-	with individual Methods GET, POST ,DELETE
			-	Details about to consuming services
			-	Produces, consumes
			-	Excepted Responses
			-	operationId
			-	link to resource representation  

	-	definitions

		-	provides details about Resource Representation classes
		-	Its properties and types 


## Swagger Html UI

-	Swagger UI can be used by client to verify the api resource in ui 
-	What is Request and its format and what would be expected responses
-	All the resource representation classes


## Enhancing Swagger Documentation with Custom Annotations


-	We can customize the swagger documentation with Swagger Annotations like info about application and api resources, its representation classes
-	We can customize the swagger at central place like in SwaggerConfig class or also at specific resources class

		
			import org.springframework.context.annotation.Bean;
			import org.springframework.context.annotation.Configuration;
			import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
			import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
			import com.google.common.base.Predicates;
			import springfox.documentation.builders.RequestHandlerSelectors;
			import springfox.documentation.spi.DocumentationType;
			import springfox.documentation.spring.web.plugins.Docket;
			import springfox.documentation.swagger2.annotations.EnableSwagger2;
			 
			@Configuration
			@EnableSwagger2
			public class Swagger2UiConfiguration extends WebMvcConfigurerAdapter
			{
				public static final Contact DEFAULT_CONTACT = new Contact("bharath", "https://iambharath@iambh.com", "imBhAshok");
					
				public static final ApiInfo = new ApiInf("Awesome API Title", DEFAULT_CONTACT);
			
				
				@Bean
				public Docket api() {
					
					
					//Register the controllers to swagger
					//Also it is configuring the Swagger Docket
					return new Docket(DocumentationType.SWAGGER_2).select()
					apiInfo(DEFAULT_API_INFO)		
					
				}
			 
				@Override
				public void addResourceHandlers(ResourceHandlerRegistry registry)
				{
					//enabling swagger-ui part for visual documentation
					registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
					registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
				}
			}

-	@Api – We can add this Annotation to the controller to add basic information regarding the controller.
-	@ApiOperation and @ApiResponses – We can add these annotations to any rest method in the controller to add basic information related to that method. e.g.
-	@ApiModelProperty – This annotation is used in the Model property to add some description to the Swagger output for that model attribute. e.g.




##	Api Model

-	REST resource classes (or model classes) require special annotations: @ApiModel and @ApiModelProperty. Here is how our Person class looks like:

		
			@ApiModel( value = "Person", description = "Person resource representation" )
			public class Person {
				@ApiModelProperty( value = "Person's first name", required = true ) 
				private String email;
				@ApiModelProperty( value = "Person's e-mail address", required = true ) 
				private String firstName;
				@ApiModelProperty( value = "Person's last name", required = true ) 
				private String lastName;
				// ...
			}
			
			
			public class Student
			{
				@ApiModelProperty(notes = "Name of the Student",name="name",required=true,value="test name")
				private String name;
			 
				@ApiModelProperty(notes = "Class of the Student",name="cls",required=true,value="test class")
				private String cls;
			 
				@ApiModelProperty(notes = "Country of the Student",name="country",required=true,value="test country")
				private String country;
			 
				public Student(String name, String cls, String country) {
					super();
					this.name = name;
					this.cls = cls;
					this.country = country;
				}
			 
				public String getName() {
					return name;
				}
			 
				public String getCls() {
					return cls;
				}
			 
				public String getCountry() {
					return country;
				}
			 
				@Override
				public String toString() {
					return "Student [name=" + name + ", cls=" + cls + ", country=" + country + "]";
				}
			}
			
			
##	RestController 

		
		@Produces( { MediaType.APPLICATION_JSON } )
		@Path( "/{email}" )
		@GET
		@ApiOperation( 
			value = "Find person by e-mail", 
			notes = "Find person by e-mail", 
			response = Person.class 
		)
		@ApiResponses( {
			@ApiResponse( code = 404, message = "Person with such e-mail doesn't exists" )    
		} )
		public Person getPeople( 
				@ApiParam( value = "E-Mail address to lookup for", required = true ) 
				@PathParam( "email" ) final String email ) {
			// ...
		}
		
		
		
##	Swagger2DemoRestController.java



			@Api(value = "Swagger2DemoRestController", description = "REST Apis related to Student Entity!!!!")
			@RestController
			public class Swagger2DemoRestController {
			 
				List<Student> students = new ArrayList<Student>();
				{
					students.add(new Student("Sajal", "IV", "India"));
					students.add(new Student("Lokesh", "V", "India"));
					students.add(new Student("Kajal", "III", "USA"));
					students.add(new Student("Sukesh", "VI", "USA"));
				}
			 
				@ApiOperation(value = "Get list of Students in the System ", response = Iterable.class, tags = "getStudents")
				@ApiResponses(value = {
						@ApiResponse(code = 200, message = "Suceess|OK"),
						@ApiResponse(code = 401, message = "not authorized!"),
						@ApiResponse(code = 403, message = "forbidden!!!"),
						@ApiResponse(code = 404, message = "not found!!!") })
			 
				@RequestMapping(value = "/getStudents")
				public List<Student> getStudents() {
					return students;
				}
			 
				@ApiOperation(value = "Get specific Student in the System ", response = Student.class, tags = "getStudent")
				@RequestMapping(value = "/getStudent/{name}")
				public Student getStudent(@PathVariable(value = "name") String name) {
					return students.stream().filter(x -> x.getName().equalsIgnoreCase(name)).collect(Collectors.toList()).get(0);
				}
			 
				@ApiOperation(value = "Get specific Student By Country in the System ", response = Student.class, tags = "getStudentByCountry")
				@RequestMapping(value = "/getStudentByCountry/{country}")
				public List<Student> getStudentByCountry(@PathVariable(value = "country") String country) {
					System.out.println("Searching Student in country : " + country);
					List<Student> studentsByCountry = students.stream().filter(x -> x.getCountry().equalsIgnoreCase(country))
							.collect(Collectors.toList());
					System.out.println(studentsByCountry);
					return studentsByCountry;
				}
			 
				// @ApiOperation(value = "Get specific Student By Class in the System ",response = Student.class,tags="getStudentByClass")
				@RequestMapping(value = "/getStudentByClass/{cls}")
				public List<Student> getStudentByClass(@PathVariable(value = "cls") String cls) {
					return students.stream().filter(x -> x.getCls().equalsIgnoreCase(cls)).collect(Collectors.toList());
				}
			}