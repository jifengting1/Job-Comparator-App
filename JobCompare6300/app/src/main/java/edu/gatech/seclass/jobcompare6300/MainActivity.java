package edu.gatech.seclass.jobcompare6300;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.gatech.seclass.jobcompare6300.comparisonsettings.ComparisonSettingActivity;
import edu.gatech.seclass.jobcompare6300.currentjob.CurrentJob;
import edu.gatech.seclass.jobcompare6300.currentjob.CurrentJobActivity;
import edu.gatech.seclass.jobcompare6300.db.AppDatabase;
import edu.gatech.seclass.jobcompare6300.jobComparer.JobComparerActivity;
import edu.gatech.seclass.jobcompare6300.joboffers.JobOffer;
import edu.gatech.seclass.jobcompare6300.joboffers.JobOfferActivity;
import edu.gatech.seclass.jobcompare6300.joboffers.JobOfferActivity;

public class MainActivity extends AppCompatActivity {

    Button currentJobButton;
    Button enterJobOfferButton;
    Button comparisonSettingsButton;
    Button compareJobsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onCurrentJobClick(View view) {
        Intent currentJobIntent = new Intent(MainActivity.this, CurrentJobActivity.class);
        MainActivity.this.startActivity(currentJobIntent);

    }

    public void onJobComparisonClick(View view) {
        Intent jobComparisonIntent = new Intent(MainActivity.this, ComparisonSettingActivity.class);
        MainActivity.this.startActivity(jobComparisonIntent);
    }

    public void onJobOfferClick(View view) {
        Intent jobOfferIntent = new Intent(MainActivity.this, JobOfferActivity.class);
        MainActivity.this.startActivity(jobOfferIntent);

    }


    public void onJobComparerClick(View view) {
        int jobCount = 0;

        try {
            CurrentJob currentJob = new GetCurrentJob(this).execute().get();
            if (currentJob != null) {
                jobCount++;
            }
            List<JobOffer> jobOffers = new GetJobOffers(this).execute().get();
            if (jobOffers != null) {
                jobCount += jobOffers.size();
            }
        } catch (InterruptedException | ExecutionException e) {
            Toast.makeText(getApplicationContext(), "Error retrieving jobs",
                    Toast.LENGTH_SHORT).show();
        }

        if (jobCount < 2) {
            Toast.makeText(getApplicationContext(),
                    "Enter Current Job and 1 Job Offer or 2 Job Offers",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Intent jobComparerIntent = new Intent(MainActivity.this, JobComparerActivity.class);
        MainActivity.this.startActivity(jobComparerIntent);
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

    public static class GetJobOffers extends AsyncTask<Void, Void, List<JobOffer>> {
        private WeakReference<Context> context;

        public GetJobOffers(Context context) {
            this.context = new WeakReference<>(context);
        }

        @Override
        protected List<JobOffer> doInBackground(Void... voids) {
            AppDatabase db = AppDatabase.getInstance(context.get());
            return db.JobOfferDao().getAll();
        }

        @Override
        protected void onPostExecute(List<JobOffer> jobOffers) {
            super.onPostExecute(jobOffers);
        }
    }

}
