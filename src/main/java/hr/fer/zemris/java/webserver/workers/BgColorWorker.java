package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class BgColorWorker implements IWebWorker{

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String bgcolor = context.getParameter("bgcolor");
		if(bgcolor != null && bgcolor.length() == 6) {
			context.setPersistentParameter("bgcolor", bgcolor);
			context.setPersistentParameter("message", "Color updated");
			context.getDispatcher().dispatchRequest("private\\pages\\color.smscr");
		}
		else {
			context.setPersistentParameter("message", "Color not updated");
			context.getDispatcher().dispatchRequest("private\\pages\\color.smscr");
		}
		
	}

}
