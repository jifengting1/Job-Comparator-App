package edu.gatech.seclass.jobcompare6300.joboffers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.comparisonsettings.CurrentComparisonSetting;
import edu.gatech.seclass.jobcompare6300.currentjob.CurrentJob;
import edu.gatech.seclass.jobcompare6300.db.AppDatabase;
import edu.gatech.seclass.jobcompare6300.job.Job;
import edu.gatech.seclass.jobcompare6300.job.Location;
import edu.gatech.seclass.jobcompare6300.jobComparer.CompareTwoJobsActivity;
import edu.gatech.seclass.jobcompare6300.jobranker.JobRanker;

public class JobOfferActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText companyEditText;
    private EditText cityEditText;
    private EditText stateEditText;
    private EditText colEditText;
    private EditText salaryEditText;
    private EditText yearlyBonusEditText;
    private EditText signingBonusEditText;
    private EditText benefitsEditText;
    private EditText leaveTimeEditText;
    public CurrentJob currentJob;
    public static List<Job> CurrentjobCompareJoboffer;
    public List<Job> jobOffersAndCurrentJob = new ArrayList<>();

    JobOffer jobOffer;
    CurrentComparisonSetting currentComparisonSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_offer);

        titleEditText = findViewById(R.id.titleEditText);
        companyEditText = findViewById(R.id.companyEditText);
        cityEditText = findViewById(R.id.cityEditText);
        stateEditText = findViewById(R.id.stateEditText);
        colEditText = findViewById(R.id.colEditText);
        salaryEditText = findViewById(R.id.salaryEditText);
        yearlyBonusEditText = findViewById(R.id.yearlyBonusEditText);
        signingBonusEditText = findViewById(R.id.signingBonusEditText);
        benefitsEditText = findViewById(R.id.benefitsEditText);
        leaveTimeEditText = findViewById(R.id.leaveTimeEditText);
    }

    public void save() {
        String title = titleEditText.getText().toString();
        String company = companyEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String state = stateEditText.getText().toString();
        Location location = new Location(city, state);
        try {
            short costOfLivingIndex = Short.parseShort(colEditText.getText().toString());
            BigDecimal salary = new BigDecimal(salaryEditText.getText().toString());
            BigDecimal yearlyBonus = new BigDecimal(yearlyBonusEditText.getText().toString());
            BigDecimal signingBonus = new BigDecimal(signingBonusEditText.getText().toString());
            float retirementBenefits = Float.parseFloat(benefitsEditText.getText().toString());
            short leaveTime = Short.parseShort(leaveTimeEditText.getText().toString());
            if (title.equals("") || company.equals("") || city.equals("") || state.equals("") || location.equals("")) {
                Toast.makeText(getApplicationContext(), "Please fill in all the information", Toast.LENGTH_SHORT).show();
            } else {
                jobOffer = new JobOffer(title, company, location, costOfLivingIndex, salary,
                        yearlyBonus, signingBonus, retirementBenefits, leaveTime);
                jobOffer.setJobScore(JobRanker.calculateJobScore(jobOffer, currentComparisonSetting));
                new AddJobOffer(getApplicationContext(), jobOffer).execute();
            }
        } catch (NullPointerException | NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Please fill in all the information", Toast.LENGTH_SHORT).show();
        }
    }
    public void read() {
        String title = titleEditText.getText().toString();
        String company = companyEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String state = stateEditText.getText().toString();
        Location location = new Location(city, state);
        try {
            short costOfLivingIndex = Short.parseShort(colEditText.getText().toString());
            BigDecimal salary = new BigDecimal(salaryEditText.getText().toString());
            BigDecimal yearlyBonus = new BigDecimal(yearlyBonusEditText.getText().toString());
            BigDecimal signingBonus = new BigDecimal(signingBonusEditText.getText().toString());
            float retirementBenefits = Float.parseFloat(benefitsEditText.getText().toString());
            short leaveTime = Short.parseShort(leaveTimeEditText.getText().toString());
            if (title.equals("") || company.equals("") || city.equals("") || state.equals("") || location.equals("")) {
                Toast.makeText(getApplicationContext(), "Please fill in all the information", Toast.LENGTH_SHORT).show();
            } else {
                jobOffer = new JobOffer(title, company, location, costOfLivingIndex, salary,
                        yearlyBonus, signingBonus, retirementBenefits, leaveTime);
                jobOffer.setJobScore(JobRanker.calculateJobScore(jobOffer, currentComparisonSetting));
            }
        } catch (NullPointerException | NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Please fill in all the information", Toast.LENGTH_SHORT).show();
        }
    }

    public void back() {
        finish();
    }

    public void onAddAnotherClick(View view) {
        titleEditText.setText("");
        companyEditText.setText("");
        cityEditText.setText("");
        stateEditText.setText("");
        colEditText.setText("");
        salaryEditText.setText("");
        yearlyBonusEditText.setText("");
        signingBonusEditText.setText("");
        benefitsEditText.setText("");
        leaveTimeEditText.setText("");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                save();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.compareCurrentjobButton:
//                finish();
                getJobofferandCurrentjob_void();
                if (CurrentjobCompareJoboffer == null || CurrentjobCompareJoboffer.size() == 0 || CurrentjobCompareJoboffer.get(0) == null) {
                    Toast.makeText(getApplicationContext(), "No Current Job Available",Toast.LENGTH_SHORT).show();
                } else if (CurrentjobCompareJoboffer.size() < 2) {
                    Toast.makeText(getApplicationContext(), "Please fill in all the information",Toast.LENGTH_SHORT).show();
                }else {
                    Intent compareCurrentJob = new Intent(this, CompareTwoJobsActivity.class);
                    compareCurrentJob.putExtra("Source", "from Job offer interface");
                    startActivity(compareCurrentJob);
                }
        }
    }

    static class AddJobOffer extends AsyncTask<Void, Void, Void> {
        JobOffer jobOffer;
        private WeakReference<Context> context;

        public AddJobOffer(Context context, JobOffer jobOffer) {
            this.context = new WeakReference<>(context);
            this.jobOffer = jobOffer;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AppDatabase db = AppDatabase.getInstance(context.get());
            db.JobOfferDao().insertJobOffer(jobOffer);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context.get(), "Job offer added successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    static class GetCurrentComparisonSetting extends AsyncTask<Void, Void, CurrentComparisonSetting> {
        private WeakReference<Context> context;

        public GetCurrentComparisonSetting(Context context) {
            this.context = new WeakReference<>(context);
        }

        @Override
        protected CurrentComparisonSetting doInBackground(Void... voids) {
            AppDatabase db = AppDatabase.getInstance(context.get());
            return db.comparisonSettingDao().findOne();
        }

        @Override
        protected void onPostExecute(CurrentComparisonSetting currentComparisonSetting) {
            super.onPostExecute(currentComparisonSetting);
        }
    }


    public void getJobofferandCurrentjob_void() {
        //get and current job from database
        CurrentjobCompareJoboffer = new ArrayList<>();
        read();
        try {
            currentJob = new GetCurrentJob(JobOfferActivity.this).execute().get();
        }
        catch (InterruptedException | ExecutionException | NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Error populating current job",
                    Toast.LENGTH_SHORT).show();
        }
        if (currentJob != null) {
            CurrentjobCompareJoboffer.add(0,currentJob);
            if (jobOffer != null) {
                CurrentjobCompareJoboffer.add(1,jobOffer);
            }
        }

    }

    public static List<Job> getJobofferandCurrentjob() {
        return CurrentjobCompareJoboffer;
    }

    static class GetCurrentJob extends AsyncTask<Void, Void, CurrentJob> {
        private WeakReference<Context> context;

        public GetCurrentJob(Context context) {
            this.context = new WeakReference<>(context);
        }

        @Override
        protected CurrentJob doInBackground(Void... voids) {
            AppDatabase db = AppDatabase.getInstance(context.get());
            return db.currentJobDao().findOne();
        }

        @Override
        protected void onPostExecute(CurrentJob currentJob) {
            super.onPostExecute(currentJob);

        }
    }
}
