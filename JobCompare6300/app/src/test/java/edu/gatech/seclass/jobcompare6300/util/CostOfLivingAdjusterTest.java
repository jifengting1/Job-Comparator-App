package edu.gatech.seclass.jobcompare6300.util;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class CostOfLivingAdjusterTest {

    @Test
    public void calculateAdjustedAmount() {
        BigDecimal salary = new BigDecimal("150000");
        BigDecimal col = new BigDecimal("255");

        BigDecimal actualAdjustedSalary = CostOfLivingAdjuster.calculateAdjustedAmount(salary, col);
        BigDecimal expectedAdjustedSalary = new BigDecimal("58823.52");

        assertEquals(expectedAdjustedSalary, actualAdjustedSalary);
    }
}