package proc.help;

import lombok.*;

@Getter
@NoArgsConstructor
public final class Flags {
    private boolean OVR; //overflow
    private boolean C4; //carry flag
    private boolean F3; //sign of the result, the last bit
    private boolean Z; //zero flag, set 1 if the result is zero
    private boolean lock;

    public Flags(boolean OVR, boolean c4, boolean f3, boolean z) {
        this.OVR = OVR;
        C4 = c4;
        F3 = f3;
        Z = z;
    }

    public void setFlags(boolean OVR, boolean C4, boolean F3, boolean Z) {
        if (lock) return;
        this.OVR = OVR;
        this.C4 = C4;
        this.F3 = F3;
        this.Z = Z;
    }

    public void lock() {
        lock = true;
    }

    public void unlock() {
        lock = false;
    }

    public Flags copy() {
        return new Flags(OVR, C4, F3, Z);
    }

    @Override
    public int hashCode() {
        int result = 0;
        result += 31 * result + (OVR ? 1 : 0);
        result += 31 * result + (C4 ? 1 : 0);
        result += 31 * result + (F3 ? 1 : 0);
        result += 31 * result + (Z ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj instanceof Flags) {
            var other = (Flags) obj;
            return (OVR == other.OVR)
                    && (C4 == other.C4)
                    && (F3 == other.F3)
                    && (Z == other.Z);
        }
        return false;
    }

    @Override
    public String toString() {
        final var stringBuilder = new StringBuilder();
        stringBuilder.append("OVR: ");
        stringBuilder.append(OVR ? "1" : "0");
        stringBuilder.append(", C4: ");
        stringBuilder.append(C4 ? "1" : "0");
        stringBuilder.append(", F3: ");
        stringBuilder.append(F3 ? "1" : "0");
        stringBuilder.append(", Z: ");
        stringBuilder.append(OVR ? "1" : "0");
        return stringBuilder.toString();
    }
}