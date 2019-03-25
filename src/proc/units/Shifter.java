package proc.units;

import lombok.NonNull;
import proc.help.AbstractRegister;

/**
 * This class shifts input value, not create new instance!
 */
public final class Shifter {

    public enum Type {LEFT, RIGHT, CYCLIC_LEFT, CYCLIC_RIGHT, ARITHM_LEFT, ARITHM_RIGHT}

    public static void shift(@NonNull AbstractRegister register, @NonNull Type type) {
        switch (type) {
            case LEFT:
                shiftLeft(register);
                break;
            case RIGHT:
                shiftRight(register);
                break;
            case CYCLIC_LEFT:
                shiftLeftCyclic(register);
                break;
            case CYCLIC_RIGHT:
                shiftRightCyclic(register);
                break;
            case ARITHM_LEFT:
                arithmShiftLeft(register);
                break;
            case ARITHM_RIGHT:
                arithmShiftRight(register);
        }
    }

    private static void shiftRight(AbstractRegister register) {
        register.set(register.shiftRight(1, false));
    }

    private static void shiftLeft(AbstractRegister register) {
        register.set(register.shiftLeft(1, false));
    }

    private static void shiftRightCyclic(AbstractRegister register) {
        register.set(register.shiftRight(1, register.getRigthBit()));
    }

    private static void shiftLeftCyclic(AbstractRegister register) {
        register.set(register.shiftLeft(1, register.getSign()));
    }

    private static void arithmShiftRight(AbstractRegister register) {
        register.set(register.shiftRight(1, register.getSign()));
    }

    /**
     * THIS SHIFTING IS INCORRECT
     * BUT EMULATOR IN LABORATORY IN FUCKING POLYTECH
     * DOES IT THIS WAY
     * @param register
     */
    private static void arithmShiftLeft(AbstractRegister register) {
        register.set(register.shiftLeft(1, true));
    }
}