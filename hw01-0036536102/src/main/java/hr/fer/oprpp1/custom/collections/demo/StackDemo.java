package hr.fer.oprpp1.custom.collections.demo;
import java.util.Arrays;
import hr.fer.oprpp1.custom.collections.ObjectStack;

public class StackDemo {
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}

	public static void main(String[] args) {
		String[] split = args[0].split("\\s+");
		ObjectStack stack = new ObjectStack();
		boolean work = true;
		for(String s : split) {
			if(!work) {
				break;
			}
			if(isInteger(s)) {
				stack.push(Integer.parseInt(s));
			}
			else {
				int first = (int) stack.pop();
				int second = (int) stack.pop();
				switch(s) {
			    	case("+"):
				    	stack.push(second + first);
			    	    break;
			    	case("-"):
				    	stack.push(second - first);
			    	    break;
			    	case("/"):
			    		if(first == 0) {
			    			System.out.println("Dijeljenje s nulom je zabranjeno");
			    			work = false;
			    			break;
			    		}
				    	stack.push(second / first);
			    	    break;
			    	case("*"):
				    	stack.push(second * first);
			    	    break;
			    	case("%"):
			    		if(first == 0) {
			    			System.out.println("Dijeljenje s nulom je zabranjeno");
			    			work = false;
			    			break;
			    		}
				    	stack.push(second % first);
			    	    break;
			    	default:
			    		System.out.println("Neispravan izraz unesen");
			    		work = false;
				}
			}
		}
		if(work){
			if(stack.size() != 1) {
			System.out.println("error");
		    }
		    else {
		    	System.out.println("Expression evaluets to " + stack.pop() + ".");
	    	}

		}
		
	}

}
