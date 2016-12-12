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

        Button button = (Button) findViewById(R.id.back_button); // find button by id

        button.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                goToFirstActivity();

            }

        });
    }
    private void goToFirstActivity() {

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

    }
}
