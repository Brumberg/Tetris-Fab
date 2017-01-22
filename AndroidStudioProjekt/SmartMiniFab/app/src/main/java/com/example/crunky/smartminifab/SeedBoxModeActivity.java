package com.example.crunky.smartminifab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SeedBoxModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed_box_mode);
    }

    public void goToDevelopmentModeActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, DevelopmentModeActivity.class);
        startActivity(intent);
    }

    public void goToPlacementModeActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, PlacementModeActivity.class);
        startActivity(intent);
    }

    public void goToHelpWindowActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, HelpWindowActivity.class);
        startActivity(intent);

    }

    public void goToSeedBoxModeActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, SeedBoxModeActivity.class);
        startActivity(intent);
    }

    public void goToWarehouseActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, WarehouseActivity.class);
        startActivity(intent);
    }
}
