package com.example.crunky.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

    }
    public void goToFirstActivity(View view) { //is called by onClick function of Button in activity_second.xml

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

    }
}
