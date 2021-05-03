package com.nycschools;

import android.content.Context;
import org.json.JSONException;

import java.util.List;

public interface Presenter<V> {
    void attachView(V view);
    void detachView();
    void setContext(Context context);
    Context getContext();
    List<Repository.School> getEmployeeList() throws JSONException;
    void setEmployeeList();
    void setModel(Repository model);
    Repository getModel();
}
