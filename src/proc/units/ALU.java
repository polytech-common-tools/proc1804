package proc.units;

import lombok.NonNull;
import proc.help.Flags;
import proc.help.Register3;
import proc.help.Register4;

/**
 * Arithmetic and Logic Unit
 * retrieve two operands
 */
public final class ALU {
    //Reference to outer object
    private final Flags flags;

    public ALU(@NonNull Flags flags) {
        if (flags == null) throw new IllegalArgumentException("Flags cannot be null");
        this.flags = flags;
    }

    /**
     * @param R    first operand
     * @param S    second operand
     * @param C0   carry
     * @param FUNC alu function
     * @return returns new instance of Register4 with the result of calculation
     */
    public Register4 calculate(@NonNull Register4 R, @NonNull Register4 S, boolean C0, @NonNull Register3 FUNC) {

        Register4 res;

        switch (FUNC.toString()) {
            case "000":
                res = R.add(S, C0, flags);
                break;
            case "001":
                res = S.sub(R, C0, flags);
                break;
            case "010":
                res = R.sub(S, C0, flags);
                break;
            case "011":
                res = R.or(S);
                res.setF3andZ(flags);
                break;
            case "100":
                res = R.and(S);
                res.setF3andZ(flags);
                break;
            case "101":
                res = R.not().and(S);
                res.setF3andZ(flags);
                break;
            case "110":
                res = R.xor(S);
                res.setF3andZ(flags);
                break;
            case "111":
                res = R.xor(S).not();
                res.setF3andZ(flags);
                break;
            default:
                throw new IllegalStateException("Unexpected state of OP");
        }
        return res;
    }
}