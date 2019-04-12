package proc.help;

import lombok.NonNull;

public class Register4 extends AbstractRegister<Register4> {

    private static final int size = 4;

    public Register4() {
        super(size);
    }

    @Override
    protected Register4 createInstance() {
        return new Register4();
    }

    public static Register4 ones() {
        return AbstractRegister.ones(Register4.class);
    }

    public static Register4 valueOf(@NonNull String str) {
        return AbstractRegister.valueOf(str, Register4.class);
    }
}