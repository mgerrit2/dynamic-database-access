### Purpose to demonstrate dynamic datasource access
- If the datasource is disconnected, run springboot anyway.
- Connect to more datasource at the same time.
- Restart and shutdown springboot with jvm stays alive from controller
- Dynamically updates the database credentials

## Refresh properties


The dependency to add 
``` groovy
implementation 'org.springframework.boot:spring-boot-starter-actuator'
implementation 'org.springframework.cloud:spring-cloud-context:5.0.3'
```

The properties file to insert
``` properties
#######################################################################
# actuators
#######################################################################
# Expose health and refresh endpoints over web
management.endpoints.web.exposure.include=health,refresh,info

# Explicitly enable the refresh endpoint (if required)
management.endpoint.refresh.enabled=true
```


## temp
<div>
<a href="https://stackoverflow.com/questions/57224715/dynamic-datasource-using-springboot">
Dynamic Datasource using SpringBoot [duplicate]
</a>
</div>



## Datasource dynamicaly
<a href="https://www.baeldung.com/spring-boot-configure-data-source-programmatic">
https://www.baeldung.com/spring-boot-configure-data-source-programmatic
</a>

## Restart
<a href="https://www.baeldung.com/java-restart-spring-boot-app">
Programmatically Restarting a Spring Boot Application
</a>


## Reloading Properties Files in
<a href="https://www.baeldung.com/spring-reloading-properties#:~:text=To%20change%20properties%20in%20a,%2F%7Bpath%20to%20file%7D.">
Reloading Properties Files in
</a>

## Run deamon in sprinboot
<a href="https://stackoverflow.com/questions/40941845/how-to-start-and-eventually-stop-a-daemon-thread-in-spring-boot">
Run deamon in sprinboot
</a>

## Enable disable endpoints(stop request)
<a href="https://www.baeldung.com/spring-boot-enable-disable-endpoints-at-runtime">Enable and Disable Endpoints at Runtime With Spring Boot</a>



## Stop request
<a href="https://www.appsloveworld.com/springmvc/100/11/how-do-i-prevent-a-spring-boot-webapplication-from-accepting-http-requests-while?utm_content=cmp-true">HOW DO I PREVENT A SPRING BOOT WEBAPPLICATION FROM ACCEPTING HTTP REQUESTS WHILE IT IS STILL STARTING UP</a>

## Hikori datasource
<a href="https://www.baeldung.com/spring-boot-hikari#:~:text=Hikari%20is%20a%20JDBC%20DataSource,to%20Hikari%2C%20see%20this%20article.">Configuring a Hikari Connection</a>

## Scheduler
<a href="https://stackoverflow.com/questions/35169161/scheduler-not-running-in-spring-boot">Configuring a Hikari Connection</a>


## make sqlserver user

<a href="https://www.youtube.com/watch?v=dJ6c3OgIVDM">Make sqlserver user video</a>

userid:DynamicDatabaseAccess

password:DynamicDatabaseAccessAdmin


## Swagger
http://localhost:8080/swagger-ui/index.html

## actuators
http://localhost:8080/actuator/health
http://localhost:8080/actuator/info
http://localhost:8080/actuator/refresh
