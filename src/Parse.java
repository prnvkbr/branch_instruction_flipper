
import java.io.IOException;

import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFGE;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.IFLE;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.IF_ICMPGE;
import org.apache.bcel.generic.IF_ICMPGT;
import org.apache.bcel.generic.IF_ICMPLE;
import org.apache.bcel.generic.IF_ICMPLT;
import org.apache.bcel.generic.IF_ICMPNE;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.SWAP;
import org.apache.bcel.generic.Type;


public class Parse {
	
	public static void main(String args[]){
		
		try {
			JavaClass clazz = Repository.lookupClass("Scratch");
			
			JavaClass newClass = instrument(clazz);
			
			newClass.dump("instrumented/Scratch.class");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Method for inserting instrumented code
	 */
	
	private static JavaClass instrument(JavaClass clazz) {
		//Generate a new class
		ClassGen cg = new ClassGen(clazz);
		Method methods[] = clazz.getMethods();
		//Create a constant pool
		ConstantPoolGen cp = cg.getConstantPool();
		InstructionFactory factory = new InstructionFactory(cg);

		/*
		 * This loop iterates over the methods and the instructions per method
		 */
		
		for(int i=0;i<methods.length;i++){
			InstructionList instructionList = new InstructionList(methods[i].getCode().getCode());
			InstructionHandle [] handle = instructionList.getInstructionHandles();
			for(int j=0;j<handle.length;j++){
				if(handle[j].getInstruction() instanceof IF_ICMPGE || handle[j].getInstruction() instanceof IF_ICMPGT){
					int str = cp.addString(handle[j].getInstruction().getName()); //Get the name of the type of instruction
					int pos = cp.addInteger(handle[j].getPosition()); //Get the position of the instruction in the code
					InstructionList list = new InstructionList();
					list.insert(new LDC(str));
					list.append(new LDC(pos));
					//Method call passing the position and the name of the IF type
					list.append(factory.createInvoke("Scratch", "flip", Type.INT, 
								new Type[]{Type.STRING, Type.INT}, Constants.INVOKESTATIC)); 
					list.append(new IFEQ(handle[j]));
					list.append(new SWAP());
					instructionList.insert(handle[j], list);
				}
				else if(handle[j].getInstruction() instanceof IF_ICMPLE || handle[j].getInstruction() instanceof IF_ICMPLT){
					int instName = cp.addString(handle[j].getInstruction().getName());
					int position = cp.addInteger(handle[j].getPosition());
					InstructionList list1 = new InstructionList();
					list1.insert(new LDC(instName));
					list1.append(new LDC(position));
					list1.append(factory.createInvoke("Scratch", "flip", Type.INT, 
								new Type[]{Type.STRING, Type.INT}, Constants.INVOKESTATIC));
					list1.append(new IFEQ(handle[j]));
					list1.append(new SWAP());
					instructionList.insert(handle[j], list1);
				}
				else if(handle[j].getInstruction() instanceof IFGE || handle[j].getInstruction() instanceof IFLE
						|| handle[j].getInstruction() instanceof IFLT || handle[j].getInstruction() instanceof IFGT){
					InstructionList list3 = new InstructionList();
					int instName = cp.addString(handle[j].getInstruction().getName());
					int position = cp.addInteger(handle[j].getPosition());
					list3.insert(new LDC(instName));
					list3.append(new LDC(position));
					list3.append(factory.createInvoke("Scratch", "flip", Type.INT, 
								new Type[]{Type.STRING, Type.INT}, Constants.INVOKESTATIC));
					list3.append(new IFEQ(handle[j]));
					list3.append(new ISTORE(1));
					list3.append(new ICONST(-1));
					instructionList.insert(handle[j], list3);
				}
				else if(handle[j].getInstruction() instanceof IF_ICMPEQ || handle[j].getInstruction() instanceof IF_ICMPNE){
					int instName = cp.addString(handle[j].getInstruction().getName());
					int position = cp.addInteger(handle[j].getPosition());
					InstructionList list4 = new InstructionList();
					list4.insert(new LDC(instName));
					list4.append(new LDC(position));
					list4.append(factory.createInvoke("Scratch", "flip", Type.INT, 
								new Type[]{Type.STRING, Type.INT}, Constants.INVOKESTATIC));
					list4.append(new IFEQ(handle[j]));
					list4.append(new ISTORE(1));
					list4.append(new ISTORE(2));
					list4.append(new ILOAD(1));
					list4.append(new ILOAD(1));
					instructionList.insert(handle[j], list4);
				}
				MethodGen mg = new MethodGen(methods[i], clazz.getClassName(), cp);
				mg.setMaxStack(8);
				mg.setInstructionList(instructionList);
				cg.replaceMethod(methods[i], mg.getMethod());
				
			}
		}
		return cg.getJavaClass();
	}
}
