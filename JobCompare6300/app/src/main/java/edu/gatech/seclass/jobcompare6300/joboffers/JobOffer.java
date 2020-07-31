package edu.gatech.seclass.jobcompare6300.joboffers;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

import java.math.BigDecimal;

import edu.gatech.seclass.jobcompare6300.job.Job;
import edu.gatech.seclass.jobcompare6300.job.Location;

@Entity(indices = {@Index(value = {"title", "company", "location", "cost_of_living_index", "salary","signing_bonus", "yearly_bonus", "retirement_benefits", "leave_time"}, unique = true)})

public class JobOffer extends Job {

    public JobOffer(String title, String company, Location location, short costOfLivingIndex,
                    BigDecimal salary, BigDecimal signingBonus, BigDecimal yearlyBonus,
                    float retirementBenefits, short leaveTime, BigDecimal jobScore) {
        super(title, company, location, costOfLivingIndex, salary, signingBonus, yearlyBonus, retirementBenefits, leaveTime, jobScore);
    }

    @Ignore
    public JobOffer(String title, String company, Location location, short costOfLivingIndex,
                    BigDecimal salary, BigDecimal signingBonus, BigDecimal yearlyBonus,
                    float retirementBenefits, short leaveTime) {
        super(title, company, location, costOfLivingIndex, salary, signingBonus, yearlyBonus, retirementBenefits, leaveTime);
    }
}
