package edu.gatech.seclass.jobcompare6300.jobComparer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import java.util.HashMap;
import java.util.List;

import edu.gatech.seclass.jobcompare6300.R;

public class JobListAdapter extends BaseAdapter {
    private LayoutInflater jobLayoutInflater;
    private List<HashMap<String, Object>> jobOffers;
    private Context context;
    public static HashMap<Integer, Boolean> checkBoxSelected;

    public JobListAdapter(List<HashMap<String, Object>> jobOffers, Context context) {
        this.jobOffers = jobOffers;
        this.context = context;
        this.jobLayoutInflater = LayoutInflater.from(context);
        checkBoxSelected = new HashMap<Integer, Boolean>();
        initCheckBoxSelected();
    }
    public static HashMap<Integer, Boolean> getCheckBoxSelected() {
        return checkBoxSelected;
    }

    public static void setCheckBoxSelected(HashMap<Integer, Boolean> checkBoxSelected) {
        JobListAdapter.checkBoxSelected = checkBoxSelected;
    }
    private void initCheckBoxSelected() {
        for (int i = 0; i < jobOffers.size(); i++) {
            getCheckBoxSelected().put(i, false);
        }
    }

    @Override
    public int getCount() {
        return jobOffers.size();
    }

    @Override
    public Object getItem(int position) {
        return jobOffers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        jobViewHolder holder = null;
        if (convertView == null) {
            convertView = jobLayoutInflater.inflate(R.layout.jobcomparerlistview, parent, false);
            holder = new jobViewHolder();
            holder.rank = convertView.findViewById(R.id.rank);
            holder.title = convertView.findViewById(R.id.title);
            holder.company = convertView.findViewById(R.id.company);
            holder.currentJob = convertView.findViewById(R.id.currentJob);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        } else {
            holder = (jobViewHolder) convertView.getTag();
        }
        holder.title.setText((String) jobOffers.get(position).get("title"));
        holder.company.setText((String) jobOffers.get(position).get("company"));
        holder.currentJob.setText((String) jobOffers.get(position).get("currentJob"));
        holder.rank.setText((String) jobOffers.get(position).get("rank").toString());
        holder.checkBox.setChecked(getCheckBoxSelected().get(position));

        return convertView;
    }

    static class jobViewHolder {
        public TextView rank;
        public TextView title;
        public TextView company;
        public TextView currentJob;
        public CheckBox checkBox;
    }
}
