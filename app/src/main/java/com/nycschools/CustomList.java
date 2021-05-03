package com.nycschools;

import android.widget.ArrayAdapter;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String> {
    private Activity context;
    private final String[] names;
    private MainPresenter presenter;
    private MyApplication mMyApplication;
    private int mOtherContactStartingPosition = 0;

    public CustomList(MyApplication myApplication, Activity context, String[] names) {
        super(context, R.layout.list_single, names);
        this.context = context;
        this.names = names;
        mMyApplication = myApplication;
        presenter = mMyApplication.getPresenter();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View rowView = null;
        try {
            rowView = mMyApplication.getCustomList(position);
        } catch (Exception e) {
            System.out.println("exception: "+e);
        }
        TextView txtName = (TextView) rowView.findViewById(R.id.contact_name);
        txtName.setText(names[position]);
        Repository.School employee = presenter.getModel().getEmployee(Integer.toString(position));
        return rowView;
    }
}

