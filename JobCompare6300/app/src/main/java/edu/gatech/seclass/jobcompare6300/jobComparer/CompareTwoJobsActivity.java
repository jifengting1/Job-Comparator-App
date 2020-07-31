package edu.gatech.seclass.jobcompare6300.jobComparer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.gatech.seclass.jobcompare6300.MainActivity;
import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.job.Job;
import edu.gatech.seclass.jobcompare6300.joboffers.JobOfferActivity;
import edu.gatech.seclass.jobcompare6300.util.CostOfLivingAdjuster;

public class CompareTwoJobsActivity extends AppCompatActivity {
    private List<HashMap<String, Object>> twoSelectedJobs = new ArrayList<>();
    public String flag;
    public List<Job> jobSelectedtoCompare = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_two_jobs);
        Intent intent = getIntent();
        flag = intent.getExtras().getString("Source");
        if (flag.equals("from Job Rank")) {
            jobSelectedtoCompare = JobComparerActivity.getJobSelected();
        }
        else if (flag.equals("from Job offer interface")) {
            jobSelectedtoCompare = JobOfferActivity.getJobofferandCurrentjob();
        }
        Context context = CompareTwoJobsActivity.this;
        ListView listViewCompareTwo = (ListView) findViewById(R.id.compareTwoJobsListview);
        initData();
        CompareTwoJobsAdapter adapter = new CompareTwoJobsAdapter(twoSelectedJobs, context);
        listViewCompareTwo.setAdapter(adapter);

    }

    private void initData() {

        String[] jobDetailName = {"Title","Company", "Location", "Yearly Salary Adjusted", "Signing Bonus Adjusted", "Yearly Bonus Adjusted", "Retirement Benefits", "Leave Time"};
        String[] jobSelectedOneStr = adjustedJob(jobSelectedtoCompare.get(0));
        String[] jobSelectedTwoStr = adjustedJob(jobSelectedtoCompare.get(1));
        Log.v("Tag", "I am here let do it");
        for (int i = 0; i < jobDetailName.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("jobDetailName", jobDetailName[i]);
            map.put("jobSelectedOne",jobSelectedOneStr[i]);
            map.put("jobSelectedTwo",jobSelectedTwoStr[i]);
            twoSelectedJobs.add(map);
        }
    }

    private String[] adjustedJob(Job job) {
        BigDecimal salary = job.getSalary();
        BigDecimal signingBonus = job.getSigningBonus();
        BigDecimal yearlyBonus = job.getYearlyBonus();
        Short costOfLivingIndex = job.getCostOfLivingIndex();
        BigDecimal AYS = CostOfLivingAdjuster.calculateAdjustedAmount(salary, BigDecimal.valueOf(costOfLivingIndex));
        BigDecimal ASB = CostOfLivingAdjuster.calculateAdjustedAmount(signingBonus, BigDecimal.valueOf(costOfLivingIndex));
        BigDecimal AYB = CostOfLivingAdjuster.calculateAdjustedAmount(yearlyBonus, BigDecimal.valueOf(costOfLivingIndex));
        String[] adjustedJobStr = {String.valueOf(job.getTitle()), String.valueOf(job.getCompany()), (String.valueOf(job.getLocation().getCity())+ ", " +String.valueOf(job.getLocation().getState())), AYS.toString(),ASB.toString(),AYB.toString(), String.valueOf(job.getRetirementBenefits()) + " %", String.valueOf(job.getLeaveTime())};

        return adjustedJobStr;
    }

    public void onNewComparisonClick(View view) {
        finish();
    }

    public void onReturnToMainClick(View view) {
        Intent returnToMain = new Intent(CompareTwoJobsActivity.this, MainActivity.class);
        startActivity(returnToMain);
    }

    public void onBackClick(View view) {
            finish();
        }
}