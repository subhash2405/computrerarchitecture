package processor.pipeline;

import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;

public class MemoryAccess {
	Processor containingProcessor;
	Instruction instruction;
	int aluResult;
	int ldResult;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	// changed the attributes of the fucntion
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performMA()
	{
		//TODO
		if (EX_MA_Latch.getcheck()) {
			MA_RW_Latch.setcheck(true);
			MA_RW_Latch.setInstruction(null);
			EX_MA_Latch.setcheck(false);
		} 
		else if(EX_MA_Latch.isMA_enable()){
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

			if (instruction.getOperationType().ordinal() == 29) {
				IF_EnableLatch.setIF_enable(false);
			}   

			MA_RW_Latch.setInstruction(instruction);
			//EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
		}

		
	}

}
