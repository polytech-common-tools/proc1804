package system;

import lombok.NonNull;

import java.io.IOException;
import java.io.Writer;

public interface StateWriter {

    void writeHeader() throws IOException;

    void writeHeader(@NonNull Writer writer) throws IOException;

    void write(@NonNull ProcState state) throws IOException;

    void write(@NonNull ProcState state, @NonNull Writer writer) throws IOException;

    void writeFooter() throws IOException;

    void writeFooter(@NonNull Writer writer) throws IOException;

    void close() throws IOException;
}