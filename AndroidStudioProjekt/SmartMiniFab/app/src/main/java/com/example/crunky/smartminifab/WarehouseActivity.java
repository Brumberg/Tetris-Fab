package com.example.crunky.smartminifab;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;


/*
 * Warehouse activity for setting the warehousing stock
 */
public class WarehouseActivity extends AppCompatActivity {

    private int m_Color;            //background color of buttons
    private Button m_SelButton;     //active button

    private class BlockDescriptor { //descriptor to localize button
        public int m_ButtonId;
        public BlockShape m_Shape;
        public BlockColor m_Color;
        BlockDescriptor(int ButtonId, BlockShape shape, BlockColor color) {
            m_ButtonId = ButtonId;
            m_Shape = shape;
            m_Color = color;
        }
    }
    private BlockDescriptor BockDescription[][] = { //descriptor list
            {
                    new BlockDescriptor(R.id.ID_WarehouseMode_1x1_Black_Counter_Button,BlockShape.SIMPLE_SQUARE,BlockColor.BLACK),
                    new BlockDescriptor(R.id.ID_WarehouseMode_1x1_Red_Counter_Button,BlockShape.SIMPLE_SQUARE,BlockColor.RED),
                    new BlockDescriptor(R.id.ID_WarehouseMode_1x1_Green_Counter_Button,BlockShape.SIMPLE_SQUARE,BlockColor.GREEN),
                    new BlockDescriptor(R.id.ID_WarehouseMode_1x1_Blue_Counter_Button,BlockShape.SIMPLE_SQUARE,BlockColor.BLUE),
                    new BlockDescriptor(R.id.ID_WarehouseMode_1x1_Yellow_Counter_Button,BlockShape.SIMPLE_SQUARE,BlockColor.YELLOW)
            },
            {
                    new BlockDescriptor(R.id.ID_WarehouseMode_2x2_Black_Counter_Button,BlockShape.QUADRUPLE_SQUARE,BlockColor.BLACK),
                    new BlockDescriptor(R.id.ID_WarehouseMode_2x2_Red_Counter_Button,BlockShape.QUADRUPLE_SQUARE,BlockColor.RED),
                    new BlockDescriptor(R.id.ID_WarehouseMode_2x2_Green_Counter_Button,BlockShape.QUADRUPLE_SQUARE,BlockColor.GREEN),
                    new BlockDescriptor(R.id.ID_WarehouseMode_2x2_Blue_Counter_Button,BlockShape.QUADRUPLE_SQUARE,BlockColor.BLUE),
                    new BlockDescriptor(R.id.ID_WarehouseMode_2x2_Yellow_Counter_Button,BlockShape.QUADRUPLE_SQUARE,BlockColor.YELLOW)
            },
            {
                    new BlockDescriptor(R.id.ID_WarehouseMode_I_Black_Counter_Button,BlockShape.I_SHAPE,BlockColor.BLACK),
                    new BlockDescriptor(R.id.ID_WarehouseMode_I_Red_Counter_Button,BlockShape.I_SHAPE,BlockColor.RED),
                    new BlockDescriptor(R.id.ID_WarehouseMode_I_Green_Counter_Button,BlockShape.I_SHAPE,BlockColor.GREEN),
                    new BlockDescriptor(R.id.ID_WarehouseMode_I_Blue_Counter_Button,BlockShape.I_SHAPE,BlockColor.BLUE),
                    new BlockDescriptor(R.id.ID_WarehouseMode_I_Yellow_Counter_Button,BlockShape.I_SHAPE,BlockColor.YELLOW)
            },
            {
                    new BlockDescriptor(R.id.ID_WarehouseMode_J_Black_Counter_Button,BlockShape.MIRRORED_L_SHAPE,BlockColor.BLACK),
                    new BlockDescriptor(R.id.ID_WarehouseMode_J_Red_Counter_Button,BlockShape.MIRRORED_L_SHAPE,BlockColor.RED),
                    new BlockDescriptor(R.id.ID_WarehouseMode_J_Green_Counter_Button,BlockShape.MIRRORED_L_SHAPE,BlockColor.GREEN),
                    new BlockDescriptor(R.id.ID_WarehouseMode_J_Blue_Counter_Button,BlockShape.MIRRORED_L_SHAPE,BlockColor.BLUE),
                    new BlockDescriptor(R.id.ID_WarehouseMode_J_Yellow_Counter_Button,BlockShape.MIRRORED_L_SHAPE,BlockColor.YELLOW),
            },
            {       new BlockDescriptor(R.id.ID_WarehouseMode_S_Black_Counter_Button,BlockShape.L_SHAPE,BlockColor.BLACK),
                    new BlockDescriptor(R.id.ID_WarehouseMode_S_Red_Counter_Button,BlockShape.L_SHAPE,BlockColor.RED),
                    new BlockDescriptor(R.id.ID_WarehouseMode_S_Green_Counter_Button,BlockShape.L_SHAPE,BlockColor.GREEN),
                    new BlockDescriptor(R.id.ID_WarehouseMode_S_Blue_Counter_Button,BlockShape.L_SHAPE,BlockColor.BLUE),
                    new BlockDescriptor(R.id.ID_WarehouseMode_S_Yellow_Counter_Button,BlockShape.L_SHAPE,BlockColor.YELLOW),
            },
            {
                    new BlockDescriptor(R.id.ID_WarehouseMode_T_Black_Counter_Button,BlockShape.FOUR_SHAPE,BlockColor.BLACK),
                    new BlockDescriptor(R.id.ID_WarehouseMode_T_Red_Counter_Button,BlockShape.FOUR_SHAPE,BlockColor.RED),
                    new BlockDescriptor(R.id.ID_WarehouseMode_T_Green_Counter_Button,BlockShape.FOUR_SHAPE,BlockColor.GREEN),
                    new BlockDescriptor(R.id.ID_WarehouseMode_T_Blue_Counter_Button,BlockShape.FOUR_SHAPE,BlockColor.BLUE),
                    new BlockDescriptor(R.id.ID_WarehouseMode_T_Yellow_Counter_Button,BlockShape.FOUR_SHAPE,BlockColor.YELLOW),
            },
            {
                    new BlockDescriptor(R.id.ID_WarehouseMode_L_Black_Counter_Button,BlockShape.MIRRORED_T_SHAPE,BlockColor.BLACK),
                    new BlockDescriptor(R.id.ID_WarehouseMode_L_Red_Counter_Button,BlockShape.MIRRORED_T_SHAPE,BlockColor.RED),
                    new BlockDescriptor(R.id.ID_WarehouseMode_L_Green_Counter_Button,BlockShape.MIRRORED_T_SHAPE,BlockColor.GREEN),
                    new BlockDescriptor(R.id.ID_WarehouseMode_L_Blue_Counter_Button,BlockShape.MIRRORED_T_SHAPE,BlockColor.BLUE),
                    new BlockDescriptor(R.id.ID_WarehouseMode_L_Yellow_Counter_Button,BlockShape.MIRRORED_T_SHAPE,BlockColor.YELLOW),
            },
            {
                    new BlockDescriptor(R.id.ID_WarehouseMode_Z_Black_Counter_Button,BlockShape.MIRRORRED_FOUR_SHAPE,BlockColor.BLACK),
                    new BlockDescriptor(R.id.ID_WarehouseMode_Z_Red_Counter_Button,BlockShape.MIRRORRED_FOUR_SHAPE,BlockColor.RED),
                    new BlockDescriptor(R.id.ID_WarehouseMode_Z_Green_Counter_Button,BlockShape.MIRRORRED_FOUR_SHAPE,BlockColor.GREEN),
                    new BlockDescriptor(R.id.ID_WarehouseMode_Z_Blue_Counter_Button,BlockShape.MIRRORRED_FOUR_SHAPE,BlockColor.BLUE),
                    new BlockDescriptor(R.id.ID_WarehouseMode_Z_Yellow_Counter_Button,BlockShape.MIRRORRED_FOUR_SHAPE,BlockColor.YELLOW),
            }
    };

    /**
     * specify color, no button in active state, transparent background color of buttons
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);
        m_SelButton=null;
        m_Color = Color.TRANSPARENT;
        UpdateButtons();
    }

    /**
     * just updates content of button array
     */
    private void UpdateButtons() {
        int NoBlocks=0;
        CBlockFactory factory = CBlockFactory.getInstance();
        for (int shapeix=0; shapeix<BockDescription.length;++shapeix) {
            for (int colorix=0;colorix<BockDescription[shapeix].length;++colorix) {
                int blockcount = factory.GetNoBlocksAvailable(
                        BockDescription[shapeix][colorix].m_Shape,
                        BockDescription[shapeix][colorix].m_Color);
                        View b=findViewById(BockDescription[shapeix][colorix].m_ButtonId);
                ((Button)b).setBackgroundColor(m_Color);
                ((Button)b).setText(Integer.toString(blockcount));
                NoBlocks += blockcount;
            }
        }
        TextView SumTextView=(TextView)(findViewById(R.id.ID_WarehouseMode_Sum_TextView));
        String text = "Sum=" + Integer.toString(NoBlocks);
        SumTextView.setText(text);
    }

    /**
     * recolor buttons (additional content has not changed)
     */
    private void RecolorButtons() {
        int NoBlocks=0;
        CBlockFactory factory = CBlockFactory.getInstance();
        for (int shapeix=0; shapeix<BockDescription.length;++shapeix) {
            for (int colorix=0;colorix<BockDescription[shapeix].length;++colorix) {
                Button b=(Button)(findViewById(BockDescription[shapeix][colorix].m_ButtonId));
                b.setBackgroundColor(m_Color);
            }
        }
    }


    /**
    * Load predefined stock
    * @param v
    */
    public void LoadButton_onClick(View v) {
        CBlockFactory factory=CBlockFactory.getInstance();
        String filename = getString(R.string.warehouse_file_name);
        String filePath = getFilesDir().getPath().toString() + "/";
        filename = filePath+filename;

        CBlockFactory.getInstance().readObject(filename);
        if (CBlockFactory.getInstance().GetFactoryState()== CBlockFactory.FactoryState.UNINITIALIZED) {
            //error while loading
        }

        UpdateButtons();
        if (m_SelButton!=null) {
            HighlightButton(m_SelButton);
        }
    }

    /**
     * Save predefined stock
     * @param v
     */
    public void StoreButton_onClick(View v) {
        CBlockFactory factory = CBlockFactory.getInstance();
        String filename = getString(R.string.warehouse_file_name);
        String filePath = getFilesDir().getPath().toString() + "/";
        filename = filePath+filename;
        CBlockFactory.getInstance().writeObject(filename);
        if (CBlockFactory.getInstance().GetFactoryState()==
                CBlockFactory.FactoryState.SERIALIZATIONERROR) {
                //error occurred
        }

        if (m_SelButton!=null) {
            UpdateButtons();
            HighlightButton(m_SelButton);
        }
    }

    /**
     * Activate button to increase/decrease stock of a specific block
     * @param v
     */
    public void selectBlock_onClick(View v) {
        m_SelButton = (Button)v;
        RecolorButtons();
        HighlightButton((Button)v);
        v.invalidate();
    }

    /**
     * Highlight button in its natural color
     * @param v
     */
    private void HighlightButton(Button v) {
        BlockDescriptor des = FindBlockDescription((Button) v);
        if (des!=null) {
            switch(des.m_Color) {
                case BLACK:
                    v.setBackgroundColor(Color.LTGRAY);
                    break;
                case GREEN:
                    v.setBackgroundColor(Color.argb(0xFF,0x4C,0xAF,0x50));
                    break;
                case RED:
                    v.setBackgroundColor(Color.argb(0xFF,0xF4,0x43,0x36));
                    break;
                case BLUE:
                    v.setBackgroundColor(Color.argb(0xFF,0x21,0x96,0xF3));
                    break;
                case YELLOW:
                    v.setBackgroundColor(Color.argb(0xFF,0xFF,0xEB,0x3B));
                    break;
            }
        } else {
        }
    }

    /**
     * just figure out which block is active
     * @param v
     * @return
     */
    private BlockDescriptor FindBlockDescription(Button v) {
        for (int shapeix=0; shapeix<BockDescription.length;++shapeix) {
            for (int colorix=0;colorix<BockDescription[shapeix].length;++colorix) {
                if (v==findViewById(BockDescription[shapeix][colorix].m_ButtonId)) {
                    return BockDescription[shapeix][colorix];
                }
            }
        }
        return null;
    }

    /**
     * Decrease stock
     * @param v
     */
    public void DecreaseButton_onClick(View v) {
        if (m_SelButton!=null) {
            CBlockFactory factory=CBlockFactory.getInstance();
            BlockDescriptor block = FindBlockDescription(m_SelButton);
            if (block!=null) {
                boolean blockavailale = factory.IsBlocktypeAvailable(
                        block.m_Shape, block.m_Color
                );
                if (blockavailale) {
                    factory.DeleteBlock(block.m_Shape, block.m_Color);
                    UpdateButtons();
                    HighlightButton(m_SelButton);
                }
            }
        }
    }

    /**
     * Increase stock
     * @param v
     */
    public void IncreaseButton_onClick(View v) {
        if (m_SelButton!=null) {
            CBlockFactory factory = CBlockFactory.getInstance();
            BlockDescriptor block = FindBlockDescription(m_SelButton);
            if (block != null) {
                if (factory.GetNoBlocksAvailable(block.m_Shape, block.m_Color) <
                        factory.m_MaxNoOfEachBlockType) {
                    factory.AddBlock(block.m_Shape, block.m_Color);
                    UpdateButtons();
                    HighlightButton(m_SelButton);
                }
            }
        }
    }

    /**
     * Proceed to help_activity.xml
     * @param view
     */
    public void goToHelpWindowActivity(View view) {
        Intent intent = new Intent(this, HelpWindowActivity.class);
        startActivity(intent);

    }

    /**
     * Proceed to activity_main.xml
     * @param view
     */
    public void goToSeedBoxModeActivity(View view) {
        Intent intent = new Intent(this, SeedBoxModeActivity.class);
        startActivity(intent);
    }

    /**
     * Proceed to activity_main.xml
     * @param view
     */
    public void goToFactorySelectModeActivity(View view) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}
