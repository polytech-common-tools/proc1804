package system;

import java.util.*;

/**
 * REGS and REG<INDEX>
 * FLAGS and FLAG<INDEX>
 * STACKS and STACK<INDEX> cannot be used together
 *
 * N for clk number
 */
public enum PrintableValue {
    COMMENT, N, Y, F, Q, NEXT, ADDR, FLAGS, OVR, C4, F3, Z,
    REGS, REG0, REG1, REG2, REG3, REG4, REG5, REG6, REG7, REG8, REG9, REG10, REG11, REG12, REG13, REG14, REG15,
    STACKS, STACK0, STACK1, STACK2, STACK3,
    SP;

    public boolean isSpecificReg() {
        return this.ordinal() >= REG0.ordinal() && this.ordinal() <= REG15.ordinal();
    }

    public boolean isSpecificStack() {
        return this.ordinal() >= STACK0.ordinal() && this.ordinal() <= STACK3.ordinal();
    }

    public boolean isSpecificFlag() {
        return this == OVR || this == C4 || this == F3 || this == Z;
    }

    public static List<PrintableValue> getAllRegs() {
        var regs = new ArrayList<PrintableValue>();
        for (int i = REG0.ordinal(); i <= REG15.ordinal(); i++) {
            regs.add(PrintableValue.values()[i]);
        }
        return regs;
    }

    public static List<PrintableValue> getAllStacks() {
        return List.of(STACK0, STACK1, STACK2, STACK3);
    }

    public static List<PrintableValue> getAllFlags() {
        return List.of(OVR, C4, F3, Z);
    }
}