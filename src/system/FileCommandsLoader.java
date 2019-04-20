package system;

import lombok.NonNull;
import proc.help.*;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Load file with only 0 and 1,
 * numbers can be separated by spaces.
 * Length of each line must be 36.
 * Maximal lines number is 256.
 */
public final class FileCommandsLoader implements CommandsLoader {

    public static final int MEMORY_SIZE = 256;
    private static final int COMMAND_LENGTH = 36;
    private Map<Integer, String> commentsMap;

    @Override
    public void setCommentsMap(@NonNull Map<Integer, String> commentsMap) {
        this.commentsMap = commentsMap;
    }

    public Command[] loadCommands(@NonNull final Reader reader) throws IOException {
        final var strCommands = new String[MEMORY_SIZE];
        /*
        Using Atomics because in lambda-functions it is necessary to use effectively
        final or final objects or primitives.
        There are no method by Integer wrapper to increment the inner value
         */
        final AtomicInteger counter = new AtomicInteger(0);
        try (final var bufferedReader = new BufferedReader(reader)) {
            bufferedReader.lines()
                    .forEach(line -> {
                        if (counter.get() >= MEMORY_SIZE)
                            throw new IllegalArgumentException("You can't load more than 256 commands");
                        //call trim to remove extra spaces at the beginning and at the end of string
                        final String outLine = transformLine(line.trim(), counter.get());
                        if (outLine != null) {
                            strCommands[counter.incrementAndGet()] = outLine;
                        }
                    });
        }

        var commandsObj = new Command[MEMORY_SIZE];
        for (int i = 0; i < counter.get(); i++) {
            commandsObj[i] = strToCommand(strCommands[i]);
        }
        //Fill the rest of memory with empty commands
        for (int i = counter.get(); i < MEMORY_SIZE; i++) {
            commandsObj[i] = Command.builder().build();
        }
        return commandsObj;
    }

    private String transformLine(@NonNull String inputLine, int counter) {
        int commandLengthCounter = 0;
        byte[] bytes = inputLine.getBytes();
        var builder = new StringBuilder();
        //Line begins with # if it's a comment
        //Double ## for printable comment
        //check that there are more symbols than only ##
        if (bytes.length > 2 && bytes[0] == '#' && bytes[1] == '#') {
            if (commentsMap != null) commentsMap.put(counter, inputLine.substring(2).trim());
            return null;
        }
        if (bytes.length == 0 || bytes[0] == '#') return null;
        for (byte b : bytes) {
            if (b == ' ') continue;
            if (b != 'x' && b != '0' && b != '1')
                throw new IllegalArgumentException(String.format("Illegal symbol: %c, in command: %d", b, counter));
            if (b == '1') builder.append("1");
            else builder.append("0");
            commandLengthCounter++;
        }
        if (commandLengthCounter != COMMAND_LENGTH)
            throw new IllegalArgumentException("Command length must be " + COMMAND_LENGTH + " " + inputLine);
        return builder.toString();
    }

    private Command strToCommand(@NonNull String string) {
        return Command.ofValue(string);
    }
}