package com.example.crunky.smartminifab;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.*;

public class WarehouseActivity extends AppCompatActivity {

    private Button[][] CountButtons;
    private static final String[] BlockShapeIDParts=new String[] { "1x1", "2x2", "I", "J", "L", "S", "T", "Z" };
    private static final String[] BlockColorIDParts=new String[] { "Black", "Red", "Green", "Blue", "Yellow" };
    private Button m_currentButton;
    private TextView SumTextView;
    private int m_sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);
        m_sum=0;
        SumTextView=(TextView)(findViewById(R.id.ID_WarehouseMode_Sum_TextView));
        SumTextView.setText("Sum = 0");
        CountButtons=new Button[BlockShape.values().length][BlockColor.values().length];
        m_currentButton=null;
        for(int s=0; s<CountButtons.length; s++) {
            for(int c=0; c<CountButtons[s].length; c++) {
                try {
                    Field idField = R.id.class.getDeclaredField("ID_WarehouseMode_".concat(BlockShapeIDParts[s]).concat("_").concat(BlockColorIDParts[c]).concat("_Counter_Button"));
                    Button b=(Button)(findViewById(idField.getInt(idField)));
                    b.setTag(new BlockType(BlockShape.values()[s], BlockColor.values()[c]));
                    b.setText("0");
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button sender=(Button)(v);
                            if(m_currentButton==sender) {
                                m_currentButton=null;
                            } else {
                                m_currentButton=sender;
                            }
                        }
                    });
                    CountButtons[s][c]=b;
                } catch (Exception e) {
                    CountButtons[s][c]=null;
                }
            }
        }
    }

    public void LoadButton_onClick(View v) {
        CBlockFactory factory=CBlockFactory.getInstance();
        try {
            ObjectInputStream stream = new ObjectInputStream(openFileInput(getString(R.string.warehouse_file_name)));
            // factory.SetBlockNumbers((int[][])(stream.readObject()));
        }
        catch(FileNotFoundException e) {
        }
        catch(IOException e) {
        }
    }

    // TODO: Methods for getting and setting total number of blocks and for a specific blocktype in the storage must be implemented
    public void DecreaseButton_onClick(View v) {
        CBlockFactory factory=CBlockFactory.getInstance();
        BlockType blockType=(BlockType)(m_currentButton.getTag());
        // factory.Release(blockType);
        // m_currentButton.setText(Integer.toString(factory.GetNoBlocks(blockType.getShape(), blockType.getColor())));
        SumTextView.setText(Integer.toString(factory.GetNoBlocks()));
    }

    public void IncreaseButton_onClick(View v) {
        CBlockFactory factory=CBlockFactory.getInstance();
        BlockType blockType=(BlockType)(m_currentButton.getTag());
        // factory.Allocate(blockType);
        // m_currentButton.setText(Integer.toString(factory.GetNoBlocks(blockType.getShape(), blockType.getColor())));
        SumTextView.setText(Integer.toString(factory.GetNoBlocks()));
    }

    public void StoreButton_onClick(View v) {
        CBlockFactory factory=CBlockFactory.getInstance();
        try {
            ObjectOutputStream stream=new ObjectOutputStream(openFileOutput(getString(R.string.warehouse_file_name), Context.MODE_PRIVATE));
            // stream.writeObject(factory.GetBlockNumbers());
        }
        catch(IOException e) {
        }
    }

    public void goToHelpWindowActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, HelpWindowActivity.class);
        startActivity(intent);

    }

    public void goToSeedBoxModeActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, SeedBoxModeActivity.class);
        startActivity(intent);
    }
}
