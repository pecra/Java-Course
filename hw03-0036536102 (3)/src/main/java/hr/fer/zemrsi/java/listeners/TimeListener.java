package hr.fer.zemrsi.java.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class TimeListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("created", System.currentTimeMillis());
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}
