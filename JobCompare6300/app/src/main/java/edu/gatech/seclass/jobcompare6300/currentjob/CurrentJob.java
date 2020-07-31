package edu.gatech.seclass.jobcompare6300.currentjob;

import androidx.room.Entity;
import androidx.room.Ignore;

import java.math.BigDecimal;

import edu.gatech.seclass.jobcompare6300.job.Job;
import edu.gatech.seclass.jobcompare6300.job.Location;

@Entity
public class CurrentJob extends Job {

    public CurrentJob(String title, String company, Location location, short costOfLivingIndex,
                      BigDecimal salary, BigDecimal signingBonus, BigDecimal yearlyBonus,
                      float retirementBenefits, short leaveTime, BigDecimal jobScore) {
        super(title, company, location, costOfLivingIndex, salary, signingBonus, yearlyBonus, retirementBenefits, leaveTime, jobScore);
    }

    @Ignore
    public CurrentJob(String title, String company, Location location, short costOfLivingIndex,
                      BigDecimal salary, BigDecimal signingBonus, BigDecimal yearlyBonus,
                      float retirementBenefits, short leaveTime) {
        super(title, company, location, costOfLivingIndex, salary, signingBonus, yearlyBonus, retirementBenefits, leaveTime);
    }
}
