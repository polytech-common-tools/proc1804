package proc.help;

import lombok.NonNull;

import java.util.Arrays;

public class Register extends AbstractRegister<Register> {

    public Register(int size) {
        super(size);
    }

    @Override
    protected Register createInstance() {
        return new Register(regs.length);
    }

    public static Register valueOf(@NonNull String str) {
        var reg = new Register(str.length());
        reg.regs = boolValueOf(str);
        return reg;
    }

    public static Register ones(int size) {
        var reg = new Register(size);
        Arrays.fill(reg.regs, true);
        return reg;
    }
}