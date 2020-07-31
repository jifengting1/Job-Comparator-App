package edu.gatech.seclass.jobcompare6300.jobComparer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import edu.gatech.seclass.jobcompare6300.R;

public class CompareTwoJobsAdapter extends BaseAdapter {
    private LayoutInflater compareTwoJobsLayoutInflater;
    private List<HashMap<String, Object>> twoSelectedJobs;
    private Context context;

    public CompareTwoJobsAdapter(List<HashMap<String, Object>> twoSelectedJobs, Context context) {
        this.twoSelectedJobs = twoSelectedJobs;
        this.context = context;
        this.compareTwoJobsLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return twoSelectedJobs.size();
    }

    @Override
    public Object getItem(int position) {
        return twoSelectedJobs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        compareTwoJobsHolder holder = null;
        if (convertView == null) {
            convertView = compareTwoJobsLayoutInflater.inflate(R.layout.comparetwojobslistview,parent, false);
            holder = new compareTwoJobsHolder();
            holder.jobDetailName = convertView.findViewById(R.id.jobDetailName);
            holder.jobSelectedOne = convertView.findViewById(R.id.jobSelectedOne);
            holder.jobSelectedTwo = convertView.findViewById(R.id.jobSelectedTwo);
            convertView.setTag(holder);
        } else {
            holder = (compareTwoJobsHolder) convertView.getTag();
        }
        holder.jobDetailName.setText((String) twoSelectedJobs.get(position).get("jobDetailName"));
        holder.jobSelectedOne.setText(twoSelectedJobs.get(position).get("jobSelectedOne").toString());
        holder.jobSelectedTwo.setText(twoSelectedJobs.get(position).get("jobSelectedTwo").toString());

        return convertView;
    }



    static class compareTwoJobsHolder {
        public TextView jobDetailName;
        public TextView jobSelectedOne;
        public TextView jobSelectedTwo;
    }
}
