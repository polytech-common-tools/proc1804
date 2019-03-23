package system;

import lombok.NonNull;
import proc.help.*;

import java.io.*;

/**
 * Load file with only 0 and 1,
 * numbers can be separated by spaces.
 * Length of each line must be 36.
 * Maximal lines number is 256.
 */
public final class FileCommandsLoader implements CommandsLoader {

    public static final int MEMORY_SIZE = 256;

    public Command[] loadCommands(@NonNull final Reader reader) throws IOException {
        final var strCommends = new String[MEMORY_SIZE];
        final var bufferedReader = new BufferedReader(reader);
        String inLine;
        int counter = 0;
        while ((inLine = bufferedReader.readLine()) != null) {
            if (counter >= MEMORY_SIZE) throw new IllegalArgumentException("You can't load more than 256 commands");
            final String outLine = transformLine(inLine);
            if (outLine == null) continue;
            strCommends[counter] = outLine;
            counter++;
        }

        bufferedReader.close();

        var commandsObj = new Command[MEMORY_SIZE];
        for (int i = 0; i < counter; i++) {
            commandsObj[i] = strToCommand(strCommends[i]);
        }
        //Fill the rest of memory with empty commands
        for (int i = counter; i < MEMORY_SIZE; i++) {
            commandsObj[i] = Command.builder().build();
        }
        return commandsObj;
    }

    private String transformLine(@NonNull String inputLine) {
        int commandLengthCounter = 0;
        byte bytes[] = inputLine.getBytes();
        var builder = new StringBuilder();
        //Line begins with # if it's a comment
        if (bytes.length == 0 || bytes[0] == '#') return null;
        for (byte b : bytes) {
            if (b == ' ') continue;
            if (b != 'x' && b != '0' && b != '1')
                throw new IllegalArgumentException(String.format("Illegal symbol: %c", b));
            if (b == '1') builder.append("1");
            else builder.append("0");
            commandLengthCounter++;
        }
        if (commandLengthCounter != 36)
            throw new IllegalArgumentException("Command length must be 36, " + inputLine);
        return builder.toString();
    }

    private Command strToCommand(@NonNull String string) {
        return Command.ofValue(string);
    }
}