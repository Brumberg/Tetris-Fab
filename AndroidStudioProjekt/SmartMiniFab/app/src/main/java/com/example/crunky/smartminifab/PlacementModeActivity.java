package com.example.crunky.smartminifab;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import static com.example.crunky.smartminifab.R.id.ID_PlacementMode_BrickPreview_ImageView;


public class PlacementModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement_mode);

        // Allowing a view to be dragged
        findViewById(R.id.ID_PlacementMode_BrickPreview_ImageView).setOnTouchListener(new MyTouchListener());

        // Defining drop target
        findViewById(R.id.TetrisGrid).setOnDragListener(new MyDragListener());
    }

    public void goToHelpWindowActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, HelpWindowActivity.class);
        startActivity(intent);
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

    // defines touch listener
    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(final View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");

                final int w = (int) (view.getWidth() * view.getScaleX()); // get scaled width
                final int h = (int) (view.getHeight() * view.getScaleY()); // get scaled height
                double rotationRad = Math.toRadians(view.getRotation()); // get rotation in rad

                // get new scale factor for rotation
                double s = Math.abs(Math.sin(rotationRad));
                double c = Math.abs(Math.cos(rotationRad));

                // scale new width and height
                final int width = (int) (w * c + h * s);
                final int height = (int) (w * s + h * c);


                // define dragshadow builder and override 2 functions
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view) {

                    // Defines dimensions of the shadow image and the point within that shadow that
                    //should be centered under the touch location while dragging
                    @Override
                    public void onProvideShadowMetrics(android.graphics.Point shadowSize,
                                                       android.graphics.Point shadowTouchPoint) {

                        // set shadow size to new rotated size
                        shadowSize.set(width, height);

                        // scale touch point
                        shadowTouchPoint.set(shadowSize.x / 2, shadowSize.y / 2);
                    }

                    // Draws the shadow image. The system creates the Canvas object based on the
                    // dimensions it received from the onProvideShadowMetrics(Point, Point) callback
                    @Override
                    public void onDrawShadow(Canvas canvas) {

                        // scale the image relating to a pivot point (reference point)
                        canvas.scale(view.getScaleX(), view.getScaleY(), width / 2, height / 2);

                        // rotate the image relating to a pivot point (reference point)
                        canvas.rotate(view.getRotation(), width / 2, height / 2);

                        // Calculate the canvas matrix with the specified translation with given
                        // distances to x and y
                        canvas.translate((width - view.getWidth()) / 2,
                                (height - view.getHeight()) / 2);

                        // call parent onDrawShadow function
                        super.onDrawShadow(canvas);
                    }
                };


                view.startDrag(data, shadowBuilder, view, 0);

                view.setVisibility(View.INVISIBLE);

                return true;
            } else {
                return false;
            }
        }
    }

    public class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    //ViewGroup owner = (ViewGroup) view.getParent();
                    //owner.removeView(view);
                    //LinearLayout container = (LinearLayout) v;
                    //container.addView(view);
                    //int x_cord = (int) event.getX();
                    //int y_cord = (int) event.getY();

                    // CSeedBoxSurface surface = (CSeedBoxSurface) findViewById(R.id.TetrisGrid);

                    // do something
                    // surface.HandleDropOperation((int)event.getX(),(int)event.getY());
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    // do nothing
                    break;
                default:
                    break;
            }
            return true;
        }
    }

}
