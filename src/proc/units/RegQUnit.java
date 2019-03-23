package proc.units;

import lombok.NonNull;
import proc.help.Register4;

//Unit just to store a register
public final class RegQUnit {
    private Register4 Q = new Register4();

    public Register4 loadQ() {
        return Q.copy();
    }

    public void storeQ(@NonNull final Register4 Q) {
        this.Q = Q.copy();
    }
}