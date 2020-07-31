package edu.gatech.seclass.jobcompare6300.currentjob;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.comparisonsettings.CurrentComparisonSetting;
import edu.gatech.seclass.jobcompare6300.db.AppDatabase;
import edu.gatech.seclass.jobcompare6300.job.Location;
import edu.gatech.seclass.jobcompare6300.jobranker.JobRanker;

public class CurrentJobActivity extends AppCompatActivity {
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

    CurrentJob currentJob;
    CurrentComparisonSetting currentComparisonSetting;
    CurrentJobValidator currentJobValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_job);

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

        try {
            currentJob = new GetCurrentJob(CurrentJobActivity.this).execute().get();
        }
        catch (InterruptedException | ExecutionException e) {
            Toast.makeText(getApplicationContext(), "Error populating current job details",
                    Toast.LENGTH_SHORT).show();
        }

        try {
            currentComparisonSetting = new GetCurrentComparisonSetting(this).execute().get();
        }
        catch (InterruptedException | ExecutionException e) {
            Toast.makeText(getApplicationContext(), "Error retrieving comparison settings",
                    Toast.LENGTH_SHORT).show();
        }

        currentJobValidator = new CurrentJobValidator();
    }

    public void save() {
        if (!validateFields()) {
            return;
        }
        try {
            String title = titleEditText.getText().toString();
            String company = companyEditText.getText().toString();
            String city = cityEditText.getText().toString();
            String state = stateEditText.getText().toString();
            Location location = new Location(city, state);
            short costOfLivingIndex = Short.parseShort(colEditText.getText().toString());
            BigDecimal salary = new BigDecimal(salaryEditText.getText().toString());
            BigDecimal yearlyBonus = new BigDecimal(yearlyBonusEditText.getText().toString());
            BigDecimal signingBonus = new BigDecimal(signingBonusEditText.getText().toString());
            float retirementBenefits = Float.parseFloat(benefitsEditText.getText().toString());
            short leaveTime = Short.parseShort(leaveTimeEditText.getText().toString());


            if (currentJob == null) {
                currentJob = new CurrentJob(title, company, location, costOfLivingIndex, salary,
                        yearlyBonus, signingBonus, retirementBenefits, leaveTime);

                BigDecimal jobScore = JobRanker.calculateJobScore(currentJob, currentComparisonSetting);
                currentJob.setJobScore(jobScore);

                new AddCurrentJob(getApplicationContext(), currentJob).execute();
            } else {
                currentJob.setTitle(title);
                currentJob.setCompany(company);
                currentJob.setLocation(location);
                currentJob.setCostOfLivingIndex(costOfLivingIndex);
                currentJob.setSalary(salary);
                currentJob.setYearlyBonus(yearlyBonus);
                currentJob.setSigningBonus(signingBonus);
                currentJob.setRetirementBenefits(retirementBenefits);
                currentJob.setLeaveTime(leaveTime);
                currentJob.setJobScore(JobRanker.calculateJobScore(currentJob, currentComparisonSetting));
                new UpdateCurrentJob(getApplicationContext(), currentJob).execute();

            }
            finish();
        } catch (NullPointerException | NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Please fill in all the information",
                    Toast.LENGTH_SHORT).show();
        };

    }

    private boolean validateFields() {
        boolean isValidTitle = currentJobValidator.isValidField(titleEditText);
        boolean isValidCompany = currentJobValidator.isValidField(companyEditText);
        boolean isValidCity = currentJobValidator.isValidField(cityEditText);
        boolean isValidState = currentJobValidator.isValidField(stateEditText);
        boolean isValidCol = currentJobValidator.isValidField(colEditText);
        boolean isValidSalary = currentJobValidator.isValidField(salaryEditText);
        boolean isValidYearlyBonus = currentJobValidator.isValidField(yearlyBonusEditText);
        boolean isValidSigningBonus = currentJobValidator.isValidField(signingBonusEditText);
        boolean isValidBenefits = currentJobValidator.isValidField(benefitsEditText);
        boolean isValidLeaveTime = currentJobValidator.isValidField(leaveTimeEditText);

        return isValidTitle
                && isValidCompany
                && isValidCity
                && isValidState
                && isValidCol
                && isValidSalary
                && isValidYearlyBonus
                && isValidSigningBonus
                && isValidBenefits
                && isValidLeaveTime;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                save();
                break;
            case R.id.back:
                finish();
                break;
        }
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

            if (currentJob == null) {
                return;
            }

            EditText titleEditText = ((Activity) context.get()).findViewById(R.id.titleEditText);
            EditText companyEditText = ((Activity) context.get()).findViewById(R.id.companyEditText);
            EditText cityEditText = ((Activity) context.get()).findViewById(R.id.cityEditText);
            EditText stateEditText = ((Activity) context.get()).findViewById(R.id.stateEditText);
            EditText colEditText = ((Activity) context.get()).findViewById(R.id.colEditText);
            EditText salaryEditText = ((Activity) context.get()).findViewById(R.id.salaryEditText);
            EditText yearlyBonusEditText = ((Activity) context.get()).findViewById(R.id.yearlyBonusEditText);
            EditText signingBonusEditText = ((Activity) context.get()).findViewById(R.id.signingBonusEditText);
            EditText benefitsEditText = ((Activity) context.get()).findViewById(R.id.benefitsEditText);
            EditText leaveTimeEditText = ((Activity) context.get()).findViewById(R.id.leaveTimeEditText);

            titleEditText.setText(currentJob.getTitle());
            companyEditText.setText(currentJob.getCompany());
            cityEditText.setText(currentJob.getLocation().getCity());
            stateEditText.setText(currentJob.getLocation().getState());
            colEditText.setText(String.valueOf(currentJob.getCostOfLivingIndex()));
            salaryEditText.setText(currentJob.getSalary().toString());
            yearlyBonusEditText.setText(currentJob.getYearlyBonus().toString());
            signingBonusEditText.setText(currentJob.getSigningBonus().toString());
            benefitsEditText.setText(String.valueOf(currentJob.getRetirementBenefits()));
            leaveTimeEditText.setText(String.valueOf(currentJob.getLeaveTime()));
        }
    }

    static class AddCurrentJob extends AsyncTask<Void, Void, Void> {
        CurrentJob currentJob;
        private WeakReference<Context> context;

        public AddCurrentJob(Context context, CurrentJob currentJob) {
            this.context = new WeakReference<>(context);
            this.currentJob = currentJob;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AppDatabase db = AppDatabase.getInstance(context.get());
            db.currentJobDao().insertCurrentJob(currentJob);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context.get(), "Current Job added successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    static class UpdateCurrentJob extends AsyncTask<Void, Void, Void> {
        private CurrentJob currentJob;
        private WeakReference<Context> context;

        public UpdateCurrentJob(Context context, CurrentJob currentJob) {
            this.context = new WeakReference<>(context);
            this.currentJob = currentJob;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AppDatabase db = AppDatabase.getInstance(context.get());
            db.currentJobDao().updateCurrentJob(currentJob);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context.get(), "Current Job updated successfully!", Toast.LENGTH_SHORT).show();
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

}
