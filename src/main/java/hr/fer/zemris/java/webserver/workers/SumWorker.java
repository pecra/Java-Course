package hr.fer.zemris.java.webserver.workers;

import java.nio.file.Paths;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class SumWorker implements IWebWorker{

	@Override
	public void processRequest(RequestContext context) throws Exception {
		int a = 1;
		int b = 2;
		if(context.getParameterNames().contains("a")) {
			a = Integer.parseInt(context.getParameter("a"));
		}
		if(context.getParameterNames().contains("b")) {
			b = Integer.parseInt(context.getParameter("b"));
		}
		int j = a + b;
		String zbroj = Integer.toString(j);
		context.setTemporaryParameter("zbroj", zbroj);
		context.setTemporaryParameter("varA", Integer.toString(a));
		context.setTemporaryParameter("varB", Integer.toString(b));
		String imgName = null;
		imgName = ((a+b)%2 == 0) ? "/images/graph.png" : "/images/image2.jpg";
		context.setTemporaryParameter("imgName", imgName);
		context.getDispatcher().dispatchRequest(Paths.get("private\\pages\\calc.smscr").toString());
		
	}

}
