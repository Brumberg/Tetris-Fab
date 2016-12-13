package com.example.crunky.testapp;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;


public class SecondActivity extends AppCompatActivity {

    static int color = 0;
    static int resource = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


    }
    public void goToFirstActivity(View view) { //is called by onClick function of Button in activity_second.xml

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void changeColor(View view) {

        ImageButton button = (ImageButton) findViewById(R.id.imageButton2);

        switch (color) {
            case 0:
                button.setColorFilter(Color.RED);
                color = 1;
                break;
            case 1:
                button.setColorFilter(Color.GREEN);
                color = 2;
                break;
            case 2:
                button.setColorFilter(Color.BLUE);
                color = 3;
                break;
            case 3:
                button.setColorFilter(Color.YELLOW);
                color = 4;
                break;
            default:
                button.setColorFilter(Color.BLACK);
                color = 0;
                break;
        }

    }

    public void changeObj(View view) {
        ImageButton button = (ImageButton) findViewById(R.id.imageButton2);

        switch (resource){
            case 0:
                button.setImageResource(R.drawable.vi_quadrat_small);
                resource = 1;
                break;

            case 1:
                button.setImageResource(R.drawable.vi_i);
                resource = 2;
                break;

            case 2:
                button.setImageResource(R.drawable.vi_t);
                resource = 3;
                break;

            case 3:
                button.setImageResource(R.drawable.vi_l);
                resource = 4;
                break;

            case 4:
                button.setImageResource(R.drawable.vi_z);
                resource = 5;
                break;

            default:
                button.setImageResource(R.drawable.vi_quadrat_large);
                resource = 0;
                break;

        }


    }
}
