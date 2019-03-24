package system;

import lombok.NonNull;
import proc.help.Command;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

public interface CommandsLoader {

    void setCommentsMap(@NonNull Map<Integer, String> commentsMap);

    Command[] loadCommands(@NonNull Reader reader) throws IOException;
}