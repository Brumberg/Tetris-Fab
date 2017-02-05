package com.example.crunky.smartminifab;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import static com.example.crunky.smartminifab.R.drawable.vi_3x3_seed_box;
import static com.example.crunky.smartminifab.SeedBoxSize.FIVEBYFOUR;
import static com.example.crunky.smartminifab.SeedBoxSize.FOURBYTHREE;


public class SeedBoxModeActivity extends AppCompatActivity {

    boolean seedboxsizechoosen;
    Button gotoPlacementMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed_box_mode);
        gotoPlacementMode = (Button)findViewById(R.id.ID_SeedBoxMode_Placement_Button);
        gotoPlacementMode.setEnabled(false);
    }

    public void goToDevelopmentModeActivity(View view) { //is called by onClick function of Button
        Intent intent = new Intent(this, DevelopmentModeActivity.class);
        startActivity(intent);
    }

    public void goToPlacementModeActivity(View view) { //is called by onClick function of Button

        Intent intent = new Intent(this, PlacementModeActivity.class);
        startActivity(intent);


    }

    public void goToHelpWindowActivity(View view) { //is called by onClick function of Button
        Intent intent = new Intent(this, HelpWindowActivity.class);
        startActivity(intent);

    }

    public void goToSeedBoxModeActivity(View view) { //is called by onClick function of Button
        Intent intent = new Intent(this, SeedBoxModeActivity.class);
        startActivity(intent);
    }

    public void goToWarehouseActivity(View view) { //is called by onClick function of Button
        Intent intent = new Intent(this, WarehouseActivity.class);
        startActivity(intent);
    }

    public void setSeedBoxSize(View view) { //is called by onClick function of Button

        SeedBoxSize size;

        ImageButton seedbox_3x3 = (ImageButton) findViewById(R.id.ID_SeedBoxMode_3x3_imageButton);
        ImageButton seedbox_4x3 = (ImageButton) findViewById(R.id.ID_SeedBoxMode_4x3_imageButton);
        ImageButton seedbox_5x4 = (ImageButton) findViewById(R.id.ID_SeedBoxMode_5x4_imageButton);

        seedbox_3x3.setImageResource(R.drawable.vi_3x3_seed_box);
        seedbox_4x3.setImageResource(R.drawable.vi_4x3_seed_box);
        seedbox_5x4.setImageResource(R.drawable.vi_5x4_seed_box);

        switch (view.getId()){
            case R.id.ID_SeedBoxMode_3x3_imageButton:
                size = SeedBoxSize.THREEBYTHREE;
                seedbox_3x3.setImageResource(R.drawable.vi_3x3_seed_box_marked);
                break;
            case R.id.ID_SeedBoxMode_4x3_imageButton:
                size = SeedBoxSize.FOURBYTHREE;
                seedbox_4x3.setImageResource(R.drawable.vi_4x3_seed_box_marked);
                break;
            case R.id.ID_SeedBoxMode_5x4_imageButton:
                size = SeedBoxSize.FIVEBYFOUR;
                seedbox_5x4.setImageResource(R.drawable.vi_5x4_seed_box_marked);
                break;
            default:
                size = SeedBoxSize.THREEBYTHREE;
        }

        ((SmartMiniFab) this.getApplication()).setSeedBoxSize(size);

        gotoPlacementMode.setEnabled(true);
    }
}
