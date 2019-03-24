package proc.units;

import lombok.NonNull;
import proc.help.Register4;

/**
 * Class for register which are used
 * to store data, temporary results, etc
 */
public final class RegistersMemoryUnit {
    final static short MEM_SIZE = 16;
    //16 common purpose registers
    private Register4 regs[] = new Register4[MEM_SIZE];

    {
        for (int i = 0; i < regs.length; i++) {
            regs[i] = new Register4();
        }
    }
    /**
     * @param address where to save
     * @param value   saving register
     */
    public void save(@NonNull Register4 address, @NonNull Register4 value) {
        regs[(int) address.toLong()] = value.copy();
    }

    /**
     * @param address address of loading value
     * @return register from passed address
     */
    public Register4 load(@NonNull Register4 address) {
        return regs[(int) address.toLong()].copy();
    }
}