package generic;

import processor.Clock;
import processor.Processor;
import java.util.*;
import java.io.*;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */
		try{
			FileInputStream file = new FileInputStream(assemblyProgramFile);
			DataInputStream line = new DataInputStream(file);
			int next = 0;
			if(line.available()>0){
				next=line.readInt();
				processor.getRegisterFile().setProgramCounter(next);
			}
			for(int i=0;line.available()>0;i++){
				next=line.readInt();
				processor.getMainMemory().setWord(i,next);
			}

			processor.getRegisterFile().setValue(0,0);
			processor.getRegisterFile().setValue(1,65535);
			processor.getRegisterFile().setValue(2,65535);
			line.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}

	}
	
	public static void simulate()
	{
		int numinst=0;
		int numcycles=0;
		while(simulationComplete == false)
		{
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			processor.getOFUnit().performOF();
			Clock.incrementClock();
			processor.getEXUnit().performEX();
			Clock.incrementClock();
			processor.getMAUnit().performMA();
			Clock.incrementClock();
			processor.getRWUnit().performRW();
			Clock.incrementClock();
			numinst+=1;
			numcycles+=1;
		}
		
		// TODO
		// set statistics
		Statistics.setNumberOfInstructions(numinst);
		Statistics.setNumberOfCycles(numcycles);
		// Statistics.setCPI(numinst, numcycles);

	}

	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
