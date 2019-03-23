package system;

import lombok.NonNull;
import proc.help.Command;

import java.io.IOException;
import java.io.Reader;

public interface CommandsLoader {
    Command[] loadCommands(@NonNull Reader reader) throws IOException;
}