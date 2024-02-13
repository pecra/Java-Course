package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.tokens.INodeVisitor;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Rayred koji izvodi document cije parsirano stablo primi.
 * @author Petra
 *
 */
public class SmartScriptEngine {
	
	private DocumentNode documentNode;
	private RequestContext requestContext;
	private ObjectMultistack multistack = new ObjectMultistack();
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			try {
				SmartScriptEngine.this.requestContext.write(node.getProp().toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String variable = node.getVariable().asText();
			ValueWrapper endExpression = new ValueWrapper(node.getEndExpression().asText());
			ValueWrapper stepExpression = new ValueWrapper(node.getStepExpression().asText());
			ValueWrapper startExpression = new ValueWrapper(node.getStartExpression().asText());
			
			multistack.push(variable, startExpression);
			
			while(multistack.peek(variable).numCompare(endExpression.getValue())<=0) {
				int j = node.numberOfChildren();
				for(int i = 0; i < j; i++) {
					node.getChild(i).accept(this);
				}
				multistack.peek(variable).add(stepExpression.getValue());
			}
			
			multistack.pop(variable);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<ValueWrapper> temp = new Stack<>();
			for(Element e: node.getProp()) {
				if(e instanceof ElementConstantDouble) {
					temp.push(new ValueWrapper(((ElementConstantDouble) e).getProp()));
				}
				else if(e instanceof ElementVariable) {
					String var = ((ElementVariable) e).getProp();
					ValueWrapper val = multistack.peek(var);
					temp.push(val);
				}
				else if(e instanceof ElementString) {
					temp.push(new ValueWrapper(((ElementString) e).getProp()));
				}
				else if(e instanceof ElementConstantInteger) {
					temp.push(new ValueWrapper(((ElementConstantInteger) e).getProp()));
				}
				else if(e instanceof ElementOperator) {
					String op = ((ElementOperator) e).getProp();
					ValueWrapper second = temp.pop();//////////////
					ValueWrapper first = temp.pop();
					switch(op) {
					case("+"):
						first.add(second.getValue().toString());
						temp.push(first);
						break;
					case("-"):
						first.subtract(second.getValue().toString());
						temp.push(first);
						break;
					case("/"):
						first.divide(second.getValue().toString());
						temp.push(first);
						break;
					case("*"):
						first.multiply(second.getValue().toString());
						temp.push(first);
						break;
					
					}
				}
				else if(e instanceof ElementFunction) {
					String fu = ((ElementFunction) e).getProp().substring(1);
					switch(fu) {
					case("sin"):
						ValueWrapper first = temp.pop();
					    double d = Double.parseDouble(first.getValue().toString());
						d = Math.sin(d * Math.PI / 180);
						temp.push(new ValueWrapper(d));
						break;
					case("decfmt"):
						ValueWrapper f = temp.pop();
					    DecimalFormat format = new DecimalFormat(f.getValue().toString());
					    double x =(double) temp.pop().getValue();
						temp.push(new ValueWrapper(format.format(x)));
						break;
					case("dup"):
						ValueWrapper x2 = temp.pop();
						temp.push(new ValueWrapper(x2.getValue().toString()));
						temp.push(new ValueWrapper(x2.getValue().toString()));
						break;
					case("swap"):
						ValueWrapper x3 = temp.pop();
				    	ValueWrapper x4 = temp.pop();
				 		temp.push(x4);
				 		temp.push(x3);
						break;
					case("setMimeType"):
						ValueWrapper x5 = temp.pop();
				 		String str = x5.getValue().toString();
				 		requestContext.setMimeType(str);
						break;	
					case("paramGet"):
						ValueWrapper dv = temp.pop();
				    	ValueWrapper name = temp.pop();
				    	String value = requestContext.getParameter(name.getValue().toString());
				    	temp.push(value==null ? dv : new ValueWrapper(value));
						break;	
					case("pparamGet"):
						ValueWrapper dv2 = temp.pop();
				    	ValueWrapper name2 = temp.pop();
				    	String value2 = requestContext.getPersistentParameter(name2.getValue().toString());
				    	temp.push(value2==null ? dv2 : new ValueWrapper(value2));
						break;	
					case("pparamSet"):
						ValueWrapper name3 = temp.pop();
				    	ValueWrapper dv3 = temp.pop();
				    	requestContext.setPersistentParameter(name3.getValue().toString(),dv3.getValue().toString());
						break;
					case("pparamDel"):
				    	ValueWrapper name4 = temp.pop();
				    	requestContext.removePersistentParameter(name4.getValue().toString());
						break;	
					case("tparamGet"):
						ValueWrapper dv5 = temp.pop();
				    	ValueWrapper name5 = temp.pop();
				    	String value5 = requestContext.getTemporaryParameter(name5.getValue().toString());
				    	temp.push(value5==null ? dv5 : new ValueWrapper(value5));
						break;	
					case("tparamSet"):
						ValueWrapper name6 = temp.pop();
				    	ValueWrapper dv6 = temp.pop();
				    	requestContext.setTemporaryParameter(name6.getValue().toString(),dv6.getValue().toString());
						break;	
					case("tparamDel"):
				    	ValueWrapper name7 = temp.pop();
				    	requestContext.removeTemporaryParameter(name7.getValue().toString());
						break;		
					}
				}
			}
			if(temp.size()>0) {
				for(ValueWrapper v : temp) {
					try {
						StringBuilder sb = new StringBuilder();
						String s = v.getValue().toString();
						char[] arr = s.toCharArray();
						for(int i = 0,j = arr.length;i<j;i++) {
							char c = arr[i];
							if(c == '\\') {
								sb.append("\r\n");
								i = i + 3;
							}
							else {
								sb.append(c);
							}
						}
					//	requestContext.write( v.getValue().toString());
						requestContext.write(sb.toString());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			
			int j = node.numberOfChildren();
			for(int i = 0; i < j; i++) {
				node.getChild(i).accept(this);
			}
			
		}
	};
	public SmartScriptEngine(DocumentNode documentNode, RequestContext
	requestContext) {
	    this.documentNode = documentNode;
	    this.requestContext = requestContext;
	}
	public void execute() {
	    documentNode.accept(visitor);
	}
}
