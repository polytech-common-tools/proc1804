package system;

import lombok.NonNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Stream;

//TODO SIMPLIFY IT
public final class CoolWriter implements StateWriter {

    private List<PrintableValue> order;
    private Writer writer;
    private Map<Integer, String> comments;
    private boolean hasToComment;
    private int commentLength = "comment".length();

    public CoolWriter(@NonNull File settings, @NonNull Writer writer) throws IOException {
        this.writer = writer;
        var values = readProperties(settings);
        checkValues(values);
        order = values;
    }

    @Override
    public void write(@NonNull ProcState state) throws IOException {
        writer.write(getStringToWrite(state));
        writer.flush();
    }

    @Override
    public void write(@NonNull ProcState state, @NonNull Writer writer) throws IOException {
        writer.write(getStringToWrite(state));
        writer.flush();
    }

    private String getStringToWrite(@NonNull ProcState state) {
        var builder = new StringBuilder();
        for (PrintableValue printableValue : order) {
            builder.append("|");
            switch (printableValue) {
                case N:
                    builder.append(String.format("%4d", state.getClkCounter()));
                    break;
                case Y:
                    builder.append(state.getY());
                    break;
                case F:
                    builder.append(state.getF());
                    break;
                case Q:
                    builder.append(state.getQ());
                    break;
                case NEXT:
                    builder.append(String.format("%4d", state.getNextAddress().toLong()));
                    break;
                case ADDR:
                    builder.append(String.format("%4d", state.getCurrentAddress().toLong()));
                    break;
                case OVR:
                    builder.append(state.getFlags().isOVR() ? " 1 " : " 0 ");
                    break;
                case C4:
                    builder.append(state.getFlags().isC4() ? " 1 " : " 0 ");
                    break;
                case F3:
                    builder.append(state.getFlags().isF3() ? " 1 " : " 0 ");
                    break;
                case Z:
                    builder.append(state.getFlags().isZ() ? " 1 " : " 0 ");
                    break;
                case SP:
                    builder.append(String.format("%2d", state.getSp()));
                    break;
                case COMMENT:
                    if (hasToComment) {
                        String comment = comments.get((int) state.getCurrentAddress().toLong());
                        if (comment == null) comment = "";
                        builder.append(String.format("%-" + commentLength + "s", comment));
                    }
                    break;
                default: {
                    if (printableValue.isSpecificStack()) {
                        builder.append(state.getStackArray()[printableValue.ordinal() - PrintableValue.STACK0.ordinal()]);
                    } else if (printableValue.isSpecificReg()) {
                        builder.append(" " + state.getMemory()[printableValue.ordinal() - PrintableValue.REG0.ordinal()]);
                    } else throw new IllegalStateException();
                }
            }
        }
        builder.append("|\n");
        return builder.toString();
    }

    @Override
    public void writeHeader() throws IOException {
        writer.write(getHeaderToWrite());
        writer.flush();
    }

    @Override
    public void writeHeader(@NonNull Writer writer) throws IOException {
        writer.write(getHeaderToWrite());
        writer.flush();
    }

    private String getHeaderToWrite() {
        var builder = new StringBuilder();
        for (int lineNum = 0; lineNum < 3; lineNum++) {
            for (PrintableValue printableValue : order) {
                if (lineNum == 1) builder.append("|");
                else builder.append("+");
                switch (printableValue) {
                    case N:
                        if (lineNum == 1) builder.append(String.format("%4s", "clk"));
                        else builder.append("----");
                        break;
                    case Y:
                    case F:
                    case Q:
                        if (lineNum == 1) builder.append(String.format("%4s", printableValue.toString()));
                        else builder.append("----");
                        break;
                    case OVR:
                    case C4:
                    case F3:
                    case Z:
                        if (lineNum == 1) builder.append(String.format("%3s", printableValue.toString()));
                        else builder.append("---");
                        break;
                    case NEXT:
                        if (lineNum == 1) builder.append("next");
                        else builder.append("----");
                        break;
                    case ADDR:
                        if (lineNum == 1) builder.append("addr");
                        else builder.append("----");
                        break;
                    case SP:
                        if (lineNum == 1) builder.append(String.format("%2s", "SP"));
                        else builder.append("--");
                        break;
                    case COMMENT:
                        if (hasToComment) {
                            if (lineNum == 1) builder.append(String.format("%-" + commentLength + "s", "comment"));
                            else builder.append("-".repeat(commentLength));
                        }
                        break;
                    default: {
                        if (printableValue.isSpecificStack()) {
                            if (lineNum == 1) builder.append(String.format("%8s", printableValue.toString()));
                            else builder.append("--------");
                        } else if (printableValue.isSpecificReg()) {
                            if (lineNum == 1) builder.append(String.format("%5s", printableValue.toString()));
                            else builder.append("-----");
                        } else throw new IllegalStateException();
                    }
                }
            }
            if (lineNum == 1) builder.append("|\n");
            else builder.append("+\n");
        }
        return builder.toString();
    }

    @Override
    public void writeFooter() throws IOException {
        writer.write(getFooterToWrite());
        writer.flush();
    }

    @Override
    public void writeFooter(@NonNull Writer writer) throws IOException {
        writer.write(getFooterToWrite());
        writer.flush();
    }

    private String getFooterToWrite() {
        var builder = new StringBuilder();
        for (PrintableValue printableValue : order) {
            builder.append("+");
            switch (printableValue) {
                case N:
                    builder.append("----");
                    break;
                case Y:
                case F:
                case Q:
                    builder.append("----");
                    break;
                case OVR:
                case C4:
                case F3:
                case Z:
                    builder.append("---");
                    break;
                case NEXT:
                case ADDR:
                    builder.append("----");
                    break;
                case SP:
                    builder.append("--");
                    break;
                case COMMENT:
                    if (hasToComment) {
                        builder.append("-".repeat(commentLength));
                    }
                    break;
                default: {
                    if (printableValue.isSpecificStack()) {
                        builder.append("--------");
                    } else if (printableValue.isSpecificReg()) {
                        builder.append("-----");
                    } else throw new IllegalStateException();
                }
            }
        }
        builder.append("+\n");
        return builder.toString();
    }

    private List<PrintableValue> readProperties(@NonNull File settings) throws IOException {
        final var properties = new Properties();
        properties.load(new FileReader(settings));
        final String property = properties.getProperty("line");
        String[] values = property.split(":");

        var propertiesList = new ArrayList<PrintableValue>();
        for (String value : values) {
            propertiesList.add(PrintableValue.valueOf(value));
        }
        return propertiesList;
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    @Override
    public void setComments(@NonNull Map<Integer, String> comments) {
        if (!comments.isEmpty()) {
            this.comments = comments;
            Optional<Integer> reduced = comments.entrySet().stream().map(e -> e.getValue().length()).reduce(Math::max);
            if (reduced.isPresent() && reduced.get() > commentLength) commentLength = reduced.get();
            hasToComment = true;
        }
    }

    private void checkValues(@NonNull List<PrintableValue> values) {
        boolean allRegs = false;
        boolean specificReg = false;
        boolean allStacks = false;
        boolean specificStack = false;
        boolean allFlags = false;
        boolean specificFlag = false;
        Set<PrintableValue> copies = new HashSet<>();

        for (PrintableValue value : values) {
            if (copies.contains(value)) throw new IllegalArgumentException(value.toString() + " is defined several times");
            copies.add(value);
            if (value.isSpecificReg()) specificReg = true;
            else if (value.isSpecificStack()) specificStack = true;
            else if (value == PrintableValue.REGS) allRegs = true;
            else if (value == PrintableValue.STACKS) allStacks = true;
            else if (value == PrintableValue.FLAGS) allFlags = true;
            else if (value.isSpecificFlag()) specificFlag = true;
        }

        if (specificReg && allRegs)
            throw new IllegalArgumentException("REGS and REG<NUMBER> cannot be defined together");
        if (specificStack && allStacks)
            throw new IllegalArgumentException("STACKS and STACK<NUMBER> cannot be defined together");
        if (specificFlag && allFlags)
            throw new IllegalArgumentException("All flags and specific flag cannot be set together");
        if (allRegs) {
            int i = values.indexOf(PrintableValue.REGS);
            values.remove(i);
            values.addAll(i, PrintableValue.getAllRegs());
        }

        if (allStacks) {
            int i = values.indexOf(PrintableValue.STACKS);
            values.remove(i);
            values.addAll(i, PrintableValue.getAllStacks());
        }

        if (allFlags) {
            int i = values.indexOf(PrintableValue.FLAGS);
            values.remove(i);
            values.addAll(i, PrintableValue.getAllFlags());
        }
    }
}