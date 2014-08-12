# About this
This is study note and properties about learning clounding programming in mooc. Will exclude all assignment, focus on testing and some note.

## SpringVideoController
First submit is clone to sample 3, and will starting to change it for port setting or other study.

- Change default port:
  - properties -> Run/Debug Settings -> Application -> Environment -> Add [SERVER_PORT] = 9000
- How to debug server and client the same thim-先跑 Server   
  - debug as Java Application 
  - debug as JUnit test  
- For RequestMapping like /path/{id} :

		@RequestMapping(value="/departments/{departmentId}")
		public String 	findDepatmentAlternative(@PathVariable("departmentId")String someDepartmentId)
        {… }          

- For @Multipart it only works for MultipartFile
  -  It need add some {id} for update, use @PUT to instand.
  - For MultipartFile controller code as follow:
  
		@RequestMapping(value=\{id}\data, method=RequestMethod.POST)
		public @ResponseBody RStatus setIDforData(@PathVariable("id") long id,
		@RequestParam("data") MultipartFile vData) {
		throw new ResourceNotFoundException();
		}
    - Refer [here](http://stackoverflow.com/questions/24985832/405-method-not-allowed-with-spring)	
	
   
          
### Reference
 - Eclopse, Gradle setting process refer this [pdf](https://d28rh4a8wq0iu5.cloudfront.net/mobilecloud/docs/Cloud%20Services%20Setup%20Corrected.pdf?response-content-type=application%2Foctet-stream&a=1&response-content-disposition=attachment)
 
 
 
 
 
   
