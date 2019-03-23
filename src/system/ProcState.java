package system;

import lombok.Getter;
import lombok.NonNull;
import proc.Processor;
import proc.help.Flags;
import proc.help.Register4;
import proc.help.Register8;
import proc.units.AddressUnit;
import proc.units.RegQUnit;
import proc.units.RegistersMemoryUnit;
import proc.units.Stack;

import java.lang.reflect.Field;

@Getter
public final class ProcState {
    private Register4 Y;
    private Register4 F;
    private Register4 Q;
    private Register8 nextAddress;
    private Flags flags;
    private Register4 memory[];
    private Stack stack;
    private int sp;
    private Register8[] stackArray;
    private int clkCounter;

    private ProcState() {
    }

    public static ProcState of(@NonNull Processor processor) throws NoSuchFieldException, IllegalAccessException {
        final ProcState state = new ProcState();

        final Class clazz = processor.getClass();

        final Field f = clazz.getDeclaredField("F");
        f.setAccessible(true);
        state.F = ((Register4) f.get(processor)).copy();

        final Field y = clazz.getDeclaredField("Y");
        y.setAccessible(true);
        state.Y = ((Register4) y.get(processor)).copy();

        final Field regQUnitReference = clazz.getDeclaredField("regQUnit");
        regQUnitReference.setAccessible(true);
        final RegQUnit regQUnit = (RegQUnit) regQUnitReference.get(processor);
        final Field q = regQUnit.getClass().getDeclaredField("Q");
        q.setAccessible(true);
        state.Q = ((Register4) q.get(regQUnit)).copy();

        final Field addressUnitField = clazz.getDeclaredField("addressUnit");
        addressUnitField.setAccessible(true);
        final AddressUnit addressUnit = (AddressUnit) addressUnitField.get(processor);
        final Field nextAddress = addressUnit.getClass().getDeclaredField("nextAddress");
        nextAddress.setAccessible(true);
        state.nextAddress = (Register8) nextAddress.get(addressUnit);

        final Field flags = clazz.getDeclaredField("flags");
        flags.setAccessible(true);
        state.flags = ((Flags) flags.get(processor)).copy();

        final Field memory = clazz.getDeclaredField("registersMemoryUnit");
        memory.setAccessible(true);
        final RegistersMemoryUnit registersMemoryUnit = (RegistersMemoryUnit) memory.get(processor);
        final Field regsField = registersMemoryUnit.getClass().getDeclaredField("regs");
        regsField.setAccessible(true);
        final Register4 regs[] = (Register4[]) regsField.get(registersMemoryUnit);
        state.memory = new Register4[regs.length];
        System.arraycopy(regs, 0, state.memory, 0, regs.length);

        final Field stackField = clazz.getDeclaredField("stack");
        stackField.setAccessible(true);

        final Stack stack = (Stack) stackField.get(processor);
        final Field spField = stack.getClass().getDeclaredField("sp");
        spField.setAccessible(true);
        final Field stackArrayField = stack.getClass().getDeclaredField("stack");
        stackArrayField.setAccessible(true);
        final int sp = (Integer) spField.get(stack);
        final Register8[] stackArray = (Register8[]) stackArrayField.get(stack);

        state.sp = sp;
        state.stackArray = stackArray;

        final Stack newStack = new Stack();
        spField.set(newStack, sp);
        stackArrayField.set(newStack, stackArray);

        final Field clkCounterField = clazz.getDeclaredField("clkCounter");
        clkCounterField.setAccessible(true);
        state.clkCounter = (Integer) clkCounterField.get(processor);

        state.stack = newStack;
        return state;
    }
}
