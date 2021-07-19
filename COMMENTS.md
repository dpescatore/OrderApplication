# OrderApplication
I created a spring boot application as maven archetype in order to test API without using an Application Server. I used JUnit for testing.

# Architecture
I opted for a layered architecture:
* Controller: Expose Web API to the client without any business logic
* Service: Contains all the logic needed for application, exposes APIs to controller and uses repository layer. 
* Repository: Manage data persistence; I created a mockedRepository object that mock the DB and incapsulate all the DB Logic
* Model: map the DB entities 

## Details	
I use dependency Injection for service and repository and I also create, for these layers, both interface and implementation in order to develop an extensible application.

I used enum as a state machine to manage internal state of orders.

It was not so clear for me when assignment of photographer should be made, so I added a specific API to be called by Client.

I supposed the photographers API can be available in a separeted REST microservices, so I consumed a mocky.io REST API to obtain list and select a single photographer with a random function.

The API I moked can be found here: https://run.mocky.io/v3/c099cc58-4410-4bfc-a60c-d54f88d076cc where I put a custom JSON.

## Possible Optimizations
* It should be possibile to not sent back to client all the Order object, but to use one or more custom DTOs specific for each API if needed.
* Using Response objects instead of direct Model or DTO object (eg. findAll could return number of elements and/or pagination)
* Securization of API and authorization filters based on roles (eg. only photographers can Upload, only Operators can ccept or reject photos)
* A search API to retrieve orders based on some input condition
* Use a configuration env file to obtain constants or other elements (eg. MOCKED_PHOTOGRAPHERS_URL should be put in onfiguration)
* A lot of refactoring is possibile in OrderApplicationTests.java extracting common methods from code
