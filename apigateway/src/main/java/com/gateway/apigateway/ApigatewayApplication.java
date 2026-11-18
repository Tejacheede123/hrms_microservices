package com.gateway.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApigatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApigatewayApplication.class, args);
	}
    /*without api gateway
                    React Application

                     /        |         \
                    /         |          \
                  /          |           \

    Employee     Leave     Notification
    Service      Service     Service
    8081         8082         8083
    The frontend must know:

Employee Service runs on 8081
Leave Service runs on 8082
Notification Service runs on 8083

This is difficult to maintain.
     */
    /*with api gateway
                     React Application
                        |
                        |
                API Gateway (8080)
                        |
        ----------------------------------
        |               |                |
 Employee Service   Leave Service   Notification
      8081             8082            8083
    Now the frontend knows only:http://localhost:8080
     The Gateway decides where to send the request.*/

}
