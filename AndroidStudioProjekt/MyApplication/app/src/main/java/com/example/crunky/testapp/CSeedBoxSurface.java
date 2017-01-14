package com.example.crunky.testapp;

import android.view.SurfaceView;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

public class CSeedBoxSurface extends SurfaceView {
    private SurfaceHolder holder;                               // for fancy additional stuff.
                                                                // Not directly required right now.
    private Paint m_BackgroundColor = new Paint(Color.GREEN);   // color. Used in onDraw function.
                                                                // at the moment used for all
                                                                // aspects of drawing. There should
                                                                // be one for each task.
    private BlockComponent.CGridCtrl m_Grid = new BlockComponent.CGridCtrl(BlockComponent.CBlockFactory.getInstance());
                                                                // model of the grid
    private int m_SurfViewWidth;                                // size of the view
    private int m_SurfViewHeight;                               // size of the view
    private Bitmap m_Background;                                // not used
    private List<BlockComponent.CBlock> m_UsedBlocks;           // list of placed objects

    public int GetWidth() {return m_SurfViewWidth;}             //physical size of the view. Required?
    public int GetHeight() {return m_SurfViewHeight;}           //physical size of the view. Required?

    /**
     * Plain constructor
     * @param context
     * @param attrs
     * setup CSeedboxSurface - initialize the object (see Android spec - surfaceview)
     */
    public CSeedBoxSurface(Context context) {
        super(context);
        holder = getHolder();
        m_UsedBlocks = new ArrayList<BlockComponent.CBlock>();
        m_UsedBlocks.clear();
        m_BackgroundColor.setColor(Color.GREEN);
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
    public CSeedBoxSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        m_UsedBlocks = new ArrayList<BlockComponent.CBlock>();
        m_UsedBlocks.clear();
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
        BlockComponent.CBlockFactory factory = BlockComponent.CBlockFactory.getInstance();
        factory.AddBlock(BlockComponent.CBlock.eBlockType.L_SHAPE, BlockComponent.CBlock.eBlockColor.BLACK);
    }
    public CSeedBoxSurface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public CSeedBoxSurface(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
    }

    /**
     * FindBlock
     * @param id
     * @return block reference
     * used to associate uiniqueblockid with block reference
     */
    private BlockComponent.CBlock FindBlock(int id) {
        BlockComponent.CBlock found=null;
        for (int i=0; i<m_UsedBlocks.size(); ++i) {
            BlockComponent.CBlock block = m_UsedBlocks.get(i);
            if (block.GetUniqueObjectId()==id) {
                found = m_UsedBlocks.get(i);
                break;
            }
        }
        return found;
    }

    /**
     * void HandleDropOperation(int x, int y)
     * @param x
     * @param y
     * Test function. Get object from component factory (if available) and try o place it
     */
    public void HandleDropOperation(int x, int y) {
        //determine placement of block in "logical" units
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
        }
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

        int width = m_Grid.GetWidth();
        int height = m_Grid.GetHeight();

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
        for (int i=0; i < height; ++i)
            for (int j=0; j < width; ++j) {
                int id = m_Grid.GetObjectId(j,height-i-1);
                if (id!=0) {
                    BlockComponent.CBlock block = FindBlock(id);

                    m_BackgroundColor.setStyle(Paint.Style.FILL);
                    //set brush color - specified in block - use green
                    m_BackgroundColor.setColor(Color.BLUE);
                    m_BackgroundColor.setStrokeWidth(10);
                    canvas.drawRect(j*xcord+1,i*ycord+1,
                            (j+1)*xcord-1,(i+1)*ycord-1,m_BackgroundColor);
                }
            }
        //pass control to parent
        super.onDraw(canvas);
    }
}
