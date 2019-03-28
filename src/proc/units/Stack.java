package proc.units;

import lombok.NonNull;
import proc.help.Register8;

public final class Stack {
    private final static short SIZE = 4;

    //Stack pointer
    private int sp = -1;

    private Register8 stack[];

    public Stack() {
        stack = new Register8[4];
        for (int i = 0; i < stack.length; i++) {
            stack[i] = new Register8();
        }
    }

    public void push(@NonNull final Register8 returnAddress) {
        if (sp < SIZE - 1) sp++;
        stack[sp] = returnAddress.copy();
    }

    /**
     * @return a copy of the last address
     */
    public Register8 pop() {
        if (sp < 0) return stack[0].copy();
        final Register8 addr = stack[sp].copy();
        if (sp > 0) sp--;
        return addr;
    }
}