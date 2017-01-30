package com.example.crunky.smartminifab;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.id;
import static android.R.drawable.screen_background_light_transparent;
import static com.example.crunky.smartminifab.R.id.ID_PlacementMode_BrickPreview_ImageView;


public class PlacementModeActivity extends AppCompatActivity {

    // get block handler from factory
    CBlockFactory objBlockFactory = CBlockFactory.getInstance();

    Block objBrickPreview = null;

    // map between shapes and Image Button id's
    Map<BlockShape, Integer> map_bricks_to_id = new HashMap<BlockShape, Integer>();
    Map<Integer, BlockShape> map_id_to_bricks = new HashMap<>();

    // map beteween colors and button id's
    Map<BlockColor, Integer> map_color_to_id = new HashMap<BlockColor, Integer>();
    Map<Integer, BlockColor> map_id_to_color = new HashMap<>();

    // map between BlockColor and print color
    Map<BlockColor,Integer> map_blockcolor_to_int = new HashMap<>();

    // remember chosen brick id
    int i_act_id = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement_mode);

        // Defining drop target
        findViewById(R.id.TetrisGrid).setOnDragListener(new MyDragListener());

        // map between shapes -> Image Button id's
        map_bricks_to_id.put(BlockShape.FOUR_SHAPE, R.id.ID_PlacementMode_S_Brick_ImageButton);
        map_bricks_to_id.put(BlockShape.I_SHAPE, R.id.ID_PlacementMode_I_Brick_ImageButton);
        map_bricks_to_id.put(BlockShape.L_SHAPE, R.id.ID_PlacementMode_L_Brick_ImageButton);
        map_bricks_to_id.put(BlockShape.MIRRORED_L_SHAPE, R.id.ID_PlacementMode_J_Brick_ImageButton);
        map_bricks_to_id.put(BlockShape.MIRRORED_T_SHAPE, R.id.ID_PlacementMode_T_Brick_ImageButton);
        map_bricks_to_id.put(BlockShape.MIRRORRED_FOUR_SHAPE, R.id.ID_PlacementMode_Z_Brick_ImageButton);
        map_bricks_to_id.put(BlockShape.QUADRUPLE_SQUARE, R.id.ID_PlacementMode_2x2_Brick_ImageButton);
        map_bricks_to_id.put(BlockShape.SIMPLE_SQUARE, R.id.ID_PlacementMode_1x1_Brick_ImageButton);

        // map between Image Button id's -> shapes
        for (BlockShape shape : map_bricks_to_id.keySet()) {
            map_id_to_bricks.put(map_bricks_to_id.get(shape), shape);
        }

        // map between colors -> button id's
        map_color_to_id.put(BlockColor.BLACK, R.id.ID_PlacementMode_Black_Color_Button);
        map_color_to_id.put(BlockColor.BLUE, R.id.ID_PlacementMode_Blue_Color_Button);
        map_color_to_id.put(BlockColor.GREEN, R.id.ID_PlacementMode_Green_Color_Button);
        map_color_to_id.put(BlockColor.RED, R.id.ID_PlacementMode_Red_Color_Button);
        map_color_to_id.put(BlockColor.YELLOW, R.id.ID_PlacementMode_Yellow_Color_Button);

        // map between button id's -> colors
        for (BlockColor color : map_color_to_id.keySet()) {
            map_id_to_color.put(map_color_to_id.get(color), color);
        }

        // map between Block colors and paint colors
        map_blockcolor_to_int.put(BlockColor.BLACK, Color.BLACK);
        map_blockcolor_to_int.put(BlockColor.BLUE, Color.BLUE);
        map_blockcolor_to_int.put(BlockColor.GREEN, Color.GREEN);
        map_blockcolor_to_int.put(BlockColor.RED, Color.RED);
        map_blockcolor_to_int.put(BlockColor.YELLOW, Color.YELLOW);


        // TODO: only for Test, needs to be deleted
        objBlockFactory.AddBlocks(1, BlockShape.I_SHAPE, BlockColor.RED);
        objBlockFactory.AddBlocks(1, BlockShape.I_SHAPE, BlockColor.YELLOW);
        objBlockFactory.AddBlocks(1, BlockShape.L_SHAPE, BlockColor.GREEN);
        objBlockFactory.AddBlocks(2, BlockShape.MIRRORED_L_SHAPE, BlockColor.BLUE);
        objBlockFactory.AddBlocks(3, BlockShape.SIMPLE_SQUARE, BlockColor.BLACK);

        updateView();

    }

    private void updateView(){

        // set all bricks to grey
        for (int id : map_bricks_to_id.values()) {
            ImageView img = (ImageView) findViewById(id);
            img.setColorFilter(Color.GRAY);
        }

        // set all colours to grey
        for (int id : map_color_to_id.values()) {
            Button button = (Button) findViewById(id);
            button.setBackgroundColor(Color.GRAY);
        }

        // set available block to black
        for (BlockShape shape : map_bricks_to_id.keySet()) {
            if (isBlockAvailable(shape)) {
                ImageView img = (ImageView) findViewById(map_bricks_to_id.get(shape));
                img.setColorFilter(Color.BLACK);
            }
        }

        // set colors if a brick is chosen
        if (i_act_id > 0) {
            for (BlockColor color : map_color_to_id.keySet()) {
                BlockShape shape = map_id_to_bricks.get(i_act_id);

                if (objBlockFactory.IsBlocktypeAvailable(shape, color)) {
                    Button button = (Button) findViewById(map_color_to_id.get(color));
                    button.setBackgroundColor(map_blockcolor_to_int.get(color));
                }
            }
        }
     }

    // check if blockshape is available (independent of color)
    private boolean isBlockAvailable(BlockShape shape) {
        boolean result = false;

        for (BlockColor color : map_color_to_id.keySet()) {
                if (objBlockFactory.IsBlocktypeAvailable(shape, color)){
                    result = true;
                    break;
                }
            }
        return result;
    }

    public void goToHelpWindowActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, HelpWindowActivity.class);
        startActivity(intent);
    }

    public void chooseBrick(View v) {

        // do nothing if block is not available
        if (isBlockAvailable(map_id_to_bricks.get(v.getId()))) {

            TextView obj_text_view = (TextView) findViewById(R.id.ID_PlacementMode_BrickPreview_TextView);
            obj_text_view.setVisibility(View.INVISIBLE);

            if (objBrickPreview != null) {
                objBlockFactory.ReleaseBlock(objBrickPreview);
                objBrickPreview = null;
            }

            ImageView img = (ImageView) findViewById(ID_PlacementMode_BrickPreview_ImageView);

            img.setRotation(0);
            img.setColorFilter(Color.GRAY);

            // disable drag and drop
            findViewById(ID_PlacementMode_BrickPreview_ImageView).setOnTouchListener(null);

            switch (v.getId()) {
                case R.id.ID_PlacementMode_1x1_Brick_ImageButton:
                    img.setImageResource(R.drawable.vi_quadrat_1x1);
                    i_act_id = R.id.ID_PlacementMode_1x1_Brick_ImageButton;
                    break;

                case R.id.ID_PlacementMode_2x2_Brick_ImageButton:
                    img.setImageResource(R.drawable.vi_quadrat_2x2);
                    i_act_id = R.id.ID_PlacementMode_2x2_Brick_ImageButton;
                    break;

                case R.id.ID_PlacementMode_I_Brick_ImageButton:
                    img.setImageResource(R.drawable.vi_i);
                    i_act_id = R.id.ID_PlacementMode_I_Brick_ImageButton;
                    break;

                case R.id.ID_PlacementMode_J_Brick_ImageButton:
                    img.setImageResource(R.drawable.vi_j);
                    i_act_id = R.id.ID_PlacementMode_J_Brick_ImageButton;
                    break;

                case R.id.ID_PlacementMode_L_Brick_ImageButton:
                    img.setImageResource(R.drawable.vi_l);
                    i_act_id = R.id.ID_PlacementMode_L_Brick_ImageButton;
                    break;

                case R.id.ID_PlacementMode_S_Brick_ImageButton:
                    img.setImageResource(R.drawable.vi_s);
                    i_act_id = R.id.ID_PlacementMode_S_Brick_ImageButton;
                    break;

                case R.id.ID_PlacementMode_T_Brick_ImageButton:
                    img.setImageResource(R.drawable.vi_t);
                    i_act_id = R.id.ID_PlacementMode_T_Brick_ImageButton;
                    break;

                case R.id.ID_PlacementMode_Z_Brick_ImageButton:
                    img.setImageResource(R.drawable.vi_z);
                    i_act_id = R.id.ID_PlacementMode_Z_Brick_ImageButton;
                    break;

                default:
                    break;
            }
            updateView();
        }

    }

    public void changeColor(View v) {

        if (i_act_id > 0) {
            ImageView img = (ImageView) findViewById(ID_PlacementMode_BrickPreview_ImageView);

            // release old brick
            if (objBrickPreview != null) {
                objBlockFactory.ReleaseBlock(objBrickPreview);
                objBrickPreview = null;
            }

            // get actual selected shape
            BlockShape block_shape = map_id_to_bricks.get(i_act_id);

            // get color from id
            BlockColor block_color = map_id_to_color.get(v.getId());

            // if block is available pick up
            if (objBlockFactory.IsBlocktypeAvailable(block_shape, block_color)) {
                img.setColorFilter(map_blockcolor_to_int.get(block_color));
                objBrickPreview = objBlockFactory.Allocate(block_shape, block_color);
                // Allowing a view to be dragged
                findViewById(ID_PlacementMode_BrickPreview_ImageView).setOnTouchListener(new MyTouchListener());
            }
        }
        updateView();

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
                    View view = (View) event.getLocalState();
                    SeedBoxSurface surface = (SeedBoxSurface) findViewById(R.id.TetrisGrid);
                    boolean successful = surface.handleDropOperation((int)event.getX(),
                                                                     (int)event.getY(),
                                                                     objBrickPreview);
                    if (successful) {
                        i_act_id = 0;
                        ImageView img = (ImageView) findViewById(ID_PlacementMode_BrickPreview_ImageView);
                        img.setImageResource(R.drawable.shape_droptarget);
                        img.setColorFilter(null);
                        TextView obj_text_view = (TextView) findViewById(R.id.ID_PlacementMode_BrickPreview_TextView);
                        obj_text_view.setVisibility(View.VISIBLE);
                        // disable drag and drop
                        findViewById(ID_PlacementMode_BrickPreview_ImageView).setOnTouchListener(null);
                    }

                    view.setVisibility(View.VISIBLE);

                    updateView();
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
