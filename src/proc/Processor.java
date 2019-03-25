package proc;

import proc.help.Command;
import proc.help.Flags;
import proc.help.Register4;
import proc.help.Register8;
import proc.units.*;

/**
 * TODO
 */

public final class Processor {

    private ALU alu;
    private RegQUnit regQUnit;
    private RegistersMemoryUnit registersMemoryUnit;
    private AddressUnit addressUnit;
    private ProgramMemory programMemory;
    private Stack stack;
    private Flags flags;
    private MuxInputData muxInputData;
    private ControlUnit controlUnit;
    private Register4 F;
    private Register4 Y;
    //these two fields need only for debugging
    private int clkCounter;
    private Register8 currentAddress;

    /**
     * @param startAddress setting start address, program will start with this address
     *                     set null, to start with 0 address
     */
    public Processor(Register8 startAddress) {
        this.flags = new Flags();
        this.F = new Register4();
        this.Y = new Register4();
        this.alu = new ALU(flags);
        this.regQUnit = new RegQUnit();
        this.registersMemoryUnit = new RegistersMemoryUnit();
        this.programMemory = new ProgramMemory();
        this.muxInputData = new MuxInputData();
        this.stack = new Stack();
        if (startAddress == null) startAddress = new Register8();
        this.addressUnit = new AddressUnit(stack, startAddress);

        this.controlUnit = new ControlUnit(muxInputData, alu, regQUnit, registersMemoryUnit, F, Y);
        currentAddress = addressUnit.getNextAddress();
    }

    /**
     * Store program on the memory
     *
     * @param commands program commands
     */
    public void storeProgram(Command[] commands) {
        programMemory.storeCommands(commands);
    }

    /**
     */
    public void clk() {
        clkCounter++;
        final Command command = programMemory.loadCommand(addressUnit.getNextAddress()); //Read command
        currentAddress = addressUnit.getNextAddress().copy();
        addressUnit.countNextAddress(command.getMvAddr(), command.getMvType(), flags); //Count next address
        controlUnit.decodeAndDoEverything(command);
        flags.unlock();
    }
}