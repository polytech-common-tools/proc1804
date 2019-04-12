package proc.help;

import lombok.NonNull;

public class Register3 extends AbstractRegister<Register3> {

    private static final int size = 3;

    public Register3() {
        super(size);
    }

    @Override
    protected Register3 createInstance() {
        return new Register3();
    }

    public static Register3 ones() {
        return AbstractRegister.ones(Register3.class);
    }

    public static Register3 valueOf(@NonNull String str) {
        return AbstractRegister.valueOf(str, Register3.class);
    }
}