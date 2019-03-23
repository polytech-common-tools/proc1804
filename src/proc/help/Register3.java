package proc.help;

import lombok.NonNull;

import java.util.Arrays;

public class Register3 extends AbstractRegister<Register3> {

    public Register3() {
        super(3);
    }

    @Override
    protected Register3 createInstance() {
        return new Register3();
    }

    public static Register3 valueOf(@NonNull String str) {
        if (str == null || str.length() != 3) throw new IllegalArgumentException("Input string length must be 3");
        var reg = new Register3();
        reg.regs = boolValueOf(str);
        return reg;
    }

    public static Register3 ones() {
        var reg = new Register3();
        Arrays.fill(reg.regs, true);
        return reg;
    }
}