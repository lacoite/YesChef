package com.example.yeschef;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class IngredientExpandableListAdapter extends BaseExpandableListAdapter{
    private HashMap<String, List<String>> mstringListHashMap;
    private String[] mListHeaderGroup;

    HashMap<Integer, Integer> childCheckboxState = new HashMap<>();
    ArrayList<String> listOfStatusFilters = new ArrayList<>();


    IngredientExpandableListAdapter(HashMap<String, List<String>> stringListHashMap){
        this.mstringListHashMap = stringListHashMap;
        this.mListHeaderGroup = mstringListHashMap.keySet().toArray(new String[0]);
    }

    @Override
    public int getGroupCount() {
        return mListHeaderGroup.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mstringListHashMap.get(mListHeaderGroup[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListHeaderGroup[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mstringListHashMap.get(mListHeaderGroup[groupPosition]).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groudPosition, int childPosition) {
        return groudPosition*childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupdPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_group, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(String.valueOf(getGroup(groupdPosition)));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_item, parent, false);
        }
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        TextView textView = convertView.findViewById(R.id.textView);

        //boolean checked = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkBox", false);
        //checkBox.setChecked(checked);


        textView.setText((String.valueOf(getChild(groupPosition, childPosition))));
        if (childCheckboxState.size() > 0) {
            if (childCheckboxState.get(childPosition) != null) {
                if (childCheckboxState.get(childPosition) == 0) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
            }
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    childCheckboxState.put(childPosition, 1);
                    //PreferenceManager.getDefaultSharedPreferences(IngredientExpandableListAdapter.this).edit().putBoolean("checkBox", true).commit();
                    Log.d("Debug", "Checkbox clicked");

                } else {
                    childCheckboxState.put(childPosition, 0);
                    //PreferenceManager.getDefaultSharedPreferences(IngredientExpandableListAdapter.this).edit().putBoolean("checkBox", false).commit();
                    Log.d("Debug", "Checkbox unclicked");
                }
                notifyDataSetChanged();

            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}