package proc.help;

public class Register extends AbstractRegister<Register> {

    public Register(int size) {
        super(size);
    }

    @Override
    protected Register createInstance() {
        return new Register(regs.length);
    }
}