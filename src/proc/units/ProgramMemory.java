package proc.units;

import lombok.NonNull;
import proc.help.Command;
import proc.help.Register8;

public final class ProgramMemory {
    public static final int SIZE = 256;
    private Command[] commands = new Command[SIZE];

    public void storeCommands(@NonNull Command[] commands) {
        if (commands.length > SIZE)
            throw new IllegalArgumentException(String.format("Commands length cannot be more than %d", SIZE));
        for (int i = 0; i < commands.length; i++) {
            this.commands[i] = commands[i];
        }
    }

    public Command loadCommand(@NonNull Register8 addr) {
        return commands[(int) addr.toLong()];
    }
}