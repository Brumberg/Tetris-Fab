package com.example.crunky.smartminifab;

/**
 * Created by Crunky on 18.01.2017.
 */

import android.util.Log;
import android.view.SurfaceView;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.translateX;
import static android.R.attr.width;
import static android.R.attr.x;
import static android.R.attr.y;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.crunky.smartminifab.SeedBoxSize.FIVEBYFOUR;
import static com.example.crunky.smartminifab.SeedBoxSize.FOURBYTHREE;
import static com.example.crunky.smartminifab.SeedBoxSize.THREEBYTHREE;


public class SeedBoxSurface extends SurfaceView {
    private SurfaceHolder holder;
    Paint m_BackgroundColor = new Paint(Color.GREEN);
    private int m_SurfViewWidth;                                // size of the view
    private int m_SurfViewHeight;
    private Block obj_marked_block = null;

    private SeedBox objSeedBox = new SeedBox();

    // map between BlockColor and print color
    Map<BlockColor, Integer> map_blockcolor_to_int = new HashMap<>();


    public SeedBoxSurface(Context context) {
        super(context);
        map_blockcolor_to_int.put(BlockColor.BLACK, Color.BLACK);
        map_blockcolor_to_int.put(BlockColor.BLUE, Color.BLUE);
        map_blockcolor_to_int.put(BlockColor.GREEN, Color.GREEN);
        map_blockcolor_to_int.put(BlockColor.RED, Color.RED);
        map_blockcolor_to_int.put(BlockColor.YELLOW, Color.YELLOW);
        map_blockcolor_to_int.put(BlockColor.TRANSPARENT, Color.WHITE);

        holder = getHolder();
        /*m_UsedBlocks = new ArrayList<BlockComponent.CBlock>();
        m_UsedBlocks.clear();
        m_BackgroundColor.setColor(Color.GREEN);*/
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas c = holder.lockCanvas(null);
                onDraw(c);
                holder.unlockCanvasAndPost(c);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });


    }

    /**
     * Extended constructor (with attributes)
     * @param context
     * @param attrs
     * setup CSeedboxSurface - initialize the object (see Android spec - surfaceview)
     */
    public SeedBoxSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        map_blockcolor_to_int.put(BlockColor.BLACK, Color.BLACK);
        map_blockcolor_to_int.put(BlockColor.BLUE, Color.BLUE);
        map_blockcolor_to_int.put(BlockColor.GREEN, Color.GREEN);
        map_blockcolor_to_int.put(BlockColor.RED, Color.RED);
        map_blockcolor_to_int.put(BlockColor.YELLOW, Color.YELLOW);
        map_blockcolor_to_int.put(BlockColor.TRANSPARENT, Color.WHITE);

        holder = getHolder();
        /*m_UsedBlocks = new ArrayList<BlockComponent.CBlock>();
        m_UsedBlocks.clear();*/
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas c = holder.lockCanvas(null);
                onDraw(c);
                holder.unlockCanvasAndPost(c);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
        /*BlockComponent.CBlockFactory factory = BlockComponent.CBlockFactory.getInstance();
        factory.AddBlock(BlockComponent.CBlock.eBlockType.L_SHAPE, BlockComponent.CBlock.eBlockColor.BLACK);*/
    }
    public SeedBoxSurface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        map_blockcolor_to_int.put(BlockColor.BLACK, Color.BLACK);
        map_blockcolor_to_int.put(BlockColor.BLUE, Color.BLUE);
        map_blockcolor_to_int.put(BlockColor.GREEN, Color.GREEN);
        map_blockcolor_to_int.put(BlockColor.RED, Color.RED);
        map_blockcolor_to_int.put(BlockColor.YELLOW, Color.YELLOW);
        map_blockcolor_to_int.put(BlockColor.TRANSPARENT, Color.WHITE);
    }


    /**
     * onSizeChanged
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     * needed to get the size of the view. In the constructor it is too early to determine it.
     */
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        m_SurfViewHeight = h;
        m_SurfViewWidth = w;
        setWillNotDraw(false);
    }

    public void setSeedBoxSize (SeedBoxSize size) {
        objSeedBox.setSize(size);
    }

    public Block handleTouchOperation(int x, int y) {

        obj_marked_block = objSeedBox.getBlock(TranslateXcoord(x), TranslateYcoord(y));

        // force surface to update view
        invalidate();

        return obj_marked_block;
    }

    public void deleteBrick(Block block) {
        objSeedBox.remove(block);
        obj_marked_block = null;

        // force surface to update view
        invalidate();

    }

    public Boolean handleDropOperation(int x, int y, Block block) {
        boolean successful = objSeedBox.place(TranslateXcoord(x), TranslateYcoord(y), block);

        // force surface to update view
        invalidate();

        return successful;

    }

    /**
     * Translate coordinates from android standard to Seedbox standard
      */

    private int TranslateXcoord (int x) {
        // get size of seedbox
        SeedBoxSize seedBoxSize = objSeedBox.getSize();

        int norm_factor_x = 0;

        switch (seedBoxSize){
            case THREEBYTHREE:
                norm_factor_x = 3;
                break;
            case FOURBYTHREE:
                norm_factor_x = 4;
                break;
            case FIVEBYFOUR:
                norm_factor_x = 5;
                break;
        }

        View v = findViewById(R.id.TetrisGrid);

        int i_with_of_single_block = v.getWidth() / norm_factor_x;

        return x / i_with_of_single_block;

    }

    /**
     * Translate coordinates from android standard to Seedbox standard
     */
    private int TranslateYcoord (int y) {
        // get size of seedbox
        SeedBoxSize seedBoxSize = objSeedBox.getSize();

        int norm_factor_y = 0;

        switch (seedBoxSize){
            case THREEBYTHREE:
                norm_factor_y = 3;
                break;
            case FOURBYTHREE:
                norm_factor_y = 3;
                break;
            case FIVEBYFOUR:
                norm_factor_y = 4;
                break;
        }

        View v = findViewById(R.id.TetrisGrid);

        int i_height_of_single_block = v.getHeight() / norm_factor_y;

        // change point of origin from y to SeedBox convention (bottom = 0)
        y = -y + v.getHeight();

        return y / i_height_of_single_block;

    }



    /**
     * onDraw
     * @param canvas
     * function is called when display needs to be refreshed. Function draws the grid
     * and its background. It accesses the seedbox (model) to draw the blocks that are
     * already placed. The content of the seedbox corresponds with the content of the view.
     */
    protected void onDraw(Canvas canvas) {
        //draw background of the seedbox

        canvas.drawColor(Color.GRAY);
        m_BackgroundColor.setStyle(Paint.Style.FILL);
        m_BackgroundColor.setColor(Color.WHITE);
        canvas.drawRect(0,0,m_SurfViewWidth-1,m_SurfViewHeight-1,m_BackgroundColor);
        //draw borders of the seedbox
        m_BackgroundColor.setStyle(Paint.Style.STROKE);
        m_BackgroundColor.setColor(Color.GREEN);
        m_BackgroundColor.setStrokeWidth(10);
        canvas.drawRect(0,0,m_SurfViewWidth-1,m_SurfViewHeight-1,m_BackgroundColor);

       SeedBoxSize size = objSeedBox.getSize();

        int width = 0; //int width = m_Grid.GetWidth();
        int height = 0; //m_Grid.GetHeight();

        switch (size){
            case THREEBYTHREE:
                width = 3;
                height = 3;
                break;
            case FOURBYTHREE:
                width = 4;
                height = 3;
                break;
            case FIVEBYFOUR:
                width = 5;
                height = 4;
                break;
        }


        int xcord = m_SurfViewWidth/width;
        int ycord = m_SurfViewHeight/height;

        //draw grid inside the seedbox
        for (int i=1;i<width;++i) {
            canvas.drawLine(i*xcord, 0, i*xcord, m_SurfViewHeight, m_BackgroundColor);
        }

        for (int i=1;i<height;++i) {
            canvas.drawLine(0, i*ycord, m_SurfViewWidth-1, i*ycord, m_BackgroundColor);
        }

        //draw content of the seedbox
        for (int y=0; y < height; ++y) {
            for (int x=0; x < width; ++x) {
                BlockColor block_color = BlockColor.TRANSPARENT;

                if (objSeedBox.getBlock(x, y) != null)
                {
                    block_color = objSeedBox.getBlockColor(x, y);
                }

                m_BackgroundColor.setStyle(Paint.Style.FILL);

                int colorval = 0;
                if (objSeedBox.getBlock(x, y) == obj_marked_block) {
                     colorval = adjustAlpha(map_blockcolor_to_int.get(block_color),0.5f);
                } else {
                     colorval = adjustAlpha(map_blockcolor_to_int.get(block_color),1.0f);
                }

                m_BackgroundColor.setColor(colorval);
                m_BackgroundColor.setStrokeWidth(10);

                int y_scaled = -y + height - 1;

                canvas.drawRect(x * xcord + 5, y_scaled * ycord + 5, (x + 1) * xcord - 5,
                                (y_scaled + 1) * ycord - 5, m_BackgroundColor);

            }
        }

        //pass control to parent
        super.onDraw(canvas);
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}



