package processor.pipeline;
import processor.Processor;
import generic.Instruction;

public class MemoryAccess {
	Processor containingProcessor;
	Instruction instruction;
	int aluResult;
	int ldResult;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		//TODO
		if(EX_MA_Latch.isMA_enable()){
			instruction=EX_MA_Latch.getInstruction();
			aluResult=EX_MA_Latch.getaluResult();
			String op=instruction.getOperationType().toString();
			MA_RW_Latch.setAluResult(aluResult);
			if(op.equals("load")){
				ldResult=containingProcessor.getMainMemory().getWord(aluResult);
				MA_RW_Latch.setLdResult(ldResult);
			}
			if(op.equals("store")){
				int op1= instruction.getSourceOperand1().getValue();
				int val=containingProcessor.getRegisterFile().getValue(op1);
				containingProcessor.getMainMemory().setWord(aluResult,val);
			}
			MA_RW_Latch.setInstruction(instruction);
			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
		}
	}

}
