package proc.units;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import proc.help.*;

/**
 * TODO NEED TO SIMPLIFY
 */

@RequiredArgsConstructor
public final class ControlUnit {
    @NonNull //Reference to outer object
    private MuxInputData muxInputData;
    @NonNull //Reference to outer object
    private ALU alu;
    @NonNull //Reference to outer object
    private RegQUnit regQUnit;
    @NonNull //Reference to outer object
    private RegistersMemoryUnit registersMemoryUnit;
    @NonNull //Reference to outer object
    private Register4 F;
    @NonNull //Reference to outer object
    private Register4 Y;

    public void decodeAndDoEverything(@NonNull Command command) {
        final Register3 func = command.getFunc();
        //Get values from memory
        final Register4 a = registersMemoryUnit.load(command.getA());
        final Register4 b = registersMemoryUnit.load(command.getB());
        muxInputData.mux(command.getD(),
                a,
                b,
                regQUnit.loadQ(),
                command.getSrc());
        Register4 R = muxInputData.getR();
        Register4 S = muxInputData.getS();
        F.set(alu.calculate(R, S, command.isC0(), func));

        decodeDestination(command);
    }

    private void decodeDestination(@NonNull Command command) {
        boolean ms1 = command.isMS1();
        boolean ms2 = command.isMS2();
        switch (command.getDest().toString()) {
            case "000": // F -> Q, F -> Y
                Y.set(F);
                regQUnit.storeQ(F);
                break;
            case "001": // F -> Y
                Y.set(F);
                break;
            case "010": // F -> B, A -> Y
                Y.set(command.getA());
                registersMemoryUnit.save(command.getB(), F);
                break;
            case "011": // F -> B, F -> Y
                Y.set(F);
                registersMemoryUnit.save(command.getB(), F);
                break;
            //TODO NOT SURE ABOUT NEXT ACTIONS
            //STORE F IN Y AFTER SHIFTING OT BEFORE?
            case "100": // F/2 -> B, Q/2 -> Q, F -> Y
                Y.set(F);
                shift(ms1, ms2, true, true);
                registersMemoryUnit.save(command.getB(), F);
                break;
            case "101": // F/2 -> B, F -> Y
                Y.set(F);
                shift(ms1, ms2, true, false);
                registersMemoryUnit.save(command.getB(), F);
                break;
            case "110": // 2F -> B, 2Q -> Q, F -> Y
                Y.set(F);
                shift(ms1, ms2, false, true);
                registersMemoryUnit.save(command.getB(), F);
                break;
            case "111": // 2F -> B, F -> Y
                Y.set(F);
                shift(ms1, ms2, false, false);
                registersMemoryUnit.save(command.getB(), F);
                break;
            default:
                throw new IllegalStateException();
        }
    }


    /**
     * @param MS1     bit
     * @param MS2     bit
     * @param toRight if true shift to right, else - to left
     */
    private void shift(boolean MS1, boolean MS2, boolean toRight, boolean storeQ) {
        if (MS1 && MS2) { // 11
            //combine F and Q
            Register8 tempReg = Register8.valueOf(F, regQUnit.loadQ());
            if (toRight) {
                Shifter.shift(tempReg, Shifter.Type.ARITHM_RIGHT);
            } else {
                Shifter.shift(tempReg, Shifter.Type.ARITHM_LEFT);
            }
            //split tempReg into F and Q
            Y.set(tempReg.left4Bits());
            if (storeQ) regQUnit.storeQ(tempReg.riht4Bits());
        } else if (MS1 && !MS2) { //10
            //combine F and Q
            Register8 tempReg = Register8.valueOf(F, regQUnit.loadQ());
            if (toRight) {
                Shifter.shift(tempReg, Shifter.Type.CYCLIC_RIGHT);
            } else {
                Shifter.shift(tempReg, Shifter.Type.CYCLIC_LEFT);
            }
            //split tempReg into F and Q
            Y.set(tempReg.left4Bits());
            if (storeQ) regQUnit.storeQ(tempReg.riht4Bits());
        } else if (!MS2 && MS1) { //01
            Register4 Q = regQUnit.loadQ();
            if (toRight) {
                Shifter.shift(Y, Shifter.Type.CYCLIC_RIGHT);
                Shifter.shift(Q, Shifter.Type.CYCLIC_RIGHT);
            } else {
                Shifter.shift(Y, Shifter.Type.CYCLIC_LEFT);
                Shifter.shift(Q, Shifter.Type.CYCLIC_LEFT);
            }
            if (storeQ) regQUnit.storeQ(Q);
        } else { //00
            Register4 Q = regQUnit.loadQ();
            if (toRight) {
                Shifter.shift(Y, Shifter.Type.RIGHT);
                Shifter.shift(Q, Shifter.Type.RIGHT);
            } else {
                Shifter.shift(Y, Shifter.Type.LEFT);
                Shifter.shift(Q, Shifter.Type.LEFT);
            }
            if (storeQ) regQUnit.storeQ(Q);
        }
    }
}