import proc.help.Flags;
import org.junit.jupiter.api.Test;
import proc.help.Register;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegisterTest {

    @Test
    public void shiftLeftTest() {
        Register register = Register.valueOf("0101001");
        Register shifted = register.shiftLeft(1, true);
        assertEquals(Register.valueOf("1010011"), shifted);

        shifted = register.shiftLeft(0, true);
        assertEquals(register, shifted);

        shifted = register.shiftLeft(2, false);
        assertEquals(Register.valueOf("0100100"), shifted);

        shifted = register.shiftLeft(register.size(), false);
        assertEquals(Register.valueOf("0000000"), shifted);

        shifted = register.shiftLeft(register.size() + 10, true);
        assertEquals(Register.valueOf("1111111"), shifted);
    }

    @Test
    public void shiftRightTest() {
        Register register = Register.valueOf("0101001");
        Register shifted = register.shiftRight(1, true);
        assertEquals(Register.valueOf("1010100"), shifted);

        shifted = register.shiftRight(0, true);
        assertEquals(register, shifted);

        shifted = register.shiftRight(2, false);
        assertEquals(Register.valueOf("0001010"), shifted);

        shifted = register.shiftRight(register.size(), false);
        assertEquals(Register.valueOf("0000000"), shifted);

        shifted = register.shiftRight(register.size() + 10, true);
        assertEquals(Register.valueOf("1111111"), shifted);
    }

    @Test
    public void andTest() {
        Register register1 = Register.valueOf("0001110101001");
        Register register2 = Register.valueOf("0101010001000");
        Register expected = Register.valueOf("0001010001000");

        Register res = register1.and(register2);
        assertEquals(expected, res);
    }

    @Test
    public void orTest() {
        Register register1 = Register.valueOf("1001110101001");
        Register register2 = Register.valueOf("0101010001010");
        Register expected = Register.valueOf("1101110101011");

        Register res = register1.or(register2);
        assertEquals(expected, res);
    }

    @Test
    public void xorTest() {
        Register register1 = Register.valueOf("1001110101001");
        Register register2 = Register.valueOf("0101010001010");
        Register expected = Register.valueOf("1100100100011");

        Register res = register1.xor(register2);
        assertEquals(expected, res);
    }

    @Test
    public void nandTest() {
        Register register1 = Register.valueOf("1001110101001");
        Register register2 = Register.valueOf("0101010001010");
        Register expected = Register.valueOf("1110101110111");

        Register res = register1.nand(register2);
        assertEquals(expected, res);
    }

    @Test
    public void norTest() {
        Register register1 = Register.valueOf("1001110101001");
        Register register2 = Register.valueOf("0101010001010");
        Register expected = Register.valueOf("0010001010100");

        Register res = register1.nor(register2);
        assertEquals(expected, res);
    }

    @Test
    public void xnorTest() {
        Register register1 = Register.valueOf("1001110101001");
        Register register2 = Register.valueOf("0101010001010");
        Register expected = Register.valueOf("0011011011100");

        Register res = register1.xnor(register2);
        assertEquals(expected, res);
    }

    @Test
    public void notTest() {
        Register register = Register.valueOf("0001110101");
        Register expected = Register.valueOf("1110001010");

        Register res = register.not();
        assertEquals(expected, res);
    }

    @Test
    public void reverseTest() {
        Register register = Register.valueOf("0001110101");
        Register expected = Register.valueOf("1010111000");

        Register res = register.reverse();
        assertEquals(expected, res);
    }

    @Test
    public void getSignTest() {
        Register register1 = Register.valueOf("1");
        Register register2 = Register.valueOf("0");

        boolean one = register1.getSign();
        assertTrue(one);
        boolean zero = register2.getSign();
        assertFalse(zero);
    }

    @Test
    public void valueOfAndToStringTest() {
        Register register = Register.valueOf("0101001010");
        assertEquals("0101001010", register.toString());
    }

    @Test
    public void onesTest() {
        Register register = Register.ones(10);
        assertEquals(Register.valueOf("1111111111"), register);
    }

    @Test
    public void equalsTest() {
        Register reg1 = Register.valueOf("010101");
        Register reg2 = Register.valueOf("010101");
        Register reg3 = Register.valueOf("1010101");

        assertTrue(reg1.equals(reg2));
        assertTrue(reg2.equals(reg1));
        assertFalse(reg1.equals(reg3));
        assertFalse(reg3.equals(reg2));
    }

    /**
     * ADDITION TESTS
     */
    //TODO need more tests for add method in Register class
    @Test
    public void addTest1() {
        Register regA = Register.valueOf("0001");
        Register regB = Register.valueOf("0101");
        Flags flags = new Flags();
        Register regC = regA.add(regB, true, flags);
        assertEquals("0111", regC.toString());
        assertEquals(new Flags(false, false, false, false), flags);
    }

    @Test
    public void addTest2() {
        Register regA = Register.valueOf("1001");
        Register regB = Register.valueOf("0101");
        Flags flags = new Flags();
        Register regC = regA.add(regB, false, flags);
        assertEquals("1110", regC.toString());
        assertEquals(new Flags(false, false, true, false), flags);
    }

    @Test
    public void addTest3() {
        Register regA = Register.valueOf("1111");
        Register regB = Register.valueOf("1111");
        Flags flags = new Flags();
        Register regC = regA.add(regB, false, flags);
        assertEquals("1110", regC.toString());
        assertEquals(new Flags(false, true, true, false), flags);
    }

    @Test // OVERFLOW
    public void addTest4() {
        Register regA = Register.valueOf("0111");
        Register regB = Register.valueOf("0011");
        Flags flags = new Flags();
        Register regC = regA.add(regB, true, flags);
        assertEquals("1011", regC.toString());
        assertEquals(new Flags(true, false, true, false), flags);
    }

    /**
     * SUBTRACTION TESTS
     */
    //TODO need more tests for sub method in Register class
    @Test
    public void subTest1() {
        Register regA = Register.valueOf("11111");
        Register regB = Register.valueOf("00010");
        Flags flags = new Flags();
        //pass c_in = 1 to avoid subtraction 1
        Register regC = regA.sub(regB, true, flags);
        assertEquals("11101", regC.toString());
        assertEquals(new Flags(false, false, true, false), flags);
    }

    @Test
    public void subTest2() {
        Register regA = Register.valueOf("11111");
        Register regB = Register.valueOf("00010");
        Flags flags = new Flags();
        //pass c_in = 1 to avoid subtraction 1
        Register regC = regA.sub(regB, false, flags);
        assertEquals("11100", regC.toString());
        assertEquals(new Flags(false, false, true, false), flags);
    }

    @Test
    public void subTest3() {
        Register regA = Register.valueOf("11111");
        Register regB = Register.valueOf("11111");
        Flags flags = new Flags();
        //pass c_in = 1 to avoid subtraction 1
        Register regC = regA.sub(regB, false, flags);
        assertEquals("11111", regC.toString());
        assertEquals(new Flags(false, true, true, false), flags);
    }
}