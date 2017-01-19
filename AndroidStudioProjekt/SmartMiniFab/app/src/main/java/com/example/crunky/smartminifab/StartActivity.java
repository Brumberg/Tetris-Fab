package com.example.crunky.smartminifab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    //TODO: Tempfunction to be deleted
    public void nextPage(View v){
        Intent intent = new Intent(this, PlacementModeActivity.class);

        startActivity(intent);
    }


}
