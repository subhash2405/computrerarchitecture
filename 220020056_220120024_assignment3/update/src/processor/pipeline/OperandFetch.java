package processor.pipeline;

import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Statistics;
import generic.Operand.OperandType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	    public String twosComplement(String bin) {
        StringBuilder onesBuilder = new StringBuilder();
        StringBuilder twosBuilder = new StringBuilder();
        boolean carry = true;

        // Calculate ones complement
        for (int i = 0; i < bin.length(); i++) {
            char bit = bin.charAt(i);
            onesBuilder.append((bit == '1') ? '0' : '1');
        }

        // Calculate twos complement
        for (int i = bin.length() - 1; i >= 0; i--) {
            char bit = onesBuilder.charAt(i);
            if (bit == '1' && carry) {
                twosBuilder.insert(0, '0'); // Add 0 when there's a carry
            } else if (bit == '0' && carry) {
                twosBuilder.insert(0, '1'); // Add 1 when there's no carry
                carry = false; // Reset carry flag
            } else {
                twosBuilder.insert(0, bit); // Add the remaining bits
            }
        }

        return twosBuilder.toString();
    }
	
	public boolean hazardflag(Instruction instruction, int r1, int r2)
	{
		if(instruction!=null && instruction.getOperationType()!=null){
			int instNumber =instruction.getOperationType().ordinal();
			if ((instNumber <= 21 && instNumber % 2 == 0) || (instNumber <= 21 && instNumber % 2 != 0) || instNumber == 22 || instNumber == 23) {
				int dest_reg = instruction != null ? instruction.getDestinationOperand().getValue() : -1;
				if (r1 == dest_reg || r2 == dest_reg) {
					return true;
				}
			} 
			else if((instNumber == 6 || instNumber ==7)&&(r1 == 31)||r2 == 31){
				return true;
			}
		}
		return false;
		}

	
	public void PCchanger() {
		System.out.println("Possible Hazard!");
		OF_EX_Latch.setcheck(true);
		IF_EnableLatch.setIF_enable(false);
	}
 	
	public void performOF() {
		if (IF_OF_Latch.isOF_enable()) {
			Statistics.setNumberOfOFInstructions(Statistics.getNumberOfOFInstructions() + 1);
			OperationType[] operationType = OperationType.values();
			String instruction = Integer.toBinaryString(IF_OF_Latch.getInstruction());
			System.out.println("OF is enabled with instruction: " + instruction + "..");
			while (instruction.length() != 32) {
				instruction = "0" + instruction;
			}
			String opcode = instruction.substring(0, 5);
			int type_operation = Integer.parseInt(opcode, 2);
			OperationType operation = operationType[type_operation];
			
			switch (operation.ordinal()) {
				case 24:
				case 25:
				case 26:
				case 27:
				case 28:
					IF_EnableLatch.setIF_enable(false);
					break;
				default:
					break;
			}
			
			boolean hazard_inst = false;
			Instruction instruction_ex_stage = OF_EX_Latch.getInstruction();
			Instruction instruction_rw_stage = MA_RW_Latch.getInstruction();
			Instruction instruction_ma_stage = EX_MA_Latch.getInstruction();
			Instruction inst = new Instruction();
			switch (operation) {
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
				int registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
				rs1.setValue(registerNo);

				Operand rs2 = new Operand();
				rs2.setOperandType(OperandType.Register);
				int registerNo2 = Integer.parseInt(instruction.substring(10, 15), 2);
				rs2.setValue(registerNo2);
				if (hazardflag(instruction_ex_stage, registerNo, registerNo2))
					hazard_inst= true;
				if (hazardflag(instruction_ma_stage, registerNo, registerNo2))
					hazard_inst = true;
				if (hazardflag(instruction_rw_stage, registerNo, registerNo2))
					hazard_inst = true;
				if (hazard_inst) {
					this.PCchanger();
					break;
				}

				Operand rd = new Operand();
				rd.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(instruction.substring(15, 20), 2);
				rd.setValue(registerNo);

				inst.setSourceOperand1(rs1);
				inst.setOperationType(operationType[type_operation]);
				inst.setDestinationOperand(rd);
				inst.setSourceOperand2(rs2);
				break;
			case end:
				inst.setOperationType(operationType[type_operation]);
				IF_EnableLatch.setIF_enable(false);
				break;
			case jmp:
				Operand op = new Operand();
				String imm = instruction.substring(10, 32);
				int imm_val = Integer.parseInt(imm, 2);
				if (imm.charAt(0) == '1') {
					imm = twosComplement(imm);
					imm_val = Integer.parseInt(imm, 2) * -1;
				}
				if (imm_val != 0) {
					op.setOperandType(OperandType.Immediate);
					op.setValue(imm_val);
				} else {
					int registerno = Integer.parseInt(instruction.substring(5, 10), 2);
					op.setOperandType(OperandType.Register);
					op.setValue(registerno);
				}

				inst.setDestinationOperand(op);
				inst.setOperationType(operationType[type_operation]);
				break;

			case beq:
			case bne:
			case blt:
			case bgt:
				rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				int RegisterNo = Integer.parseInt(instruction.substring(5, 10), 2);
				rs1.setValue(RegisterNo);
				
				// destination register
				rs2 = new Operand();
				rs2.setOperandType(OperandType.Register);
				int RegisterNo2 = Integer.parseInt(instruction.substring(10, 15), 2);
				rs2.setValue(RegisterNo2);
				
				if (hazardflag(instruction_ex_stage, RegisterNo, RegisterNo2))
					hazard_inst = true;
				if (hazardflag(instruction_ma_stage, RegisterNo, RegisterNo2))
					hazard_inst = true;
				if (hazardflag(instruction_rw_stage, RegisterNo, RegisterNo2))
					hazard_inst = true;
				if (hazard_inst) {
					this.PCchanger();
					break;
				}
				rd = new Operand();
				rd.setOperandType(OperandType.Immediate);
				imm = instruction.substring(15, 32);
				imm_val = Integer.parseInt(imm, 2);
				if (imm.charAt(0) == '1') {
					imm = twosComplement(imm);
					imm_val = Integer.parseInt(imm, 2) * -1;
				}
				rd.setValue(imm_val);
				
				inst.setSourceOperand1(rs1);
				inst.setOperationType(operationType[type_operation]);
				inst.setDestinationOperand(rd);
				inst.setSourceOperand2(rs2);
				break;

			default:
				rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
				rs1.setValue(registerNo);
				if (hazardflag(instruction_ex_stage, registerNo, registerNo)) {
					hazard_inst = true;
				}	
				if (hazardflag(instruction_ma_stage, registerNo, registerNo)) {
					hazard_inst = true;
				}
				if (hazardflag(instruction_rw_stage, registerNo, registerNo)) {
					hazard_inst = true;
				}
					
				if (hazard_inst) {
					this.PCchanger();
					break;
				}

				// Destination register
				rd = new Operand();
				rd.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(instruction.substring(10, 15), 2);
				rd.setValue(registerNo);

				// Immediate values
				rs2 = new Operand();
				rs2.setOperandType(OperandType.Immediate);
				imm = instruction.substring(15, 32);
				imm_val = Integer.parseInt(imm, 2);
				if (imm.charAt(0) == '1') {
					imm = twosComplement(imm);
					imm_val = Integer.parseInt(imm, 2) * -1;
				}
				rs2.setValue(imm_val);
				inst.setOperationType(operationType[type_operation]);
				inst.setSourceOperand2(rs2);
				inst.setSourceOperand1(rs1);
				inst.setDestinationOperand(rd);
				break;
			}
			OF_EX_Latch.setInstruction(inst);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}