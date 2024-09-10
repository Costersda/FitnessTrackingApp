package com.example.fitnesstrackingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fitnesstrackingapp.R;
import com.example.fitnesstrackingapp.dataModel.StepHistoryItem;

import java.util.List;

public class StepHistoryAdapter extends ArrayAdapter<StepHistoryItem> {

    private Context context;
    private List<StepHistoryItem> stepHistoryList;

    public StepHistoryAdapter(List<StepHistoryItem> stepHistoryList, Context context) {
        super(context, 0, stepHistoryList);
        this.context = context;
        this.stepHistoryList = stepHistoryList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item_step_history, parent, false);
        }

        StepHistoryItem stepHistoryItem = stepHistoryList.get(position);

        TextView tvDate = itemView.findViewById(R.id.tvStepHistoryDate);
        TextView tvStepCount = itemView.findViewById(R.id.tvStepHistorySteps);

        tvDate.setText(stepHistoryItem.getDate());
        tvStepCount.setText("Steps: " + stepHistoryItem.getStepCount());

        return itemView;
    }

    public void updateData(List<StepHistoryItem> newData) {
        clear();
        addAll(newData);
    }

    public static void setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int itemCount = listAdapter.getCount();
        for (int i = 0; i < itemCount; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (itemCount - 1));
        listView.setLayoutParams(params);
    }

}