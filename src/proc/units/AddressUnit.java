package proc.units;

import lombok.Getter;
import lombok.NonNull;
import proc.help.Flags;
import proc.help.Register4;
import proc.help.Register8;

/**
 * Class to chose next command address
 */
public final class AddressUnit {
    //Commands counter, need to find next command
    //If we immediately move to address, nextAddress will be equal this address
    @Getter
    private Register8 nextAddress;
    //Reference to outer object
    private Stack stack;

    /**
     * @param stack        stack from processor
     * @param startAddress program will start with this address
     */
    public AddressUnit(@NonNull Stack stack, @NonNull Register8 startAddress) {
        this.stack = stack;
        this.nextAddress = startAddress;
    }

    //TODO
    public void countNextAddress(@NonNull Register8 mvAddress, @NonNull Register4 mvType, @NonNull Flags flags) {
        if (!nextAddress.equals(Register8.ones())) nextAddress.increment(null);
        switch (mvType.toString()) {
            case "0000": //Переход на адрес из регистра микрокоманд, если Z = 0
                if (!flags.isZ()) {
                    nextAddress = mvAddress.copy();
                } //We have already incremented nextAddress register
                break;
            case "0001": //Переход на адрес из регистра микрокоманд
                nextAddress = mvAddress.copy();
                break;
            case "0010": //Продолжить (переход на следующий адрес)
                //We have already incremented nextAddress register
                break;
            case "0011": //Переход на адрес, формируемый клавишным регистром адреса
                throw new UnsupportedOperationException("This operation is not supported");
            case "0100": //Переход к подпрограмме, если Z = 0
                //TODO NEED TO CHECK
                if (!flags.isZ()) {
                    stack.push(nextAddress);
                    nextAddress = mvAddress.copy();
                } //We have already incremented nextAddress register
                break;
            case "0101": //Переход к подпрограмме
                stack.push(nextAddress);
                nextAddress = mvAddress.copy();
                break;
            case "0110": //Возврат из подпрограммы
                nextAddress = stack.pop();
                break;
            case "0111": //Переход по стеку
                var temp = nextAddress.copy();
                nextAddress = stack.pop();
                stack.push(temp); //Put return address back into the stack
                break;

            case "1000": //Окончить цикл и вытолкнуть из стека, если Z = 1
                //TODO NEED TO CHECK
                if (flags.isZ()) {
                    stack.pop();
                    //Move to next command (incremented)
                } else {
                    //NOT SURE ABOUT THIS
                    nextAddress = mvAddress.copy();
                }
                break;
            case "1001": //Загрузить стек и продолжить
                stack.push(nextAddress);
                //Just push and continue with next command (incremented)
                break;
            case "1010": //Вытолкнуть из стека и продолжить
                stack.pop();
                //Just pop and continue with next command (incremented)
                break;
            case "1011": //Окончить цикл и вытолкнуть из стека, если C4 = 1
                //TODO
                throw new UnsupportedOperationException("Please contact Malik and make him support this move type");
            case "1100": //Переход на адрес из регистра микрокоманд, если Z = 1
                if (flags.isZ()) nextAddress = mvAddress.copy();
                break;
            case "1101": //Переход на адрес из регистра микрокоманд, если F3 = 1
                if (flags.isF3()) nextAddress = mvAddress.copy();
                break;
            case "1110": //Переход на адрес из регистра микрокоманд, если OVR = 1
                if (flags.isOVR()) nextAddress = mvAddress.copy();
                break;
            case "1111": //Переход на адрес из регистра микрокоманд, если C4 = 1
                if (flags.isC4()) nextAddress = mvAddress.copy();
                break;
            default:
                throw new IllegalStateException();
        }
    }
}