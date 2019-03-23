package proc.units;

import lombok.Getter;
import lombok.NonNull;
import proc.help.Register3;
import proc.help.Register4;

/**
 * This class stores R and S registers
 */
@Getter //generate getters for R and S
public final class MuxInputData {
    private Register4 R = new Register4();
    private Register4 S = new Register4();

    /**
     * @param D   data type
     * @param A   operand A (not address, value)
     * @param B   operand B (not address, value)
     * @param Q   register Q
     * @param SRC what operands have to be R and S
     */
    public void mux(@NonNull Register4 D,
                    @NonNull Register4 A,
                    @NonNull Register4 B,
                    @NonNull Register4 Q,
                    @NonNull Register3 SRC) {
        final var zero = new Register4();
        switch (SRC.toString()) {
            case "000":
                R = A;
                S = Q;
                break;
            case "001":
                R = A;
                S = B;
                break;
            case "010":
                R = zero;
                S = Q;
                break;
            case "011":
                R = zero;
                S = B;
                break;
            case "100":
                R = zero;
                S = A;
                break;
            case "101":
                R = D;
                S = A;
                break;
            case "110":
                R = D;
                S = Q;
                break;
            case "111":
                R = D;
                S = zero;
                break;
            default:
                throw new IllegalStateException("Unexpected state of SRC");
        }
    }
}