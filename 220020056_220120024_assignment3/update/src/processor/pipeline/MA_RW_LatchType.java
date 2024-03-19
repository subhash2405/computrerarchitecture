package processor.pipeline;
import generic.Instruction;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	Instruction instruction;
	int aluResult;
	int ldResult;
	boolean check;
	
	public MA_RW_LatchType()
	{
		RW_enable = false;
		check= false;
	}

	public MA_RW_LatchType(boolean rW_enable)
	{
		RW_enable = rW_enable;
	}

	public MA_RW_LatchType(boolean rW_enable, Instruction instruction)
	{
		RW_enable = rW_enable;
		this.instruction = instruction;
	}
	public MA_RW_LatchType(boolean rW_enable, Instruction instruction, int LdResult)
	{
		RW_enable = rW_enable;
		this.instruction = instruction;
		this.ldResult = LdResult;
	}

	public MA_RW_LatchType(boolean rW_enable, Instruction instruction, int LdResult, int aLuResult)
	{
		RW_enable = rW_enable;
		this.instruction = instruction;
		this.ldResult = LdResult;
		this.aluResult = aLuResult;
	}
	//might have to add constructors

	public void setInstruction(Instruction instruction){
		this.instruction=instruction;
	}

	public Instruction getInstruction(){
		return this.instruction;
	}

	public void setLdResult(int ldResult){
		this.ldResult=ldResult;
	}

	public int getLdResult(){
		return this.ldResult;
	}

	public void setAluResult(int aluResult){
		this.aluResult=aluResult;
	}

	public int getAluResult(){
		return this.aluResult;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	public boolean getcheck() {
		return check;
	}
	
	public void setcheck(boolean Check) {
		check = Check;
	}

}
