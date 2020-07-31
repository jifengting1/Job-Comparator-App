package edu.gatech.seclass.jobcompare6300.jobranker;


import java.math.BigDecimal;
import java.math.RoundingMode;

import edu.gatech.seclass.jobcompare6300.comparisonsettings.CurrentComparisonSetting;
import edu.gatech.seclass.jobcompare6300.job.Job;
import edu.gatech.seclass.jobcompare6300.util.CostOfLivingAdjuster;

public class JobRanker {
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100.00);

    public static BigDecimal calculateJobScore(Job job, CurrentComparisonSetting currentComparisonSetting) {
        BigDecimal salary = job.getSalary();
        BigDecimal signingBonus = job.getSigningBonus();
        BigDecimal yearlyBonus = job.getYearlyBonus();
        BigDecimal costOfLivingIndex = BigDecimal.valueOf(job.getCostOfLivingIndex());
        BigDecimal benefits = BigDecimal.valueOf(job.getRetirementBenefits()).divide(ONE_HUNDRED, 3, RoundingMode.FLOOR);
        BigDecimal leaveTime = BigDecimal.valueOf(job.getLeaveTime());

        BigDecimal adjustedSalary = CostOfLivingAdjuster.calculateAdjustedAmount(salary, costOfLivingIndex);
        BigDecimal adjustedSigningBonus = CostOfLivingAdjuster.calculateAdjustedAmount(signingBonus, costOfLivingIndex);
        BigDecimal adjustedYearlyBonus = CostOfLivingAdjuster.calculateAdjustedAmount(yearlyBonus, costOfLivingIndex);

        int salaryWeight = 1;
        int signingBonusWeight = 1;
        int yearlyBonusWeight = 1;
        int benefitsWeight = 1;
        int leaveTimeWeight = 1;

        if (currentComparisonSetting != null) {
            salaryWeight = currentComparisonSetting.getSalary();
            signingBonusWeight = currentComparisonSetting.getSigningBonus();
            yearlyBonusWeight = currentComparisonSetting.getYearlyBonus();
            benefitsWeight = currentComparisonSetting.getRetirementBenefits();
            leaveTimeWeight = currentComparisonSetting.getLeaveTime();
        }

        int totalWeight = salaryWeight + signingBonusWeight + yearlyBonusWeight + benefitsWeight + leaveTimeWeight;

        BigDecimal salaryWeightedPercent = calculateWeightedPercent(salaryWeight, totalWeight);
        BigDecimal signingBonusWeightedPercent = calculateWeightedPercent(signingBonusWeight, totalWeight);
        BigDecimal yearlyBonusWeightedPercent = calculateWeightedPercent(yearlyBonusWeight, totalWeight);
        BigDecimal benefitsWeightedPercent = calculateWeightedPercent(benefitsWeight, totalWeight);
        BigDecimal leaveTimeWeightedPercent = calculateWeightedPercent(leaveTimeWeight, totalWeight);

        BigDecimal adjustedSalaryWeighted = adjustedSalary.multiply(salaryWeightedPercent);
        BigDecimal adjustedSigningBonusWeighted = adjustedSigningBonus.multiply(signingBonusWeightedPercent);
        BigDecimal adjustedYearlyBonusWeighted = adjustedYearlyBonus.multiply(yearlyBonusWeightedPercent);
        BigDecimal benefitsWeighted = adjustedSalary.multiply(benefitsWeightedPercent).multiply(benefits);

        BigDecimal leaveTimeWeighted = adjustedSalary.multiply(leaveTimeWeightedPercent.multiply(leaveTime))
                .divide(BigDecimal.valueOf(260), 2, RoundingMode.FLOOR);

        return adjustedSalaryWeighted
                .add(adjustedSigningBonusWeighted)
                .add(adjustedYearlyBonusWeighted)
                .add(benefitsWeighted)
                .add(leaveTimeWeighted);
    }

    private static BigDecimal calculateWeightedPercent(int weight, int totalWeight) {
        return BigDecimal.valueOf(weight).divide(BigDecimal.valueOf(totalWeight), 4, RoundingMode.FLOOR);
    }
}
