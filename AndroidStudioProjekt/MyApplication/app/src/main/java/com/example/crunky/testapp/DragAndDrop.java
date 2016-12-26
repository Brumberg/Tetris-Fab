package com.example.crunky.testapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.content.ClipData;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.widget.ImageView;


public class DragAndDrop extends AppCompatActivity {

    String msg = "Android : ";
    static int color = Color.BLACK;
    static int resource = 0;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_and_drop);

        color=Color.BLACK; // default color

        Log.d(msg, "The onCreate() event");

        // Allowing a view to be dragged
        findViewById(R.id.tetris_baustein).setOnTouchListener(new MyTouchListener());

        // Defining drop target
        findViewById(R.id.topleft).setOnDragListener(new MyDragListener());
        findViewById(R.id.topmidle).setOnDragListener(new MyDragListener());
        findViewById(R.id.topright).setOnDragListener(new MyDragListener());
        findViewById(R.id.midleleft).setOnDragListener(new MyDragListener());
        findViewById(R.id.midlemidle).setOnDragListener(new MyDragListener());
        findViewById(R.id.midleright).setOnDragListener(new MyDragListener());
        findViewById(R.id.bottomleft).setOnDragListener(new MyDragListener());
        findViewById(R.id.bottommidle).setOnDragListener(new MyDragListener());
        findViewById(R.id.bottomright).setOnDragListener(new MyDragListener());

    }

    public void goBack(View view) { //is called by onClick function of Button in activity_main.xml

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

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
                    public void onProvideShadowMetrics(Point shadowSize,
                                                       Point shadowTouchPoint) {

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

                Log.d(msg, "On Touch");

                return true;
            } else {
                return false;
            }
        }
    }

    // drag and drop handling
    class MyDragListener implements View.OnDragListener {
        Drawable enterShape = getResources().getDrawable(
                R.drawable.shape_droptarget);
        Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {

            // actual shape where obj is dragged
            LinearLayout container = (LinearLayout) v;

            // list of all shapes
            int[] views = new int[9];

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundDrawable(enterShape);


                    if (container.getId() == R.id.midleright) {

                        views[0] = R.id.topright;
                        views[1] = R.id.midleright;
                        views[2] = R.id.bottomright;
                        views[3] = R.id.bottommidle;

                        for (int i = 0; i< views.length; i++) {
                            if (views[i] != 0) {
                                findViewById(views[i]).setBackgroundDrawable(enterShape);
                            }
                        }
                    }

                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    views[0] = R.id.topleft;
                    views[1] = R.id.topmidle;
                    views[2] = R.id.topright;
                    views[3] = R.id.midleleft;
                    views[4] = R.id.midlemidle;
                    views[5] = R.id.midleright;
                    views[6] = R.id.bottomleft;
                    views[7] = R.id.bottommidle;
                    views[8] = R.id.bottomright;

                    for (int i = 0; i < views.length; i++) {
                        findViewById(views[i]).setBackgroundDrawable(normalShape);
                    }

                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();

                    if (container.getId() == R.id.midleright) {

                        views[0] = R.id.topright;
                        views[1] = R.id.midleright;
                        views[2] = R.id.bottomright;
                        views[3] = R.id.bottommidle;


                        for (int i = 0; i< views.length; i++) {
                            if (views[i] != 0) {

                                findViewById(views[i]).setBackgroundColor(color);
                            }
                        }

                    } else {
                        ViewGroup owner = (ViewGroup) view.getParent();
                        owner.removeView(view);
                        container.addView(view);
                        view.setVisibility(View.VISIBLE);
                    }


                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }
    }

    public void changeColor(View view) {
        ImageView img = (ImageView) findViewById(R.id.tetris_baustein);

        switch (color) {
            case Color.BLACK:
                img.setColorFilter(Color.RED);
                color = Color.RED;
                break;
            case Color.RED:
                img.setColorFilter(Color.GREEN);
                color = Color.GREEN;
                break;
            case Color.GREEN:
                img.setColorFilter(Color.BLUE);
                color = Color.BLUE;
                break;
            case Color.BLUE:
                img.setColorFilter(Color.YELLOW);
                color = Color.YELLOW;
                break;
            default:
                img.setColorFilter(Color.BLACK);
                color = Color.BLACK;
                break;
        }

    }

    public void rotateImage(View view) {

        View img = findViewById(R.id.tetris_baustein);

        img.setRotation(img.getRotation() - (float) 90.0);

    }

    public void changeObj(View view) {
        ImageView img = (ImageView) findViewById(R.id.tetris_baustein);

        img.setRotation((float) 0.0);

        switch (resource) {
            case 0:
                img.setImageResource(R.drawable.vi_quadrat_small);
                resource = 1;
                break;

            case 1:
                img.setImageResource(R.drawable.vi_i);
                resource = 2;
                break;

            case 2:
                img.setImageResource(R.drawable.vi_t);
                resource = 3;
                break;

            case 3:
                img.setImageResource(R.drawable.vi_l);
                resource = 4;
                break;

            case 4:
                img.setImageResource(R.drawable.vi_j);
                resource = 5;
                break;

            case 5:
                img.setImageResource(R.drawable.vi_z);
                resource = 6;
                break;

            case 6:
                img.setImageResource(R.drawable.vi_s);
                resource = 7;
                break;

            default:
                img.setImageResource(R.drawable.vi_quadrat_large);
                resource = 0;
                break;

        }
    }

}
