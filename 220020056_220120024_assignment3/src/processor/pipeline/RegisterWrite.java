package processor.pipeline;

import generic.Instruction.OperationType;
import generic.Simulator;
import processor.Processor;
import generic.Instruction;


public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{
			Instruction instruction = MA_RW_Latch.getInstruction();
			int ldResult;
			String op;
			int rd;
			int aluResult;

			op=instruction.getOperationType().toString();
			if(op.equals("store")||op.equals("jmp")||op.equals("beq")||op.equals("blt")||op.equals("bne")||op.equals("bgt")){
				// nothing, just get out of this if-else clause
			}
			else if(op.equals("load")){
				ldResult=MA_RW_Latch.getLdResult();
				rd=instruction.getDestinationOperand().getValue();
				containingProcessor.getRegisterFile().setValue(rd,ldResult);
			}
			else if(op.equals("end")){
				Simulator.setSimulationComplete(true);
			}
			else{
				aluResult=MA_RW_Latch.getAluResult();
				rd=instruction.getDestinationOperand().getValue();
				containingProcessor.getRegisterFile().setValue(rd,aluResult);
			}
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}
