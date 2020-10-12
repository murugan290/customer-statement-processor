customer-statement-processor

This service will validate customer statement records and will generate the reports.

Prerequisite : The machine should have 'Java8' and Maven 3.3.9 or greater

Please follow the steps given below to build, launch and test the application through swagger UI

Step 1 : Build Code -> mvn clean install

Step 2 : Boot the application using the command -> mvn spring-boot:run

Step 3 : Access the swagger UI through http://localhost:8112/swagger-ui.html

Step 4 : Expand 'customer-statement-controller', click on the 'POST' end point button followed by clicking on 'Try it out'

Step 5 : Then upload the given file placed at folder -> src/test/resources/ using 'Choose File'

Step 6 : Click 'Execute' button

Step 7 : Check the 'Server Response' part to validate the HTTP status code and the failed record details.
