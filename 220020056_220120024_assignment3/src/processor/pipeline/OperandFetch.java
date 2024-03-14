package processor.pipeline;

import java.io.File;

import generic.Instruction;
import generic.Operand;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;
import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	RegisterFile RegisterFile;
	IF_EnableLatchType IF_EnableLatch;

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch,RegisterFile file,IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.RegisterFile = file;
		this.IF_EnableLatch=iF_EnableLatch;
	}
	public String twoscomplement(String bin) {
		String ones = "";
		for (int i = 0; i < bin.length(); i++) {
			if (bin.charAt(i) == '1') {
				ones += '0';
			} else {
				ones += '1';
			}
		}
		
		String twos = "";
		boolean carry = true; 
		for (int i = bin.length() - 1; i >= 0; i--) {
			if (ones.charAt(i) == '1' && carry) {
				twos = '0' + twos; // Add 0 when there's a carry
			} else if (ones.charAt(i) == '0' && carry) {
				twos = '1' + twos; // Add 1 when there's no carry
				carry = false; // Reset carry flag
			} else {
				twos = ones.charAt(i) + twos; // Add the remaining bits
			}
		}
		return twos;
	}
	public void performOF() 
	{
		if(IF_OF_Latch.isOF_enable())
		{
			//TOD
			Instruction newinst = new Instruction();
			String inst = Integer.toBinaryString(IF_OF_Latch.getInstruction());
			int csize = inst.length();
			csize = 32-csize;
			for(int i=1;i<=csize;i++)
				inst = '0' + inst;
			String opCode = inst.substring(0,5);
			OperationType[] operations = OperationType.values();
			OperationType operation = operations[Integer.parseInt(opCode,2)];
			newinst.setOperationType(operation);
			
			switch(operation){
				case add:
				case sub:
				case mul:
				case div:
				case and:
				case or:
				case xor:
				case slt:
				case sll:
				case srl:
				case sra:
						Operand rs1 = new Operand();
						rs1.setOperandType(OperandType.Register);
						Operand rs2 = new Operand();
						rs2.setOperandType(OperandType.Register);
						Operand rd = new Operand();
						rd.setOperandType(OperandType.Register);
						if(RegisterFile.getintregister(Integer.parseInt(inst.substring(5, 10),2))==false && RegisterFile.getintregister(Integer.parseInt(inst.substring(10, 15),2))==false)
						{
							rs1.setValue(Integer.parseInt(inst.substring(5, 10),2));
							rs2.setValue(Integer.parseInt(inst.substring(10, 15),2));
							newinst.setSourceOperand1(rs1);
							newinst.setSourceOperand2(rs2);
						}
						else
						{
							int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
							containingProcessor.getRegisterFile().setProgramCounter(currentPC - 1);
						}
						int index = Integer.parseInt(inst.substring(15, 20),2);
						if(RegisterFile.getintregister(index)==false)
						{
							rd.setValue(Integer.parseInt(inst.substring(15, 20),2));
							newinst.setDestinationOperand(rd);
							RegisterFile.setintregister(index);
						}
						break;
				case addi:
				case andi:
				case muli:
				case ori:
				case slli:
				case slti:
				case srai:
				case srli:
				case subi:
				case xori:
				case divi:
				case store:
				case load:
						Operand rsi1 = new Operand();
						rsi1.setOperandType(OperandType.Register);
						Operand rdi = new Operand();
						rdi.setOperandType(OperandType.Register);
						rsi1.setValue(Integer.parseInt(inst.substring(5, 10),2));
						rdi.setValue(Integer.parseInt(inst.substring(10, 15),2));
						Operand imm = new Operand();
						imm.setOperandType(OperandType.Immediate);
						String immd = inst.substring(15,32);
						if(immd.charAt(0)=='1')
						{
							immd = twoscomplement(immd);
							imm.setValue(-1*Integer.parseInt(immd,2));
						}
						else
						{
							imm.setValue(Integer.parseInt(immd,2));
						}
						newinst.setSourceOperand1(rsi1);
						newinst.setSourceOperand2(imm);
						newinst.setDestinationOperand(rdi);
						break;
				case jmp:
						Operand rdj = new Operand();
						rdj.setOperandType(OperandType.Register);
						Operand immj = new Operand();
						immj.setOperandType(OperandType.Immediate);
						rdj.setValue(Integer.parseInt(inst.substring(5, 10),2));
						String imme = inst.substring(10,32);
						if(imme.charAt(0)=='1')
						{
							imme = twoscomplement(imme);
							immj.setValue(-1*Integer.parseInt(imme,2));
						}
						else
						{
							immj.setValue(Integer.parseInt(imme,2));
						}
						newinst.setDestinationOperand(immj);
						newinst.setSourceOperand1(rdj);
						break;
				case end:
						break;
				case beq:
				case bgt:
				case blt:
				case bne:
						Operand rsb = new Operand();
						rsb.setOperandType(OperandType.Register);
						Operand rdb = new Operand();
						rdb.setOperandType(OperandType.Register);
						Operand immb = new Operand();
						immb.setOperandType(OperandType.Immediate);
						rsb.setValue(Integer.parseInt(inst.substring(5, 10),2));
						rdb.setValue(Integer.parseInt(inst.substring(10, 15),2));
						String imm1 = inst.substring(15,32);
						if(imm1.charAt(0)=='1')
						{
							imm1 = twoscomplement(imm1);
							immb.setValue(-1*Integer.parseInt(imm1,2));
						}
						else
						{
							immb.setValue(Integer.parseInt(imm1,2));
						}
						newinst.setSourceOperand1(rsb);
						newinst.setSourceOperand2(rdb);
						newinst.setDestinationOperand(immb);
						break;
				default:
						break;						
			}
			OF_EX_Latch.setInstruction(newinst);			
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}


