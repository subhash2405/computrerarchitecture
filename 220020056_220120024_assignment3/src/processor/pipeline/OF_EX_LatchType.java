package processor.pipeline;

import generic.Instruction;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	Instruction instruction;
	
	public OF_EX_LatchType()
	{
		EX_enable = false;
	}
	public OF_EX_LatchType(boolean eX_enable) {  // constructor that sets enable as well
		EX_enable = eX_enable;
	}

	public OF_EX_LatchType(boolean eX_enable, Instruction instruction) {  // constructor that sets enable as well
		EX_enable = eX_enable;
		this.instruction = instruction;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}
	public void setInstruction(Instruction instruction) { // set instruction that was just executed
		this.instruction = instruction;
	}

	public Instruction getInstruction() { // get instruction that was just executed
		return this.instruction;
	}
}
