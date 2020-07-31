package edu.gatech.seclass.jobcompare6300.comparisonsettings;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.currentjob.CurrentJob;
import edu.gatech.seclass.jobcompare6300.db.AppDatabase;
import edu.gatech.seclass.jobcompare6300.joboffers.JobOffer;
import edu.gatech.seclass.jobcompare6300.jobranker.JobRanker;

public class ComparisonSettingActivity extends AppCompatActivity {
    private EditText salaryCompareEditText;
    private EditText signingBonusCompareEditText;
    private EditText yearlyBonusCompareEditText;
    private EditText benefitCompareEditText;
    private EditText leaveTimeCompareEditText;

    CurrentComparisonSetting currentComparisonSetting;

    @Override
    protected void onCreate(Bundle savedComparisonState) {
        super.onCreate(savedComparisonState);
        setContentView(R.layout.activity_comparison_setting);

        salaryCompareEditText = findViewById(R.id.salaryCompareEditText);
        signingBonusCompareEditText = findViewById(R.id.signingBonusCompareEditText);
        yearlyBonusCompareEditText = findViewById(R.id.yearlyBonusCompareEditText);
        benefitCompareEditText = findViewById(R.id.benefitCompareEditText);
        leaveTimeCompareEditText = findViewById(R.id.leaveTimeCompareEditText);
        salaryCompareEditText.setText("1");
        signingBonusCompareEditText = findViewById(R.id.signingBonusCompareEditText);
        signingBonusCompareEditText.setText("1");
        yearlyBonusCompareEditText = findViewById(R.id.yearlyBonusCompareEditText);
        yearlyBonusCompareEditText.setText("1");
        benefitCompareEditText = findViewById(R.id.benefitCompareEditText);
        benefitCompareEditText.setText("1");
        leaveTimeCompareEditText = findViewById(R.id.leaveTimeCompareEditText);
        leaveTimeCompareEditText.setText("1");

        try {
            currentComparisonSetting = new GetCurrentComparisonSetting(ComparisonSettingActivity.this).execute().get();
        }
        catch (InterruptedException | ExecutionException e) {
            Toast.makeText(getApplicationContext(), "Error populating comparison settings",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void save() {
        int salary = 0;
        int yearlyBonus = 0;
        int signingBonus = 0;
        int retirementBenefits = 0;
        int leaveTime = 0;

        if (!salaryCompareEditText.getText().toString().equals("")){
            salary = Integer.parseInt(salaryCompareEditText.getText().toString()); } else{
            salaryCompareEditText.setError("Invalid Weight Number");
        }
        if (!yearlyBonusCompareEditText.getText().toString().equals("")){
            yearlyBonus = Integer.parseInt(yearlyBonusCompareEditText.getText().toString());
        } else {
            yearlyBonusCompareEditText.setError("Invalid Weight Number");
        }
        if (!signingBonusCompareEditText.getText().toString().equals("")){
            signingBonus = Integer.parseInt(signingBonusCompareEditText.getText().toString()); }
        else{
            signingBonusCompareEditText.setError("Invalid Weight Number");
        }
        if (!benefitCompareEditText.getText().toString().equals("")){
            retirementBenefits = Integer.parseInt(benefitCompareEditText.getText().toString());}
        else{
            benefitCompareEditText.setError("Invalid Weight Number");
        }
        if (!leaveTimeCompareEditText.getText().toString().equals("")){
            leaveTime = Integer.parseInt(leaveTimeCompareEditText.getText().toString());}
        else{
            leaveTimeCompareEditText.setError("Invalid Weight Number");
        }

        if (currentComparisonSetting == null) {
            currentComparisonSetting = new CurrentComparisonSetting(salary,
                    yearlyBonus, signingBonus, retirementBenefits, leaveTime);

            new AddComparisonSetting(getApplicationContext(), currentComparisonSetting).execute();
        }
        else {
            currentComparisonSetting.setSalary(salary);
            currentComparisonSetting.setYearlyBonus(yearlyBonus);
            currentComparisonSetting.setSigningBonus(signingBonus);
            currentComparisonSetting.setRetirementBenefits(retirementBenefits);
            currentComparisonSetting.setLeaveTime(leaveTime);

            new UpdateComparisonSetting(getApplicationContext(), currentComparisonSetting).execute();
        }
    }

    public void back() {
        finish();
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


    public static class GetCurrentComparisonSetting extends AsyncTask<Void, Void, CurrentComparisonSetting> {
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

            if (currentComparisonSetting == null) {
                return;
            }
            EditText salaryCompareEditText = ((Activity) context.get()).findViewById(R.id.salaryCompareEditText);
            EditText yearlyBonusCompareEditText = ((Activity) context.get()).findViewById(R.id.yearlyBonusCompareEditText);
            EditText signingBonusCompareEditText = ((Activity) context.get()).findViewById(R.id.signingBonusCompareEditText);
            EditText benefitCompareEditText = ((Activity) context.get()).findViewById(R.id.benefitCompareEditText);
            EditText leaveTimeCompareEditText = ((Activity) context.get()).findViewById(R.id.leaveTimeCompareEditText);

            salaryCompareEditText.setText(String.valueOf(currentComparisonSetting.getSalary()));
            yearlyBonusCompareEditText.setText(String.valueOf(currentComparisonSetting.getYearlyBonus()));
            signingBonusCompareEditText.setText(String.valueOf(currentComparisonSetting.getSigningBonus()));
            benefitCompareEditText.setText(String.valueOf(currentComparisonSetting.getRetirementBenefits()));
            leaveTimeCompareEditText.setText(String.valueOf(currentComparisonSetting.getLeaveTime()));

        }
    }

    static class AddComparisonSetting extends AsyncTask<Void, Void, Void> {
        CurrentComparisonSetting currentComparisonSetting;
        private WeakReference<Context> context;

        public AddComparisonSetting (Context context, CurrentComparisonSetting currentComparisonSetting) {
            this.context = new WeakReference<>(context);
            this.currentComparisonSetting  = currentComparisonSetting;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AppDatabase db = AppDatabase.getInstance(context.get());
            db.comparisonSettingDao().insertComparisonSetting(currentComparisonSetting);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context.get(), "Comparison Weights added successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    static class UpdateComparisonSetting extends AsyncTask<Void, Void, Void> {
        CurrentComparisonSetting currentComparisonSetting;
        private WeakReference<Context> context;

        public UpdateComparisonSetting(Context context, CurrentComparisonSetting currentComparisonSetting) {
            this.context = new WeakReference<>(context);
            this.currentComparisonSetting = currentComparisonSetting;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AppDatabase db = AppDatabase.getInstance(context.get());
            db.comparisonSettingDao().updateComparisonSetting(currentComparisonSetting);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context.get(), "Comparison Weights updated successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}
