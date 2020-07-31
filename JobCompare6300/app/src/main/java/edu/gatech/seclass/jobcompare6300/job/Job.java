package edu.gatech.seclass.jobcompare6300.job;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity
public abstract class Job {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "company")
    private String company;
    @ColumnInfo(name = "location")
    private Location location;
    @ColumnInfo(name = "cost_of_living_index")
    private short costOfLivingIndex;
    @ColumnInfo(name = "salary")
    private BigDecimal salary;
    @ColumnInfo(name = "signing_bonus")
    private BigDecimal signingBonus;
    @ColumnInfo(name = "yearly_bonus")
    private BigDecimal yearlyBonus;
    @ColumnInfo(name = "retirement_benefits")
    private float retirementBenefits;
    @ColumnInfo(name = "leave_time")
    private short leaveTime;
    @ColumnInfo(name = "job_score")
    private BigDecimal jobScore;

    public Job() {}

    public Job(String title, String company, Location location, short costOfLivingIndex,
               BigDecimal salary, BigDecimal signingBonus, BigDecimal yearlyBonus,
               float retirementBenefits, short leaveTime, BigDecimal jobScore) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.costOfLivingIndex = costOfLivingIndex;
        this.salary = salary;
        this.signingBonus = signingBonus;
        this.yearlyBonus = yearlyBonus;
        this.retirementBenefits = retirementBenefits;
        this.leaveTime = leaveTime;
        this.jobScore = jobScore;
    }

    public Job(String title, String company, Location location, short costOfLivingIndex,
               BigDecimal salary, BigDecimal signingBonus, BigDecimal yearlyBonus,
               float retirementBenefits, short leaveTime) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.costOfLivingIndex = costOfLivingIndex;
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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public short getCostOfLivingIndex() {
        return costOfLivingIndex;
    }

    public void setCostOfLivingIndex(short costOfLivingIndex) {
        this.costOfLivingIndex = costOfLivingIndex;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public BigDecimal getSigningBonus() {
        return signingBonus;
    }

    public void setSigningBonus(BigDecimal signingBonus) {
        this.signingBonus = signingBonus;
    }

    public BigDecimal getYearlyBonus() {
        return yearlyBonus;
    }

    public void setYearlyBonus(BigDecimal yearlyBonus) {
        this.yearlyBonus = yearlyBonus;
    }

    public float getRetirementBenefits() {
        return retirementBenefits;
    }

    public void setRetirementBenefits(float retirementBenefits) {
        this.retirementBenefits = retirementBenefits;
    }

    public short getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(short leaveTime) {
        this.leaveTime = leaveTime;
    }

    public BigDecimal getJobScore() {
        return jobScore;
    }

    public void setJobScore(BigDecimal jobScore) {
        this.jobScore = jobScore;
    }
}
