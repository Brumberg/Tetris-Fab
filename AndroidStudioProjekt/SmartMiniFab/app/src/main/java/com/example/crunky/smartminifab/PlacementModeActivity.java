package com.example.crunky.smartminifab;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import static com.example.crunky.smartminifab.BlockShape.FOUR_SHAPE;
import static com.example.crunky.smartminifab.BlockShape.I_SHAPE;
import static com.example.crunky.smartminifab.BlockShape.L_SHAPE;
import static com.example.crunky.smartminifab.BlockShape.MIRRORED_L_SHAPE;
import static com.example.crunky.smartminifab.BlockShape.MIRRORED_T_SHAPE;
import static com.example.crunky.smartminifab.BlockShape.MIRRORRED_FOUR_SHAPE;
import static com.example.crunky.smartminifab.BlockShape.QUADRUPLE_SQUARE;
import static com.example.crunky.smartminifab.BlockShape.SIMPLE_SQUARE;
import static com.example.crunky.smartminifab.R.id.ID_PlacementMode_BrickPreview_ImageView;
import static com.example.crunky.smartminifab.SeedBoxSize.FIVEBYFOUR;




public class PlacementModeActivity extends AppCompatActivity {

    // get block handler from factory
    CBlockFactory objBlockFactory = CBlockFactory.getInstance();

    // create new seed box
    SeedBox objSeedBox = new SeedBox();

    Block objBrickPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement_mode);

        // Allowing a view to be dragged
        findViewById(R.id.ID_PlacementMode_BrickPreview_ImageView).setOnTouchListener(new MyTouchListener());

        // Defining drop target
        findViewById(R.id.TetrisGrid).setOnDragListener(new MyDragListener());

        // TODO: only for Test, needs to be deleted
        // add from each block type 1
        /*for (int i = 0; i < BlockShape.values().length; i++) {
                objBlockFactory.AddBlocks(1, BlockShape.values()[i], BlockColor.BLACK);
        }*/

        objBlockFactory.AddBlocks(1, BlockShape.I_SHAPE, BlockColor.BLACK);

        initializeView();

    }

    private void initializeView(){

        int[] lst_bricks = {R.id.ID_PlacementMode_1x1_Brick_ImageButton,
                            R.id.ID_PlacementMode_2x2_Brick_ImageButton,
                            R.id.ID_PlacementMode_I_Brick_ImageButton,
                            R.id.ID_PlacementMode_J_Brick_ImageButton,
                            R.id.ID_PlacementMode_L_Brick_ImageButton,
                            R.id.ID_PlacementMode_S_Brick_ImageButton,
                            R.id.ID_PlacementMode_T_Brick_ImageButton,
                            R.id.ID_PlacementMode_Z_Brick_ImageButton};

        for (int i = 0; i < lst_bricks.length; i++) {
            findViewById(i).setVisibility(View.INVISIBLE);
        }

        for (int i=0; i < BlockShape.values().length; ++i) {
            for (int j = 0; j < BlockColor.values().length; ++j) {
                int num = objBlockFactory.GetNoBlocksAvailable(BlockShape.values()[i], BlockColor.values()[j]);

                if (num > 0) {
                    switch (BlockShape.values()[i]){
                        case SIMPLE_SQUARE:
                            findViewById(R.id.ID_PlacementMode_1x1_Brick_ImageButton).setVisibility(View.VISIBLE);
                            break;
                        case QUADRUPLE_SQUARE:
                            findViewById(R.id.ID_PlacementMode_2x2_Brick_ImageButton).setVisibility(View.VISIBLE);
                            break;
                        case I_SHAPE:
                            findViewById(R.id.ID_PlacementMode_I_Brick_ImageButton).setVisibility(View.VISIBLE);
                            break;
                        case MIRRORED_L_SHAPE:
                            findViewById(R.id.ID_PlacementMode_J_Brick_ImageButton).setVisibility(View.VISIBLE);
                            break;
                        case L_SHAPE:
                            findViewById(R.id.ID_PlacementMode_L_Brick_ImageButton).setVisibility(View.VISIBLE);
                            break;
                        case FOUR_SHAPE:
                            findViewById(R.id.ID_PlacementMode_S_Brick_ImageButton).setVisibility(View.VISIBLE);
                            break;
                        case MIRRORED_T_SHAPE:
                            findViewById(R.id.ID_PlacementMode_T_Brick_ImageButton).setVisibility(View.VISIBLE);
                            break;
                        case MIRRORRED_FOUR_SHAPE:
                            findViewById(R.id.ID_PlacementMode_Z_Brick_ImageButton).setVisibility(View.VISIBLE);
                            break;
                    }
                }

            }
        }

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
                objBrickPreview = objBlockFactory.Allocate(BlockShape.SIMPLE_SQUARE, BlockColor.BLACK);
                break;

            case R.id.ID_PlacementMode_2x2_Brick_ImageButton:
                img.setImageResource(R.drawable.vi_quadrat_2x2);
                objBrickPreview = objBlockFactory.Allocate(BlockShape.QUADRUPLE_SQUARE, BlockColor.BLACK);
                break;

            case R.id.ID_PlacementMode_I_Brick_ImageButton:
                img.setImageResource(R.drawable.vi_i);
                objBrickPreview = objBlockFactory.Allocate(BlockShape.I_SHAPE, BlockColor.BLACK);
                break;

            case R.id.ID_PlacementMode_J_Brick_ImageButton:
                img.setImageResource(R.drawable.vi_j);
                objBrickPreview = objBlockFactory.Allocate(MIRRORED_L_SHAPE, BlockColor.BLACK);
                break;

            case R.id.ID_PlacementMode_L_Brick_ImageButton:
                img.setImageResource(R.drawable.vi_l);
                objBrickPreview = objBlockFactory.Allocate(L_SHAPE, BlockColor.BLACK);
                break;

            case R.id.ID_PlacementMode_S_Brick_ImageButton:
                img.setImageResource(R.drawable.vi_s);
                objBrickPreview = objBlockFactory.Allocate(FOUR_SHAPE, BlockColor.BLACK);
                break;

            case R.id.ID_PlacementMode_T_Brick_ImageButton:
                img.setImageResource(R.drawable.vi_t);
                objBrickPreview = objBlockFactory.Allocate(MIRRORED_T_SHAPE, BlockColor.BLACK);
                break;

            case R.id.ID_PlacementMode_Z_Brick_ImageButton:
                img.setImageResource(R.drawable.vi_z);
                objBrickPreview = objBlockFactory.Allocate(MIRRORRED_FOUR_SHAPE, BlockColor.BLACK);
                break;

            default:
                img.setImageResource(R.drawable.vi_quadrat_2x2);
                objBrickPreview = null;
                //img.setImageResource(screen_background_light_transparent);
                break;
        }
    }

    public void changeColor(View v) {
        ImageView img = (ImageView) findViewById(ID_PlacementMode_BrickPreview_ImageView);

        BlockShape tempShape = objBrickPreview.getShape();
        objBlockFactory.ReleaseBlock(objBrickPreview);

        switch (v.getId()) {
            case R.id.ID_PlacementMode_Black_Color_Button:
                img.setColorFilter(Color.BLACK);
                objBrickPreview = objBlockFactory.Allocate(tempShape, BlockColor.BLACK);
                break;
            case R.id.ID_PlacementMode_Red_Color_Button:
                img.setColorFilter(Color.RED);
                objBrickPreview = objBlockFactory.Allocate(tempShape, BlockColor.RED);
                break;
            case R.id.ID_PlacementMode_Green_Color_Button:
                img.setColorFilter(Color.GREEN);
                objBrickPreview = objBlockFactory.Allocate(tempShape, BlockColor.GREEN);
                break;
            case R.id.ID_PlacementMode_Blue_Color_Button:
                img.setColorFilter(Color.BLUE);
                objBrickPreview = objBlockFactory.Allocate(tempShape, BlockColor.BLUE);
                break;

            case R.id.ID_PlacementMode_Yellow_Color_Button:
                img.setColorFilter(Color.YELLOW);
                objBrickPreview = objBlockFactory.Allocate(tempShape, BlockColor.YELLOW);
                break;

            default:
                img.setColorFilter(Color.BLACK);
                objBrickPreview = objBlockFactory.Allocate(tempShape, BlockColor.BLACK);
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

                    handleDropOperation((int)event.getX(),(int)event.getY(), objBrickPreview);
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

    private void handleDropOperation(int x, int y, Block block) {
        //determine placement of block in "logical" units

        // get size of seedbox
        SeedBoxSize seedBoxSize = objSeedBox.getSize();

        int norm_factor_x = 0;
        int norm_factor_y = 0;

        objSeedBox.setSize(FIVEBYFOUR);

        switch (seedBoxSize){
            case THREEBYTHREE:
                norm_factor_x = 3;
                norm_factor_y = 3;
                break;
            case FOURBYTHREE:
                norm_factor_x = 4;
                norm_factor_y = 3;
                break;
            case FIVEBYFOUR:
                norm_factor_x = 5;
                norm_factor_y = 4;
                break;
        }

        View v = findViewById(R.id.TetrisGrid);

        int i_with_of_single_block = v.getWidth() / norm_factor_x;
        int i_height_of_single_block = v.getHeight() / norm_factor_y;

        // change point of origin from y to SeedBox convention (bottom = 0)
        y = -y + v.getHeight();

        int xcoord = x / i_with_of_single_block;
        int ycoord = y / i_height_of_single_block;

        boolean successful = objSeedBox.place(xcoord, ycoord, block);

        Log.d("place", Boolean.toString(successful));

        Log.d("x", Integer.toString(x));
        Log.d("y", Integer.toString(y));

        Log.d("getWith", Integer.toString(v.getWidth()));
        Log.d("getHeight", Integer.toString(v.getHeight()));

        Log.d("xcoord", Integer.toString(xcoord));
        Log.d("ycoord", Integer.toString(ycoord));


        /*
        int xcord = x*m_Grid.GetWidth()/m_SurfViewWidth;
        int ycord = y*m_Grid.GetHeight()/m_SurfViewHeight;



        //block within grid?
        if (xcord<m_Grid.GetWidth()&&ycord<m_Grid.GetHeight()) {
            //valid coord, so is there a block
            BlockComponent.CBlockFactory factory = BlockComponent.CBlockFactory.getInstance();
            BlockComponent.CBlock block = factory.Allocate(BlockComponent.CBlock.eBlockType.L_SHAPE,
                    BlockComponent.CBlock.eBlockColor.BLACK);
            if (block!=null) {
                //try to place block
                if (m_Grid.PlaceBlock(block,BlockComponent.CBlock.eRotation.DEGREES_0,
                        xcord,m_Grid.GetHeight()-ycord-1)) {
                    //invalidate dislay
                    m_UsedBlocks.add(block);
                    invalidate();
                } else {
                    //can not place block - release block
                    factory.ReleaseBlock(block);
                }
            }
        }*/
    }

}
