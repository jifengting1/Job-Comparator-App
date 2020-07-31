package edu.gatech.seclass.jobcompare6300.jobComparer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.comparisonsettings.CurrentComparisonSetting;
import edu.gatech.seclass.jobcompare6300.currentjob.CurrentJob;
import edu.gatech.seclass.jobcompare6300.db.AppDatabase;
import edu.gatech.seclass.jobcompare6300.job.Job;
import edu.gatech.seclass.jobcompare6300.job.Location;
import edu.gatech.seclass.jobcompare6300.joboffers.JobOffer;
import edu.gatech.seclass.jobcompare6300.jobComparer.JobListAdapter.jobViewHolder;
import edu.gatech.seclass.jobcompare6300.jobranker.JobRanker;

public class JobComparerActivity extends AppCompatActivity {

    public List<JobOffer> jobOffers;
    public CurrentJob currentJob;
    public List<Job> jobOffersAndCurrentJob = new ArrayList<>();
    List<HashMap<String, Object>> listJob = new ArrayList<>();
    public int checkedSum = 0;
    public List<Integer> jobNumSelected = new ArrayList<>();
    public static List<Job> jobSelected = new ArrayList<>();
//  Button button1


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_comparer);
        Context context = JobComparerActivity.this;
        ListView listViewJob = (ListView) findViewById(R.id.jobcomparerlistview);
        initData();
        JobListAdapter jobListAdapter = new JobListAdapter(listJob, context);
        listViewJob.setAdapter(jobListAdapter);
        listViewJob.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jobViewHolder holder = (jobViewHolder) view.getTag();
                holder.checkBox.toggle();
                JobListAdapter.getCheckBoxSelected().put(position, holder.checkBox.isChecked());
                if(holder.checkBox.isChecked()) {
                    checkedSum++;
                    jobNumSelected.add(0, position);
                    jobSelected.add(0,jobOffersAndCurrentJob.get(position));
                } else {
                    checkedSum--;
                    for (int i = 0, len = jobNumSelected.size(); i < len;i++) {
                        if (position == jobNumSelected.get(i)) {
                            jobNumSelected.remove(i);
                            len--;
                            i--;
                        }
                    }
                    for (int i = 0, len = jobSelected.size(); i < len;i++) {
                        if (jobOffersAndCurrentJob.get(position) == jobSelected.get(i)) {
                            jobSelected.remove(i);
                            len--;
                            i--;
                        }
                    }
                }
            }
        });

    }


    private void initData() {
        //get joboffer list and current from database
        try {
            jobOffers = new GetJobOffers(JobComparerActivity.this).execute().get();
        }
        catch (InterruptedException | ExecutionException e) {
            Toast.makeText(getApplicationContext(), "Error populating job rank",
                    Toast.LENGTH_SHORT).show();
        }

        try {
            currentJob = new GetCurrentJob(JobComparerActivity.this).execute().get();
        }
        catch (InterruptedException | ExecutionException e) {
            Toast.makeText(getApplicationContext(), "Error populating job rank",
                    Toast.LENGTH_SHORT).show();
        }
        int len = jobOffers.size();
        for (int i = 0; i < len; i++) {
            jobOffersAndCurrentJob.add(jobOffers.get(i));
        }
        if (currentJob != null) {
            jobOffersAndCurrentJob.add(currentJob);
        }
        try {
            CurrentComparisonSetting currentComparisonSetting = new GetCurrentComparisonSetting(getApplicationContext()).execute().get();
            for (Job job : jobOffersAndCurrentJob) {
                job.setJobScore(JobRanker.calculateJobScore(job, currentComparisonSetting));
            }
        }
        catch (ExecutionException | InterruptedException e) {
            Toast.makeText(getApplicationContext(), "Error retrieving comparison settings",
                    Toast.LENGTH_SHORT).show();
        }


        Collections.sort(jobOffersAndCurrentJob, new Comparator<Job>() {
            @Override
            public int compare(Job o1, Job o2) {
                return o2.getJobScore().compareTo(o1.getJobScore());
            }
        });

        try {
            for (int i = 0; i < jobOffersAndCurrentJob.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();
                String title = jobOffersAndCurrentJob.get(i).getTitle();
                String company = jobOffersAndCurrentJob.get(i).getCompany();
                map.put("rank", i+1);
                map.put("title", "Title: " + title);
                map.put("company","Company: " + company);
                if (jobOffersAndCurrentJob.get(i) instanceof CurrentJob) {
                    map.put("currentJob", "current");
                }
                listJob.add(map);
            }
        }
        catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "No Job Rank available",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public static List<Job> getJobSelected() {
        return jobSelected;
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.jobRankBack:
                finish();
                break;
            case R.id.compareTwoJobButton:
                if (checkedSum == 2) {
                    StringBuilder stringBuilder = new StringBuilder();
                    Collections.sort(jobNumSelected);
                    for (int i = 0; i < jobNumSelected.size();i++) {
                        stringBuilder.append(jobNumSelected.get(i));
                    }
//                    Toast.makeText(this, "Job " + stringBuilder.toString()+ " selected.", Toast.LENGTH_SHORT).show();
                    Intent compareTwoJob = new Intent(JobComparerActivity.this, CompareTwoJobsActivity.class);
                    compareTwoJob.putExtra("Source", "from Job Rank");
                    startActivity(compareTwoJob);
                } else if (checkedSum == 1) {
                    Toast.makeText(this, "Job Rank #" + String.valueOf(jobNumSelected.get(0) + 1) + " selected. Choose 1 more job to compare", Toast.LENGTH_SHORT).show();
                } else if (checkedSum == 0) {
                    Toast.makeText(this, "Choose 2 jobs to compare", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You can only choose 2 jobs to compare", Toast.LENGTH_SHORT).show();
                }

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
        }
    }
}
