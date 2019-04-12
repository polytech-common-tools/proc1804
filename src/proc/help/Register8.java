package proc.help;

import lombok.NonNull;

public class Register8 extends AbstractRegister<Register8> {

    private static final int size = 8;

    public Register8() {
        super(size);
    }

    @Override
    protected Register8 createInstance() {
        return new Register8();
    }

    public Register4 left4Bits() {
        return Register4.valueOf(toString().substring(0, 4));
    }

    public Register4 right4Bits() {
        return Register4.valueOf(toString().substring(4, 8));
    }

    public static Register8 valueOf(@NonNull Register4 leftBits, @NonNull Register4 rightBits) {
        return Register8.valueOf(leftBits.toString() + rightBits.toString());
    }

    public static Register8 ones() {
        return AbstractRegister.ones(size);
    }
}
