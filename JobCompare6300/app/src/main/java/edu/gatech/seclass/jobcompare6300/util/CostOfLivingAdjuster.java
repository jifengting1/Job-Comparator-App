package edu.gatech.seclass.jobcompare6300.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CostOfLivingAdjuster {
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100.00);

    public static BigDecimal calculateAdjustedAmount(BigDecimal amount, BigDecimal costOfLivingIndex) {
        return amount.multiply(ONE_HUNDRED)
                .divide(costOfLivingIndex, 2, RoundingMode.FLOOR);
    }
}
