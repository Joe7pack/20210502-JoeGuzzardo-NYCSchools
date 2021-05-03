package com.nycschools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository {
    private JSONObject employeeObject;
    private String firstName;
    private String lastName;
    private String phone;
    static private String url = "https://data.cityofnewyork.us/resource/f9bf-2cp4.json";
    static private List<School> employeeList;
    static private Map<String, School> employeeMap;
    static private Map<String, Drawable> employeeIconMap;
    static private Context mContext;
    static private boolean mDataLoaded = false;
    static Map<String, Drawable> employeeSmallIcons;

    public Repository(Context context) {
        mContext = context;
        loadJSONData();
    }

    private void loadJSONData() {
        try {
            new JsonTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "https://data.cityofnewyork.us/resource/s3k6-pzi2.json");
        } catch (Exception e) {
            System.out.println("Error creating JSON Employee list: " + e);
        }
    }

    public boolean getDataLoaded() {
        return mDataLoaded;
    }

    private static void setDataLoaded(boolean dataLoaded) {
        mDataLoaded = dataLoaded;
    }

    //@Override
    public void setContext(Context context) {
        mContext = context;
    }

    //@Override
    public List<School> getEmployeeList() {
        return employeeList;
    }

    //@Override
    public School getEmployee(String employeeId) {
        if (employeeMap.containsKey(employeeId)) {
            return employeeMap.get(employeeId);
        } else {
            return null;
        }
    }

    static private void setEmployeeList(String jsonData) {
        try {
            Type collectionType = new TypeToken<List<School>>() {
            }.getType();
            employeeList = new Gson().fromJson(jsonData, collectionType);
            Collections.sort(employeeList);
            setEmployeeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Override
    public void setEmployeeList() {
        Collections.sort(employeeList);
        setEmployeeMap();
    }

    static private void setEmployeeMap() {
        employeeMap = new HashMap<String, School>();
        for (School i : employeeList) employeeMap.put(i.getKey(), i);
        setDataLoaded(true);
    }

    //@Override
    public Map<String, School> getEmployeeMap() {
        return employeeMap;
    }

    //@Override
    public Map<String, Drawable> getEmployeeIconMap() {
        return employeeIconMap;
    }

    // load the Json list from the source URL
    static private class JsonTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                setEmployeeList(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("GetEmployeesFromURL", "That didn't work!");
                }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public class School implements Comparable<School> {
        String dbn;
        String school_name;
        String num_of_sat_test_takers;
        String sat_critical_reading_avg_score;
        String sat_math_avg_score;
        String sat_writing_avg_score;

        String getKey() {
            return school_name;
        }

        @Override
        public int compareTo(School otherSchool) {
            StringBuilder thisNamePrefix = new StringBuilder();
            StringBuilder otherNamePrefix = new StringBuilder();
            String valueHere = thisNamePrefix.toString() + this.school_name;
            String valueThere = otherNamePrefix.toString() + otherSchool.school_name;
            return valueHere.compareTo(valueThere);
        }
    }

}
