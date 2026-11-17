package com.example.dynamicAccess.DynamicDatabaseAccess;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@Log4j2
@SpringBootApplication
@EnableScheduling // alow to run background jobs
public class DynamicDatabaseAccessApplication {

	private static ConfigurableApplicationContext context;


	public static void main(String[] args) {

		try{
			context = SpringApplication.run(DynamicDatabaseAccessApplication.class, args);
		} catch (Exception ex){
			if(log.isDebugEnabled()){
				log.debug(ex.getStackTrace());
			}
		}

	}

	/**
	 * Safely closes the active Spring IoC container,
	 * stopping all managed beans, database connections, and scheduled tasks g
	 */
	public static void shutdown() {
		if (context.isActive()) {
			context.close();
		}
	}

	/**
	 * Programmatically
	 * restarts the entire Spring Boot application without killing the host JVM process.
	 */
	public static void restart() {

		// Retrieves the ApplicationArguments bean to capture the original
		// arguments passed into the applicationRetrieves
		var args = context.getBean(ApplicationArguments.class);


		// Spawns a new non-daemon thread (thread.setDaemon(false))
		// to ensure the JVM stays alive while the old context shuts down and the new one boots up.
		Thread thread = new Thread(() -> {
			context.close();
			context = SpringApplication.run(DynamicDatabaseAccessApplication.class, args.getSourceArgs());
		});

		thread.setDaemon(false);
		thread.start();
	}
}
