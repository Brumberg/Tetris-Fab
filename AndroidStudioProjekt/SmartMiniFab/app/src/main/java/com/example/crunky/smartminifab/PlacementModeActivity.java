package com.example.crunky.smartminifab;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import static com.example.crunky.smartminifab.R.id.ID_PlacementMode_BrickPreview_ImageView;


public class PlacementModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement_mode);
    }

    public void chooseBrick(View v) {
        ImageView img = (ImageView) findViewById(ID_PlacementMode_BrickPreview_ImageView);

        img.setRotation(0);
        img.setColorFilter(Color.BLACK);

        switch (v.getId()) {
            case R.id.ID_PlacementMode_1x1_Brick_ImageButton:
                img.setImageResource(R.drawable.vi_quadrat_1x1);
                break;

            case R.id.ID_PlacementMode_2x2_Brick_ImageButton:
                img.setImageResource(R.drawable.vi_quadrat_2x2);
                break;

            case R.id.ID_PlacementMode_I_Brick_ImageButton:
                img.setImageResource(R.drawable.vi_i);
                break;

            case R.id.ID_PlacementMode_J_Brick_ImageButton:
                img.setImageResource(R.drawable.vi_j);
                break;

            case R.id.ID_PlacementMode_L_Brick_ImageButton:
                img.setImageResource(R.drawable.vi_l);
                break;

            case R.id.ID_PlacementMode_S_Brick_ImageButton:
                img.setImageResource(R.drawable.vi_s);
                break;

            case R.id.ID_PlacementMode_T_Brick_ImageButton:
                img.setImageResource(R.drawable.vi_t);
                break;

            case R.id.ID_PlacementMode_Z_Brick_ImageButton:
                img.setImageResource(R.drawable.vi_z);
                break;

            default:
                img.setImageResource(R.drawable.vi_quadrat_2x2);
                //img.setImageResource(screen_background_light_transparent);
                break;
        }
    }

    public void changeColor(View v) {
        ImageView img = (ImageView) findViewById(ID_PlacementMode_BrickPreview_ImageView);

        switch (v.getId()) {
            case R.id.ID_PlacementMode_Black_Color_Button:
                img.setColorFilter(Color.BLACK);
                break;
            case R.id.ID_PlacementMode_Red_Color_Button:
                img.setColorFilter(Color.RED);
                break;
            case R.id.ID_PlacementMode_Green_Color_Button:
                img.setColorFilter(Color.GREEN);
                break;
            case R.id.ID_PlacementMode_Blue_Color_Button:
                img.setColorFilter(Color.BLUE);
                break;

            case R.id.ID_PlacementMode_Yellow_Color_Button:
                img.setColorFilter(Color.YELLOW);
                break;

            default:
                img.setColorFilter(Color.BLACK);
                break;
        }

    }

    public void rotateImageClockwise(View view) {

        View img = findViewById(ID_PlacementMode_BrickPreview_ImageView);

        img.setRotation(img.getRotation() + (float) 90.0);

    }

    public void rotateImageCounterClockwise(View view) {

        View img = findViewById(ID_PlacementMode_BrickPreview_ImageView);

        img.setRotation(img.getRotation() - (float) 90.0);

    }

}
