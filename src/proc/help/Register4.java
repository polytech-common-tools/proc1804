package proc.help;

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
        return AbstractRegister.ones(size);
    }
}