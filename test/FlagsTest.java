import org.junit.jupiter.api.Test;
import proc.help.Flags;

import static org.junit.jupiter.api.Assertions.*;

public class FlagsTest {

    @Test
    public void simple() {
        Flags flags1 = new Flags();
        flags1.set(true, false, true, true);
        Flags flags2 = new Flags();
        flags2.set(true, true, false, true);
        assertNotEquals(flags1.hashCode(), flags2.hashCode());
    }
}