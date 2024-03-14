package processor.pipeline;

public class RegisterFile {
	int[] registerFile;
	int programCounter;
	int[] intregister = new int[32];
	public RegisterFile()
	{
		registerFile = new int[32];
		registerFile[0]=0;			
	}
	
	public int getValue(int registerNumber)
	{
		return registerFile[registerNumber];
	}
	
	public void setValue(int registerNumber, int value)
	{
		registerFile[registerNumber] = value;
	}

	public int getProgramCounter()
	{
		return programCounter;
	}

	public void setProgramCounter(int programCounter)
	{
		this.programCounter = programCounter;
	}
	
	public void setintregister(int index)
	{
		intregister[index]=1;
	}

	public boolean getintregister(int index)
	{
		if(intregister[index]==0)
		{
			return false;
		}
		return true;
	}
	
	public void incrementProgramCounter()
	{
		this.programCounter++;
	}
	
	public String getContentsAsString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("\nRegister File Contents:\n\n");
		sb.append("PC" + "\t: " + programCounter + "\n\n");
		
		sb.append("x" + 0 + "\t: " + registerFile[0]+ "\n");		//%xo is always 0 [RISC V]
		for(int i = 1; i < 32; i++)
		{
			sb.append("x" + i + "\t: " + registerFile[i] + "\n");
		}		
		sb.append("\n");
		return sb.toString();
	}
}
