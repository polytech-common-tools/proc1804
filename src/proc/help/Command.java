package proc.help;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * The most ugly class in the Project
 */

@Getter
@Builder
public class Command {
    @NonNull
    @Builder.Default
    private Register8 mvAddr = new Register8();
    @NonNull
    @Builder.Default
    private Register4 mvType = new Register4();
    private boolean MS2;
    @NonNull
    @Builder.Default
    private Register3 dest = new Register3();
    private boolean MS1;
    @NonNull
    @Builder.Default
    private Register3 src = new Register3();
    private boolean C0;
    @NonNull
    @Builder.Default
    private Register3 func = new Register3();
    @NonNull
    @Builder.Default
    private Register4 A = new Register4();
    @NonNull
    @Builder.Default
    private Register4 B = new Register4();
    @NonNull
    @Builder.Default
    private Register4 D = new Register4();

    public static final int SIZE = 36;

    public static Command ofValue(@NonNull String string) {
        if (string.length() != SIZE) throw new IllegalArgumentException("Length of input string must be " + SIZE);
        return Command.builder()
                .D(Register4.valueOf(string.substring(32)))
                .B(Register4.valueOf(string.substring(28, 32)))
                .A(Register4.valueOf(string.substring(24, 28)))
                .func(Register3.valueOf(string.substring(21, 24)))
                .C0("1".equals(string.substring(20, 21)))
                .src(Register3.valueOf(string.substring(17, 20)))
                .MS1("1".equals(string.substring(16, 17)))
                .dest(Register3.valueOf(string.substring(13, 16)))
                .MS2("1".equals(string.substring(12, 13)))
                .mvType(Register4.valueOf(string.substring(8, 12)))
                .mvAddr(Register8.valueOf(string.substring(0, 8)))
                .build();
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        return builder
                .append(mvAddr.toString())
                .append(mvType.toString())
                .append(MS2 ? 1 : 0)
                .append(dest.toString())
                .append(MS1 ? 1 : 0)
                .append(src.toString())
                .append(C0 ? 1 : 0)
                .append(func.toString())
                .append(A.toString())
                .append(B.toString())
                .append(D.toString())
                .toString();
    }
}
