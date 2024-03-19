// package processor.pipeline;
// import generic.Instruction;
// import generic.Instruction.OperationType;
// import generic.Statistics;
// import generic.Operand.OperandType;
// import processor.Processor;

// public class Execute {
// 	Processor containingProcessor;
// 	OF_EX_LatchType OF_EX_Latch;
// 	EX_MA_LatchType EX_MA_Latch;
// 	EX_IF_LatchType EX_IF_Latch;
// 	IF_EnableLatchType IF_EnableLatch;
// 	IF_OF_LatchType IF_OF_Latch;
// 	private boolean b;
	
// 	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch, IF_OF_LatchType if_OF_Latch,IF_EnableLatchType if_EnableLatchType)
// 	{
// 		this.containingProcessor = containingProcessor;
// 		this.OF_EX_Latch = oF_EX_Latch;
// 		this.EX_MA_Latch = eX_MA_Latch;
// 		this.EX_IF_Latch = eX_IF_Latch;
// 		this.IF_OF_Latch = if_OF_Latch;
// 		this.IF_EnableLatch = if_EnableLatchType;
// 	}
	

// 	public void performEX()
// 	{	
// 		boolean jmpRes = false;
// 		if(OF_EX_Latch.getcheck())
// 		{
// 			EX_MA_Latch.setcheck(true);
// 			OF_EX_Latch.setcheck(false);
// 			EX_MA_Latch.setInstruction(null);
// 		}
// 		else if(OF_EX_Latch.isEX_enable()){
// 		if(OF_EX_Latch.isEX_enable()){
// 			Instruction instruction = OF_EX_Latch.getInstruction();
// 			EX_MA_Latch.setInstruction(instruction);
// 			int aluResult = 0;
// 			int nowPc = containingProcessor.getRegisterFile().programCounter -1;
// 			String opType = instruction.getOperationType().toString();
// 			b = opType.equals("addi") || opType.equals("subi") || opType.equals("muli") || opType.equals("divi") || opType.equals("andi") || opType.equals("ori") || opType.equals("xori") || opType.equals("slti") || opType.equals("slli") || opType.equals("srli") || opType.equals("srai") || opType.equals("load") || opType.equals("store");
// 			if(opType.equals("beq") || opType.equals("blt") ||opType.equals("bgt") || opType.equals("bne") || opType.equals("jmp") || opType.equals("end")) {
// 				Statistics.setNumberOfBranchTaken(Statistics.getNumberOfBranchTaken() + 2);
// 				IF_EnableLatch.setIF_enable(false);
// 				IF_OF_Latch.setOF_enable(false);
// 				OF_EX_Latch.setEX_enable(false);
// 			}
// 			if(opType.equals("add") || opType.equals("sub") || opType.equals("mul") || opType.equals("div") || opType.equals("and") || opType.equals("or") || opType.equals("xor") || opType.equals("slt") || opType.equals("sll") || opType.equals("srl") || opType.equals("sra")){
// 				int rs1 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
// 				int rs2 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand2().getValue());
// 				switch(opType){
// 					case "add":
// 						aluResult = rs1 + rs2;
// 						break;
// 					case "sub":
// 						aluResult = rs1 - rs2;
// 						break;
// 					case "div":
// 						aluResult = rs1 / rs2;
// 						containingProcessor.getRegisterFile().setValue(31, rs1%rs2);
// 						break;
// 					case "mul":
// 						aluResult = rs1 * rs2;
// 						break;
// 					case "and": 
// 						aluResult = rs1 & rs2;
// 						break;
// 					case "or":
// 						aluResult = rs1 | rs2;
// 						break;
// 					case "xor":
// 						aluResult = rs1 ^ rs2;
// 						break;
// 					case "slt":
// 						aluResult = 0;
// 						if(rs2 > rs1) {aluResult = 1;}
// 						else {aluResult =0;}
// 						break;
// 					case "sll":
// 						aluResult = rs1 << rs2;
// 						break;
// 					case "srl":
// 						aluResult = rs1 >>> rs2;
// 						break;
// 					case "sra":
// 						aluResult = rs1 >> rs2;
// 						break;
// 					default:
// 						System.out.print("Issue detected in R3 type switch");
// 						break;
// 				}}
// 			else if(b){
// 				int rs1 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
// 				int immed = instruction.getSourceOperand2().getValue();
// 				switch(opType){
// 					case "subi":
// 						aluResult = rs1 - immed;
// 						break;
// 					case "addi":
// 						aluResult = rs1 + immed;
// 						break;
// 					case "muli":
// 						aluResult = rs1*immed;
// 						break;
// 					case "divi":
// 						aluResult = rs1/immed;
// 						containingProcessor.getRegisterFile().setValue(31, rs1%immed);
// 						break;
// 					case "andi":
// 						aluResult = rs1&immed;
// 						break;
// 					case "slti":
// 						aluResult = 0;
// 						if(immed > rs1) aluResult = 1;
// 						else aluResult=0;
// 						break;
// 					case "ori":
// 						aluResult = rs1 | immed;
// 						break;
// 					case "xori":
// 						aluResult = rs1 ^ immed;
// 						break;
// 					case "slli": 
// 						aluResult = rs1 << immed;
// 						break;
// 					case "srli": 
// 						aluResult = rs1 >>> immed;
// 						break;
// 					case "srai": 
// 						aluResult = rs1 >> immed;
// 						break;
// 					case "load":
// 						aluResult = rs1 + immed;
// 						break;

// 					default: 
// 						System.out.print("Issue detected in Execute.java, switch(OpType) for R2I");
// 						break;
// 				}}
// 				else if(opType.equals("store"))
// 				{
// 					int op1= containingProcessor.getRegisterFile().getValue(instruction.getDestinationOperand().getValue());
// 					int op2 = instruction.getSourceOperand2().getValue();
// 					aluResult = op1 + op2;
// 				}
// 			else if(opType.equals("end")){
// 					OF_EX_Latch.setEX_enable(false);
// 					IF_OF_Latch.setOF_enable(false);
// 					IF_EnableLatch.setIF_enable(false);
// 					Statistics.setNumberOfBranchTaken(Statistics.getNumberOfBranchTaken()+2);
// 			}
// 			else if(opType.equals("jmp")){
// 				OperandType jmpType = instruction.getDestinationOperand().getOperandType();
// 				int immed = 0;
// 				if(jmpType == OperandType.Immediate)
// 					{immed = instruction.getDestinationOperand().getValue();}
// 				else {immed = containingProcessor.getRegisterFile().getValue(instruction.getDestinationOperand().getValue());}
// 				aluResult = immed + nowPc ;
// 				jmpRes = true;
// 				EX_IF_Latch.setIsBranch_enable(true,aluResult);
// 			}
// 			else{
// 				int rs1 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
// 				int rd = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand2().getValue());
// 				int immed = instruction.getDestinationOperand().getValue();
// 				switch(opType){
// 					case "beq":
// 						if(rs1==rd){
// 							aluResult = nowPc + immed;
// 							jmpRes = true;
// 							EX_IF_Latch.setIsBranch_enable(true,aluResult);
// 							}
// 						break;
// 					case "bgt":
// 						if(rs1>rd){
// 							aluResult = nowPc + immed;
// 							jmpRes = true;
// 							EX_IF_Latch.setIsBranch_enable(true,aluResult);
// 						}
// 						break;
// 					case "bne":
// 						if(rs1!=rd){
// 							aluResult = nowPc + immed;
// 							jmpRes = true;
// 							EX_IF_Latch.setIsBranch_enable(true,aluResult);
// 						}
// 						break;
// 					case "blt":
// 						if(rs1<rd){
// 							aluResult = nowPc + immed;
// 							jmpRes = true;
// 							EX_IF_Latch.setIsBranch_enable(true,aluResult);
// 						}
// 						break;
// 					default:
// 						System.out.print("Issue detected in R2I type, for branch statements");
// 						break;
// 				}
// 			}
// 			// System.out.println("At " + containingProcessor.getRegisterFile().getProgramCounter()+ " with " + aluResult + " and optype " + opType);
			
// 			EX_MA_Latch.setaluResult(aluResult);
// 		}
// 		if(jmpRes == true)
// 		{IF_OF_Latch.setcheck(true);}
// 		EX_MA_Latch.setMA_enable(true);
// 		}

// 	}}

package processor.pipeline;

import processor.Processor;

// import java.util.Arrays;

import generic.Instruction;
import generic.Instruction.OperationType;
// import generic.Operand;
import generic.Operand.OperandType;
import generic.Statistics;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch;
	IF_EnableLatchType IF_EnableLatch;

	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch,
			EX_IF_LatchType eX_IF_Latch, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch) {
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public void performEX() {
		boolean isBranch = false;
		if (OF_EX_Latch.getcheck()) {
			EX_MA_Latch.setcheck(true);
			OF_EX_Latch.setcheck(false);
			EX_MA_Latch.setInstruction(null);
		} else if (OF_EX_Latch.isEX_enable()) {
			Instruction instruction = OF_EX_Latch.getInstruction();
			EX_MA_Latch.setInstruction(instruction);
			OperationType op_type = instruction.getOperationType();
			String opType = op_type.toString();
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter() - 1;
			boolean b = opType.equals("addi") || opType.equals("subi") || opType.equals("muli") || opType.equals("divi") || opType.equals("andi") || opType.equals("ori") || opType.equals("xori") || opType.equals("slti") || opType.equals("slli") || opType.equals("srli") || opType.equals("srai") || opType.equals("load");
			if (op_type.equals(OperationType.beq) || op_type.equals(OperationType.blt) ||op_type.equals(OperationType.bgt) || op_type.equals(OperationType.bne) || op_type.equals(OperationType.jmp) || op_type.equals(OperationType.end)) {
				Statistics.setNumberOfBranchTaken(Statistics.getNumberOfBranchTaken() + 2);
				IF_EnableLatch.setIF_enable(false);
				IF_OF_Latch.setOF_enable(false);
				OF_EX_Latch.setEX_enable(false);
			}
			
			int alu_result = 0;

			if (opType.equals("add") || opType.equals("sub") || opType.equals("mul") || opType.equals("div") || opType.equals("and") || opType.equals("or") || opType.equals("xor") || opType.equals("slt") || opType.equals("sll") || opType.equals("srl") || opType.equals("sra")) {
				int op1 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand2().getValue());
				if (op_type == OperationType.add) {
					alu_result = op1 + op2;
				} else if (op_type == OperationType.mul) {
					alu_result = op1 * op2;
				} else if (op_type == OperationType.sub) {
					alu_result = op1 - op2;
				} else if (op_type == OperationType.div) {
					alu_result = op1 / op2;
					int remainder = op1 % op2;
					containingProcessor.getRegisterFile().setValue(31, remainder);
				} else if (op_type == OperationType.and) {
					alu_result = op1 & op2;
				} else if (op_type == OperationType.xor) {
					alu_result = op1 ^ op2;
				} else if (op_type == OperationType.or) {
					alu_result = op1 | op2;
				} else if (op_type == OperationType.slt) {
					alu_result = (op1 < op2) ? 1 : 0;
				} else if (op_type == OperationType.sll) {
					alu_result = op1 << op2;
				} else if (op_type == OperationType.srl) {
					alu_result = op1 >>> op2;
				} else if (op_type == OperationType.sra) {
					alu_result = op1 >> op2;
				} else {
				}
			} else if (b) {
				int op1 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
				int op2 = instruction.getSourceOperand2().getValue();

				if (op_type == OperationType.addi) {
					alu_result = op1 + op2;
				} else if (op_type == OperationType.subi) {
					alu_result = op1 - op2;
				} else if (op_type == OperationType.muli) {
					alu_result = op1 * op2;
				} else if (op_type == OperationType.divi) {
					alu_result = op1 / op2;
					int remainder = op1 % op2;
					containingProcessor.getRegisterFile().setValue(31, remainder);
				} else if (op_type == OperationType.andi) {
					alu_result = op1 & op2;
				} else if (op_type == OperationType.ori) {
					alu_result = op1 | op2;
				} else if (op_type == OperationType.xori) {
					alu_result = op1 ^ op2;
				} else if (op_type == OperationType.slti) {
					alu_result = (op1 < op2) ? 1 : 0;
				} else if (op_type == OperationType.slli) {
					alu_result = op1 << op2;
				} else if (op_type == OperationType.srli) {
					alu_result = op1 >>> op2;
				} else if (op_type == OperationType.srai) {
					alu_result = op1 >> op2;
				} else if (op_type == OperationType.load) {
					alu_result = op1 + op2;
				}
				
			}else if (op_type.equals(OperationType.jmp)) {
				OperandType optype = instruction.getDestinationOperand().getOperandType();
				int imm = 0;
				if (optype == OperandType.Register) {
					imm = containingProcessor.getRegisterFile()
							.getValue(instruction.getDestinationOperand().getValue());
				} else {
					imm = instruction.getDestinationOperand().getValue();
				}
				alu_result = imm + currentPC;
				EX_IF_Latch.setIsBranch_enable(true, alu_result);
			} 
			else if (op_type.equals(OperationType.store)) {
				int op1 = containingProcessor.getRegisterFile().getValue(instruction.getDestinationOperand().getValue());
				int op2 = instruction.getSourceOperand2().getValue();
				alu_result = op1 + op2;
			}  else if (op_type.equals(OperationType.beq)||op_type.equals(OperationType.bne)||op_type.equals(OperationType.blt)||op_type.equals(OperationType.bgt)) {
				int op2 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand2().getValue());
				int op1 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
				int imm = instruction.getDestinationOperand().getValue();
				if (op_type == OperationType.beq) {
					if (op1 == op2) {
						isBranch = true;
						alu_result = imm + currentPC;
						EX_IF_Latch.setIsBranch_enable(true, alu_result);
					}
				}else if (op_type == OperationType.blt) {
					if (op1 < op2) {
						isBranch = true;
						alu_result = imm + currentPC;
						EX_IF_Latch.setIsBranch_enable(true, alu_result);
					}
				}
				 else if (op_type == OperationType.bne) {
					if (op1 != op2) {
						isBranch = true;
						alu_result = imm + currentPC;
						EX_IF_Latch.setIsBranch_enable(true, alu_result);
					}
				}  else if (op_type == OperationType.bgt) {
					if (op1 > op2) {
						isBranch = true;
						alu_result = imm + currentPC;
						EX_IF_Latch.setIsBranch_enable(true, alu_result);
					}
				}
				
			}
			EX_MA_Latch.setaluResult(alu_result);
			EX_MA_Latch.setMA_enable(true);
			if(isBranch==true){
				IF_OF_Latch.setcheck(true);
			}
		}
	}
}