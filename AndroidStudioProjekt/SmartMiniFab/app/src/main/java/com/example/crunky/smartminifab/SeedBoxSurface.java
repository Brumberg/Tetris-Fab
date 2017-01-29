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

import static android.R.attr.width;
import static android.R.attr.x;
import static android.R.attr.y;
import static com.example.crunky.smartminifab.SeedBoxSize.FIVEBYFOUR;
import static com.example.crunky.smartminifab.SeedBoxSize.FOURBYTHREE;
import static com.example.crunky.smartminifab.SeedBoxSize.THREEBYTHREE;


public class SeedBoxSurface extends SurfaceView {
    private SurfaceHolder holder;
    Paint m_BackgroundColor = new Paint(Color.GREEN);
    private int m_SurfViewWidth;                                // size of the view
    private int m_SurfViewHeight;

    private SeedBox objSeedBox = new SeedBox();

    public SeedBoxSurface(Context context) {
        super(context);
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
    }

    /*private SurfaceHolder holder;                               // for fancy additional stuff.
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
    /*public CSeedBoxSurface(Context context) {
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
    /*public CSeedBoxSurface(Context context, AttributeSet attrs) {
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
    /*private BlockComponent.CBlock FindBlock(int id) {
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
    /*public void HandleDropOperation(int x, int y) {
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
    }*/


    public void handleDropOperation(int x, int y, Block block) {
        //determine placement of block in "logical" units

        // get size of seedbox
        SeedBoxSize seedBoxSize = objSeedBox.getSize();

        int norm_factor_x = 0;
        int norm_factor_y = 0;

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

        invalidate();


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

        objSeedBox.setSize(FIVEBYFOUR);
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

        // map between BlockColor and print color
        Map<BlockColor, Integer> map_blockcolor_to_int = new HashMap<>();

        map_blockcolor_to_int.put(BlockColor.BLACK, Color.BLACK);
        map_blockcolor_to_int.put(BlockColor.BLUE, Color.BLUE);
        map_blockcolor_to_int.put(BlockColor.GREEN, Color.GREEN);
        map_blockcolor_to_int.put(BlockColor.RED, Color.RED);
        map_blockcolor_to_int.put(BlockColor.YELLOW, Color.YELLOW);
        map_blockcolor_to_int.put(BlockColor.TRANSPARENT, Color.WHITE);

        Log.d("m_SurfViewWidth", Integer.toString(m_SurfViewWidth));
        Log.d("m_SurfViewHeight", Integer.toString(m_SurfViewHeight));

        //draw content of the seedbox
        for (int y=0; y < height; ++y) {
            for (int x=0; x < width; ++x) {
                BlockColor color = BlockColor.TRANSPARENT;

                if (objSeedBox.getBlock(x, y) != null)
                {
                    color = objSeedBox.getBlockColor(x, y);
                    Log.d("objSeedBox.getBlock()", "=True");
                }

                m_BackgroundColor.setStyle(Paint.Style.FILL);
                //set brush color - specified in block - use green
                m_BackgroundColor.setColor(map_blockcolor_to_int.get(color));
                m_BackgroundColor.setStrokeWidth(10);

                int y_scaled = -y + height - 1;

                canvas.drawRect(x * xcord + 5, y_scaled * ycord + 5, (x + 1) * xcord - 5,
                                (y_scaled + 1) * ycord - 5, m_BackgroundColor);

            }
        }



        /*CBlockFactory objBlockFactory = CBlockFactory.getInstance();

        for (int i=0; i < BlockShape.values().length; ++i){
            for (int j=0; j < BlockColor.values().length; ++j) {
                int num = objBlockFactory.GetNoBlocksDrawn(BlockShape.values()[i], BlockColor.values()[j]);

                for (int k = 0; k < num; k++) {
                    // TODO
                }

            }
        }*/

        /*
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
            }*/
        //pass control to parent
        super.onDraw(canvas);
    }
}



