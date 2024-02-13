package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class EchoParams implements IWebWorker{

	@Override
	public void processRequest(RequestContext context) throws Exception {
        context.setMimeType("text/html");
        try {
            context.write("<html><body><table><thread>"
            		+ "<tr><th>Naziv</th><th>Vrijednost</th></tr></thread>");
            context.write("<tbody>");
            for (String s : context.getParameterNames()) {
            	context.write("<tr><td>");
            	context.write(s);
            	context.write("</td><td>");
                context.write(context.getParameter(s));
                context.write("</td>");
            }
            context.write("</table></body></html>");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		
	}

}
