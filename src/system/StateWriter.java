package system;

import lombok.NonNull;
import proc.help.Flags;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public interface StateWriter {

    void setComments(@NonNull Map<Integer, String> comments);

    void writeHeader() throws IOException;

    void writeHeader(@NonNull Writer writer) throws IOException;

    void write(@NonNull ProcState state) throws IOException;

    void write(@NonNull ProcState state, @NonNull Writer writer) throws IOException;

    //Two methods for flags delay
    void write(@NonNull ProcState state, @NonNull Flags flags) throws IOException;

    void write(@NonNull ProcState state, @NonNull Writer writer, @NonNull Flags flags) throws IOException;

    void writeFooter() throws IOException;

    void writeFooter(@NonNull Writer writer) throws IOException;

    void close() throws IOException;
}