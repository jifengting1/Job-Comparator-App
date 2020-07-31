package edu.gatech.seclass.jobcompare6300.comparisonsettings;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CurrentComparisonSetting {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "salary")
    private int salary = 1;
    @ColumnInfo(name = "signing_bonus")
    private int signingBonus = 1;
    @ColumnInfo(name = "yearly_bonus")
    private int yearlyBonus = 1;
    @ColumnInfo(name = "retirement_benefits")
    private int retirementBenefits = 1;
    @ColumnInfo(name = "leave_time")
    private int leaveTime = 1;


    public CurrentComparisonSetting(int salary, int signingBonus, int yearlyBonus,
                                    int retirementBenefits, int leaveTime) {
        this.salary = salary;
        this.signingBonus = signingBonus;
        this.yearlyBonus = yearlyBonus;
        this.retirementBenefits = retirementBenefits;
        this.leaveTime = leaveTime;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getSigningBonus() {
        return signingBonus;
    }

    public void setSigningBonus(int signingBonus) {
        this.signingBonus = signingBonus;
    }

    public int getYearlyBonus() {
        return yearlyBonus;
    }

    public void setYearlyBonus(int yearlyBonus) {
        this.yearlyBonus = yearlyBonus;
    }

    public int getRetirementBenefits() {
        return retirementBenefits;
    }

    public void setRetirementBenefits(int retirementBenefits) {
        this.retirementBenefits = retirementBenefits;
    }

    public int getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(int leaveTime) {
        this.leaveTime = leaveTime;
    }
}
