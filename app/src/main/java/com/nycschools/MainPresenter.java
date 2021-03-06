package com.nycschools;

import android.content.Context;
import java.util.List;


public class MainPresenter implements Presenter<MvpView> {

    private Context context;
    private Repository model;
    private MvpView mvpView;
    private List<Repository.School> employeeList;

    public MainPresenter(Context context) {
        model = new Repository(context);
        setModel(model);
    }

    @Override
    public void attachView(MvpView view) {
        this.mvpView = view;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void detachView() {
        this.mvpView = null;
    }

    @Override
    public List<Repository.School> getEmployeeList() {
        employeeList = getModel().getEmployeeList();
        return employeeList;
    }

    @Override
    public void setEmployeeList() {
        getModel().setEmployeeList();
    }

    public boolean getDataLoaded() {
        return getModel().getDataLoaded();
    }

    @Override
    public void setModel(Repository model) {
        this.model = model;
    }

    @Override
    public Repository getModel() {
        return this.model;
    }

}
