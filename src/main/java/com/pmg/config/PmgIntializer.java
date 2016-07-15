package com.pmg.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class PmgIntializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		// Create the 'root' Spring application context
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		// EX:- rootContext.register(ServiceConfig.class, JPAConfig.class,
		// SecurityConfig.class);
		rootContext.register(MongoDBConfig.class, PmgSecurityConfig.class);
		// rootContext.register(MongoDBConfig.class);

		System.out.println("+++++++++++++++++++++++++++++++++++");
		System.out.println("Creating the Root Context" + rootContext.getApplicationName());
		System.out.println("+++++++++++++++++++++++++++++++++++");

		// Manage the lifecycle of the root application context
		servletContext.addListener(new ContextLoaderListener(rootContext));

		// Create the dispatcher servlet's Spring application context
		AnnotationConfigWebApplicationContext dispatcherServlet = new AnnotationConfigWebApplicationContext();
		dispatcherServlet.register(PmgConfiguration.class);

		// Register and map the dispatcher servlet
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",
				new DispatcherServlet(dispatcherServlet));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");

	}

}
