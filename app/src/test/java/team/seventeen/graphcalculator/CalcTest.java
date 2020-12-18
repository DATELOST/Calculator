package team.seventeen.graphcalculator;

import org.junit.Test;

import static org.junit.Assert.*;

public class CalcTest {
    @Test
    public void calc() {
        assertEquals(Calc.calc("1+1"),2);
        assertEquals(Calc.calc("2*(1+5)/3+1"),5);
    }
}