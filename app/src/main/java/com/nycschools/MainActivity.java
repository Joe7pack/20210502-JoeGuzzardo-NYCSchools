package com.nycschools;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MvpView {

    private MainPresenter presenter;
    private ListView mListView;
    private int mEmployeeSelected;
    private static boolean listViewLoaded;
    private MyApplication myApplication;
    static private LayoutInflater inflater;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list);
        mListView.setClickable(true);
        myApplication = ((MyApplication) this.getApplication());
        myApplication.setListView(mListView);
        myApplication.setContext(this);
        mContext = this;

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object o = mListView.getItemAtPosition(position);
                mEmployeeSelected = position;
                myApplication.setEmployeeId(Integer.toString(position));
                showEmployeeDetail();
            }
        });

        presenter = new MainPresenter(this);
        presenter.setContext(this);
        inflater = this.getLayoutInflater();

        ((MyApplication) this.getApplication()).setPresenter(presenter);
        new DownloadFilesTask(this).execute("one", "two", "three");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showEmployeeDetail() {
        Intent i = new Intent(this, EmployeeDetailActivity.class);
        i.putExtra(EmployeeDetailActivity.ID, Integer.toString(mEmployeeSelected));
        startActivity(i);
    }

    private class DownloadFilesTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;
        private List<Repository.School> employeeList;

        public DownloadFilesTask(MainActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading data, please wait.");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            for (int i = 0; i < 30; i++) {
                try {
                    if (presenter.getDataLoaded()) {
                        employeeList = presenter.getEmployeeList();
                        if (listViewLoaded) {
                            return null;
                        }
                        loadListView(employeeList);
                    }
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            // do UI work here
            mListView.setAdapter(myApplication.getmAdapter());
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        private void loadListView(List<Repository.School> employeeList) {
            String[] listNames = new String[employeeList.size()];
            List<View> customList = new ArrayList<View>();

            for (int x=0; x<employeeList.size(); x++) {
                Repository.School employee = employeeList.get(x);
                listNames[x] = employee.school_name;
                myApplication.setEmployeeIdByPosition(Integer.toString(x), employee.school_name);
                View rowView = inflater.inflate(R.layout.list_single, null, false);
                customList.add(rowView);
            }

            myApplication.setCustomList(customList);
            CustomList adapter = new CustomList(myApplication,MainActivity.this, listNames);
            myApplication.setListViewAdapter(adapter);
            listViewLoaded = true;
        }
    }

    public void loadListView2(List<Repository.School> employeeList) {
        String[] listNames = new String[employeeList.size()];
        List<View> customList = new ArrayList<View>();

        for (int x=0; x<employeeList.size(); x++) {
            Repository.School employee = employeeList.get(x);
            listNames[x] = employee.school_name;
            myApplication.setEmployeeIdByPosition(Integer.toString(x), employee.school_name);
            View rowView = inflater.inflate(R.layout.list_single, null, false);
            customList.add(rowView);
        }
        myApplication.setCustomList(customList);
        CustomList adapter = new CustomList(myApplication,MainActivity.this, listNames);
        myApplication.setListViewAdapter(adapter);
        mListView.setAdapter(adapter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}