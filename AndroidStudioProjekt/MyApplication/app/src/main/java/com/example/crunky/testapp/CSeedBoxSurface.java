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
    private SurfaceHolder holder;
    private Paint m_BackgroundColor = new Paint(Color.GREEN);
    private BlockComponent.CGridCtrl m_Grid = new BlockComponent.CGridCtrl(BlockComponent.CBlockFactory.getInstance());
    private int m_SurfViewWidth;
    private int m_SurfViewHeight;
    private Bitmap m_Background;
    private List<BlockComponent.CBlock> m_UsedBlocks;

    public int GetWidth() {return m_SurfViewWidth;}
    public int GetHeight() {return m_SurfViewHeight;}

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

    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        m_SurfViewHeight = h;
        m_SurfViewWidth = w;
    }

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

    public void HandleDropOperation(int x, int y) {
        //determine placement of block
        int xcord = x*m_Grid.GetWidth()/m_SurfViewWidth;
        int ycord = y*m_Grid.GetHeight()/m_SurfViewHeight;
        if (xcord<m_Grid.GetWidth()&&ycord<m_Grid.GetHeight()) {
            //valid coord
            BlockComponent.CBlockFactory factory = BlockComponent.CBlockFactory.getInstance();
            BlockComponent.CBlock block = factory.Allocate(BlockComponent.CBlock.eBlockType.L_SHAPE,
                    BlockComponent.CBlock.eBlockColor.BLACK);
            if (block!=null) {
                if (m_Grid.PlaceBlock(block,BlockComponent.CBlock.eRotation.DEGREES_0,
                        xcord,m_Grid.GetHeight()-ycord-1)) {
                    m_UsedBlocks.add(block);
                    invalidate();
                } else {
                    factory.ReleaseBlock(block);
                }
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        //canvas.drawBitmap(m_Background, 0, 0, null);
        canvas.drawColor(Color.GRAY);
        m_BackgroundColor.setStyle(Paint.Style.FILL);
        m_BackgroundColor.setColor(Color.WHITE);
        canvas.drawRect(0,0,m_SurfViewWidth-1,m_SurfViewHeight-1,m_BackgroundColor);
        m_BackgroundColor.setStyle(Paint.Style.STROKE);
        m_BackgroundColor.setColor(Color.GREEN);
        m_BackgroundColor.setStrokeWidth(10);
        canvas.drawRect(0,0,m_SurfViewWidth-1,m_SurfViewHeight-1,m_BackgroundColor);

        int width = m_Grid.GetWidth();
        int height = m_Grid.GetHeight();

        int xcord = m_SurfViewWidth/width;
        int ycord = m_SurfViewHeight/height;

        for (int i=1;i<width;++i) {
            canvas.drawLine(i*xcord, 0, i*xcord, m_SurfViewHeight, m_BackgroundColor);
        }

        for (int i=1;i<height;++i) {
            canvas.drawLine(0, i*ycord, m_SurfViewWidth-1, i*ycord, m_BackgroundColor);
        }

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
        super.onDraw(canvas);
    }
}
