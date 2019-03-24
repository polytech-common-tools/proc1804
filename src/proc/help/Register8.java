package proc.help;

import lombok.NonNull;

import java.util.Arrays;

public class Register8 extends AbstractRegister<Register8> {

    public Register8() {
        super(8);
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

    public static Register8 valueOf(@NonNull String str) {
        if (str == null || str.length() != 8) throw new IllegalArgumentException("Input string length must be 4");
        var reg = new Register8();
        reg.regs = boolValueOf(str);
        return reg;
    }

    public static Register8 ones() {
        var reg = new Register8();
        Arrays.fill(reg.regs, true);
        return reg;
    }

    public static Register8 valueOf(@NonNull Register4 leftBits, @NonNull Register4 rightBits) {
        return Register8.valueOf(leftBits.toString() + rightBits.toString());
    }
}
