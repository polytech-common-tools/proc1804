package proc.help;

import lombok.NonNull;

import java.util.Arrays;
import java.util.function.BiFunction;

/**
 * NOT thread-safe
 *
 * @param <T> must be the type that implements this abstract class
 */
public abstract class AbstractRegister<T extends AbstractRegister> {

    protected boolean regs[];

    protected AbstractRegister(int size) {
        if (size <= 0) throw new IllegalArgumentException("Size must be at least 1");
        regs = new boolean[size];
        Arrays.fill(regs, false);
    }

    protected abstract T createInstance();

    private enum LOGICAL_OP {AND, OR, XOR, NAND, NOR, XNOR}

    public T and(T other) {
        return logicalFunction(other, LOGICAL_OP.AND);
    }

    public T or(T other) {
        return logicalFunction(other, LOGICAL_OP.OR);
    }

    public T xor(T other) {
        return logicalFunction(other, LOGICAL_OP.XOR);
    }

    public T nand(T other) {
        return logicalFunction(other, LOGICAL_OP.NAND);
    }

    public T nor(T other) {
        return logicalFunction(other, LOGICAL_OP.NOR);
    }

    public T xnor(T other) {
        return logicalFunction(other, LOGICAL_OP.XNOR);
    }

    public T not() {
        boolean res[] = new boolean[regs.length];
        for (int i = 0; i < regs.length; i++) {
            res[i] = !regs[i];
        }
        T instance = createInstance();
        instance.regs = res;
        return instance;
    }

    private T logicalFunction(@NonNull T other, @NonNull LOGICAL_OP op) {
        int length = this.regs.length;
        if (length != other.regs.length)
            throw new IllegalArgumentException("Registers must have the same size");
        var res = new boolean[length];
        BiFunction<Boolean, Boolean, Boolean> function;

        switch (op) {
            case OR:
                function = (a, b) -> a || b;
                break;
            case AND:
                function = (a, b) -> a && b;
                break;
            case XOR:
                function = (a, b) -> a ^ b;
                break;
            case NOR:
                function = (a, b) -> !(a || b);
                break;
            case NAND:
                function = (a, b) -> !(a && b);
                break;
            case XNOR:
                function = (a, b) -> (a && b) || (!a && !b);
                break;
            default:
                function = (a, b) -> a;
        }

        for (int i = 0; i < length; i++) {
            res[i] = op(function, this.regs[i], other.regs[i]);
        }
        T instance = createInstance();
        instance.regs = res;
        return instance;
    }

    private static boolean op(@NonNull BiFunction<Boolean, Boolean, Boolean> function, boolean a, boolean b) {
        return function.apply(a, b);
    }

    public T shiftRight(int num, boolean insertValue) {
        if (num < 0) throw new IllegalArgumentException("Can't shift by negative value");
        var res = new boolean[regs.length];
        if (num >= regs.length) Arrays.fill(res, insertValue);
        else {
            System.arraycopy(this.regs, 0, res, num, regs.length - num);
            Arrays.fill(res, 0, num, insertValue);
        }
        T instance = createInstance();
        instance.regs = res;
        return instance;
    }

    public T shiftLeft(int num, boolean insertValue) {
        if (num < 0) throw new IllegalArgumentException("Can't shift by negative value");
        final int length = regs.length;
        var res = new boolean[length];
        if (num >= length) Arrays.fill(res, insertValue);
        else {
            System.arraycopy(this.regs, num, res, 0, length - num);
            Arrays.fill(res, length - num, length, insertValue);
        }
        T instance = createInstance();
        instance.regs = res;
        return instance;
    }

    /**
     * this + other + c_in
     *
     * @param other other operand
     * @param c_in  input carry
     * @param flags Flags object, all fields will be set
     * @return new instance of T with the result of add function
     */
    public T add(@NonNull T other, boolean c_in, Flags flags) {
        if (regs.length != other.regs.length) throw new IllegalArgumentException("Registers must have the same size");
        boolean c = c_in; //init carry
        T res = createInstance();
        final int l = regs.length;

        boolean z = true; //zero result flag
        boolean a, b;
        for (int i = 1; i <= l; i++) {
            a = this.regs[l - i];
            b = other.regs[l - i];
            res.regs[l - i] = a ^ b ^ c;
            c = (a ^ b) && c || a && b;
            if (res.regs[l - i]) z = false;
        }
        //Set flags
        if (flags != null) {
            boolean ovr = (this.regs[0] == other.regs[0]) & this.regs[0] != res.regs[0]; //overflow
            boolean f3 = res.regs[0]; //sign
            flags.set(ovr, c, f3, z);
        }
        return res;
    }

    /**
     * TODO need to check algorithm (in Flags c4 sets barrow value)
     * this - other - 1 + c_in
     *
     * @param other other operand
     * @param c_in  input carry
     * @param flags Flags object, all fields will be set
     * @return new instance of T with the result of subtraction
     */
    public T sub(@NonNull T other, boolean c_in, Flags flags) {
        if (regs.length != other.regs.length) throw new IllegalArgumentException("Registers must have the same size");
        boolean c = !c_in; //init barrow, if c_in = 1 than don't subtract 1, else do
        T res = createInstance();
        final int l = regs.length;

        boolean z = true; //zero result flag
        boolean a, b;
        for (int i = 1; i <= l; i++) {
            a = this.regs[l - i];
            b = other.regs[l - i];
            res.regs[l - i] = a ^ b ^ c;
            c = !a && (b ^ c) || b && c;
            if (res.regs[l - i]) z = false;
        }
        //Set flags
        if (flags != null) {
            boolean ovr = (this.regs[0] == other.regs[0]) & this.regs[0] != res.regs[0]; //overflow
            boolean f3 = res.regs[0]; //sign
            flags.set(ovr, c, f3, z);
        }
        return res;
    }

    public static <T extends AbstractRegister<T>> void setFlags(@NonNull T op1, @NonNull T op2,
                                                                @NonNull T res, @NonNull Flags flags) {
        boolean zero = true;
        for (boolean reg : res.regs) {
            if (reg) {
                zero = false;
                break;
            }
        }
        boolean ovr = (op1.regs[0] == op2.regs[0]) && res.regs[0];
        flags.set(ovr, flags.isC4(), res.regs[0], zero);
    }

    public void set(@NonNull T other) {
        if (this.regs.length != other.regs.length)
            throw new IllegalArgumentException("Registers must have the same length");
        System.arraycopy(other.regs, 0, this.regs, 0, this.regs.length);
    }

    public void increment(Flags flags) {
        this.regs = add(createInstance(), true, flags).regs;
    }

    public void decrement(Flags flags) {
        this.regs = sub(createInstance(), false, flags).regs;
    }

    public T reverse() {
        final int lastIndex = regs.length - 1;
        var res = new boolean[lastIndex + 1];
        for (int i = 0; i <= lastIndex; i++) {
            res[i] = regs[lastIndex - i];
        }
        T instance = createInstance();
        instance.regs = res;
        return instance;
    }

    public boolean getSign() {
        return regs[0];
    }

    public boolean getRigthBit() {
        return regs[size() - 1];
    }

    public int size() {
        return regs.length;
    }

    /**
     * @param value String of 0 and 1 without any separators
     * @return new instance of proc.help.AbstractRegister
     */
    protected static boolean[] boolValueOf(@NonNull String value) {
        final var chars = value.toCharArray();
        final int length = chars.length;
        var res = new boolean[length];
        for (int i = 0; i < length; i++) {
            if (chars[i] == '1') res[i] = true;
            else if (chars[i] == '0') ;
            else throw new IllegalArgumentException("Input string can contain only 0 and 1");
        }
        return res;
    }

    public long toLong() {
        long res = 0;
        int l = size();
        for (int i = 1; i <= l; i++) {
            if (regs[l - i]) res += Math.pow(2, i - 1);
        }
        return res;
    }

    //Returns copy of this object
    public T copy() {
        T inst = createInstance();
        System.arraycopy(this.regs, 0, inst.regs, 0, this.regs.length);
        return inst;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj instanceof AbstractRegister) {
            var other = (AbstractRegister) obj;
            for (int i = 0; i < regs.length; i++) {
                if (this.regs[i] != other.regs[i]) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        var stringBuilder = new StringBuilder();
        for (boolean reg : regs) {
            if (reg) stringBuilder.append(1);
            else stringBuilder.append(0);
        }
        return stringBuilder.toString();
    }
}