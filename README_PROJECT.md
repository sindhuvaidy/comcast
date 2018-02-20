# comcast
SPRING BOOT COMCAST TAKE HOME TEST

To Run this project locally
  $ git clone https://github.com/sindhuvaidy/comcast.git  
  $ mvn clean install

Rest Endpoints created are as follows

For Question 1: Spring Boot application with a "Hello World" REST endpoint
  METHOD : GET
  curl http://localhost:8080/comcast/welcome
  curl http://localhost:8080/comcast/welcome/{name}
  
For Question 2: REST endpoint that accepts a number, N, and returns a JSON array with the first N Fibonacci numbers
  METHOD : GET
  curl http://localhost:8080/comcast/fibonacci/{id}

For Question 3: REST endpoint that creates two threads that become deadlocked and detect it
  METHOD : GET
  curl http://localhost:8080/comcast/detectdeadlock
  
For Question 4:  REST endpoints that add, query, and delete rows in a database using HyperSQL
  ADD Customer
  METHOD : POST
    curl -H "Content-Type: application/json" -X POST -d '{"firstName":"abc","lastName":"xyz","customerId":"101","email":"test@gmail.com"}' http://localhost:8080/comcast/addcustomer
  METHOD : GET
    curl http://localhost:8080/comcast/customerdetails/{id}
  Delete Customer
  METHOD : DELETE
    curl -i -x DELETE http://localhost:8080/comcast/delete/{id}

For Question 5: REST endpoint that queries an external REST service using Spring Rest Template
  METHOD : GET
  curl http://localhost:8080/comcast/external 
