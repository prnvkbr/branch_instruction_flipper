
public class Scratch {

	public static void main(String args[]){
		
		int i = 8;
		int j=7;
		
	if(i < j)
			System.out.println("TRUE");
		else
			System.out.println("FALSE");
	}
	
	/*
	 * Depending on the IF type the flip method will return a boolean
	 */

	public static int flip(String condition, int position){
		Object lineNo = (int)position;
		
		//The user can specify the condition type and the line number 
		//of the condition which you want to flip. The user can specify the location based on his/her
		//program. I have currently included the location to be flipped for my simple program above.
		
		if(condition.equals("if_icmpge") && lineNo.equals(8))
			return 1;
		else if(condition.equals("if_icmpgt"))
			return 1;
		else if(condition.equals("if_icmple") /* && position==75*/)
			return 1;
		else if(condition.equals("if_icmplt"))
			return 1;
		else if(condition.equals("ifge"))
			return 1;
		else if(condition.equals("ifgt"))
			return 1;
		else if(condition.equals("ifle"))
			return 1;
		else if(condition.equals("iflt"))
			return 1;
		else if(condition.equals("if_icmpeq"))
			return 1;
		else if(condition.equals("if_icmpne"))
			return 1;
		
		return 0;
	}
}

