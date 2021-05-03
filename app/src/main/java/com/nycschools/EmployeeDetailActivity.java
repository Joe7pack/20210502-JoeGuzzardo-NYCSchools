package com.nycschools;

import android.content.res.Resources;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;

public class EmployeeDetailActivity extends AppCompatActivity{
    private MainPresenter presenter;
    private static final String packageName = "com.nycschools";
    private MyApplication myApplication;
    static private Resources mResources;

    private static final int START_DOWNLOAD_MSG = 1001;
    private static final int UPDATE_PROGRESS_MSG = 2002;
    private static final int TOGGLE_START_BTN_MSG = 3003;
    private static final int DOWNLOAD_COMPLETED = 3004;
    private static final int ALL_DONE = 3005;

    public static final String ID = packageName + ".EmployeeDetailActivity.ID";
    private static final String TAG = "EmployeeDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mResources = getResources();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_detail);
        myApplication = ((MyApplication) this.getApplication());
        String positionSelected = getIntent().getStringExtra(ID);
        presenter = ((MyApplication)this.getApplication()).getPresenter();
        String employeeId = myApplication.getEmployeeIdByPosition(positionSelected);
        Repository.School employeeData = presenter.getModel().getEmployee(employeeId);
        TextView name = this.findViewById(R.id.name);
        name.setText(employeeData.school_name);

        TextView reading = this.findViewById(R.id.reading);
        reading.setText(employeeData.sat_critical_reading_avg_score);

        TextView math = this.findViewById(R.id.mathscore);
        math.setText(employeeData.sat_math_avg_score);

        TextView writing = this.findViewById(R.id.writing);
        writing.setText(employeeData.sat_writing_avg_score);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        System.out.println("back button pressed");
        presenter.getModel().setEmployeeList();
        MainActivity mainActivity = (MainActivity)myApplication.getContext();
        mainActivity.loadListView2(presenter.getModel().getEmployeeList());
        super.onBackPressed();
    }
}
