package com.mirea.elizarovnm.employeedb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AppDataBase database = App.getInstance().getDatabase();
        final EmployeeDao employeeDao = database.employeeDao();

        employeeDao.insert(new Employee(1,"Илья", 2));
        employeeDao.insert(new Employee(2,"Ваня", 4));
        employeeDao.insert(new Employee(3,"Петя", 6));

    }
}