package proc.help;

import lombok.NonNull;

import java.util.Arrays;

public class Register4 extends AbstractRegister<Register4> {

    public Register4() {
        super(4);
    }

    @Override
    protected Register4 createInstance() {
        return new Register4();
    }

    public static Register4 valueOf(@NonNull String str) {
        if (str == null || str.length() != 4) throw new IllegalArgumentException("Input string length must be 4");
        var reg = new Register4();
        reg.regs = boolValueOf(str);
        return reg;
    }

    public static Register4 ones() {
        var reg = new Register4();
        Arrays.fill(reg.regs, true);
        return reg;
    }
}