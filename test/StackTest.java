import org.junit.jupiter.api.Test;
import proc.help.Register8;
import proc.units.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StackTest {

    @Test
    public void rewriteLastValue() {
        Stack stack = new Stack();
        Register8 reg1 = Register8.valueOf("00000000");
        Register8 reg2 = Register8.valueOf("00000001");
        Register8 reg3 = Register8.valueOf("00000010");
        Register8 reg4 = Register8.valueOf("00000011");
        Register8 reg5 = Register8.valueOf("00000100");
        Register8 reg6 = Register8.valueOf("00000101");

        stack.push(reg1);
        stack.push(reg2);
        stack.push(reg3);
        stack.push(reg4);
        stack.push(reg5);
        stack.push(reg6);

        assertEquals(reg6, stack.pop());
        assertEquals(reg3, stack.pop());
        assertEquals(reg2, stack.pop());
        assertEquals(reg1, stack.pop());
    }

    @Test
    public void popEmptyStack() {
        Stack stack = new Stack();
        assertEquals(new Register8(), stack.pop());
    }

    @Test
    public void halfPushAndPopAll() {
        Stack stack = new Stack();

        Register8 reg1 = Register8.valueOf("00000000");
        Register8 reg2 = Register8.valueOf("00000001");
        Register8 reg3 = Register8.valueOf("00000010");

        stack.push(reg1);
        stack.push(reg2);
        stack.push(reg3);

        assertEquals(reg3, stack.pop());
        assertEquals(reg2, stack.pop());
        assertEquals(reg1, stack.pop());
    }

    @Test
    public void pushOnePopAndPushAndPopAgain() {
        Stack stack = new Stack();

        Register8 reg1 = Register8.valueOf("00000000");
        Register8 reg2 = Register8.valueOf("00000001");
        Register8 reg3 = Register8.valueOf("00000010");

        stack.push(reg1);
        assertEquals(reg1, stack.pop());

        stack.push(reg2);
        assertEquals(reg2, stack.pop());

        stack.push(reg3);
        assertEquals(reg3, stack.pop());
    }

    @Test
    public void pushAndPopSeveralTimes() {
        Stack stack = new Stack();

        Register8 reg1 = Register8.valueOf("00000000");

        stack.push(reg1);

        assertEquals(reg1, stack.pop());
        assertEquals(reg1, stack.pop());
        assertEquals(reg1, stack.pop());
    }
}
