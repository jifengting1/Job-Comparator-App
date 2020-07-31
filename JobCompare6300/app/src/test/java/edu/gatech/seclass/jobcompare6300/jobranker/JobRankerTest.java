package edu.gatech.seclass.jobcompare6300.jobranker;

import org.junit.Test;

import java.math.BigDecimal;

import edu.gatech.seclass.jobcompare6300.comparisonsettings.CurrentComparisonSetting;
import edu.gatech.seclass.jobcompare6300.currentjob.CurrentJob;
import edu.gatech.seclass.jobcompare6300.job.Job;
import edu.gatech.seclass.jobcompare6300.job.Location;

import static org.junit.Assert.*;

public class JobRankerTest {

    @Test
    public void testCalculateJobScore() {
        String title = "Software Engineer";
        String company = "Google";
        Location location = new Location("Mountain View", "CA");
        short costOfLivingIndex = 255;
        BigDecimal salary = new BigDecimal("150000");
        BigDecimal signingBonus = new BigDecimal("45000");
        BigDecimal yearlyBonus = new BigDecimal("15000");
        float retirementBenefits = 6.0f;
        short leaveTime = 25;

        Job job = new CurrentJob(title, company, location, costOfLivingIndex, salary, signingBonus,
                yearlyBonus, retirementBenefits, leaveTime);

        int salaryWeight = 2;
        int signingBonusWeight = 3;
        int yearlyBonusWeight = 1;
        int benefitsWeight = 4;
        int leaveTimeWeight = 5;

        CurrentComparisonSetting currentComparisonSetting = new CurrentComparisonSetting(salaryWeight,
                signingBonusWeight, yearlyBonusWeight, benefitsWeight, leaveTimeWeight);

        BigDecimal actualJobScore = JobRanker.calculateJobScore(job, currentComparisonSetting);

        BigDecimal expectedJobScore = new BigDecimal("14588.470751920");

        assertEquals(expectedJobScore, actualJobScore);
    }
}