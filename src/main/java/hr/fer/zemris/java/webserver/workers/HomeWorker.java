package hr.fer.zemris.java.webserver.workers;

import java.nio.file.Paths;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class HomeWorker implements IWebWorker{

	@Override
	public void processRequest(RequestContext context) throws Exception {
		if(context.getPersistentParameterNames().contains("bgcolor")) {
			context.setTemporaryParameter("background", context.getPersistentParameter("bgcolor"));
		}
		else {
			context.setTemporaryParameter("background","7F7F7F");
		}
		context.getDispatcher().dispatchRequest(Paths.get("private\\pages\\home.smscr").toString());
	}

}
