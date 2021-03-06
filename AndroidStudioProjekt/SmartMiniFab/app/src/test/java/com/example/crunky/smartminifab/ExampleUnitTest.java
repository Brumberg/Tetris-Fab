package com.example.crunky.smartminifab;

import org.junit.Test;
import com.example.crunky.smartminifab.Block;
import com.example.crunky.smartminifab.BlockType;
import com.example.crunky.smartminifab.SeedBox;
//import com.example.crunky.smartminifab

import static org.junit.Assert.*;
import com.example.crunky.smartminifab.CBlockFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import android.view.View;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        CBlockFactory dut = CBlockFactory.getInstance();
        dut.AddBlocks(5, BlockShape.FOUR_SHAPE, BlockColor.BLACK);
        dut.AddBlocks(2, BlockShape.L_SHAPE, BlockColor.GREEN);
        dut.AddBlocks(3, BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN);
        assert(dut.IsBlocktypeAvailable(BlockShape.FOUR_SHAPE, BlockColor.BLACK));
        assert(dut.IsBlocktypeAvailable(BlockShape.L_SHAPE, BlockColor.GREEN));
        assert(dut.IsBlocktypeAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN));
        assert(dut.IsBlocktypeAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.BLACK)==false);

        assert(dut.GetNoBlocks()==10);
        assert(dut.GetNoBlocksAvailable(BlockShape.FOUR_SHAPE, BlockColor.BLACK)==5);
        assert(dut.GetNoBlocksAvailable(BlockShape.L_SHAPE, BlockColor.GREEN)==2);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==3);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.RED)==0);

        Block dummy = dut.Allocate(BlockShape.MIRRORED_T_SHAPE, BlockColor.RED);
        assert(dummy==null);

        Block dummy1 = dut.Allocate(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN);
        assert(dummy1!=null);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==2);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==1);
        Block dummy2 = dut.Allocate(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN);
        assert(dummy2!=null);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==1);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==2);
        Block dummy3 = dut.Allocate(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN);
        assert(dummy3!=null);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==3);
        Block dummy4 = dut.Allocate(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN);
        assert(dummy4==null);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==3);

        //ignored by Bock factory - not a member
        Block external = new Block(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN,
                BlockRotation.DEGREES_0);
        dut.ReleaseBlock(external);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==0);

        dut.ReleaseBlock(dummy2);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==1);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==2);

        dut.ReleaseBlock(dummy3);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==2);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==1);

        dummy = dut.Allocate(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN);
        assert(dummy!=null);
        //remaining dummy - do seedbox checks
        SeedBox box = new SeedBox();
        assert(box.isPlaceable(0,0,dummy));
        assert(box.isPlaceable(0,1,dummy));
        assert(box.isPlaceable(1,0,dummy)==false);
        assert(box.isPlaceable(0,2,dummy)==false);
        assert(box.isPlaceable(3,0,dummy)==false);
        assert(box.isPlaceable(2,0,dummy)==false);
        assert(box.isPlaceable(2,1,dummy)==false);

        dummy.setRotation(BlockRotation.DEGREES_270);
        assert(box.isPlaceable(0,0,dummy));
        assert(box.isPlaceable(0,1,dummy)==false);
        assert(box.isPlaceable(1,0,dummy));
        assert(box.isPlaceable(0,2,dummy)==false);
        assert(box.isPlaceable(3,0,dummy)==false);
        assert(box.isPlaceable(2,0,dummy)==false);
        assert(box.isPlaceable(2,1,dummy)==false);

        box.place(0,0,dummy);
        Block[][] testblock = box.GetTestSeedBox();
        assert(testblock[0][0]==null);
        assert(testblock[1][0]!=null);
        assert(testblock[2][0]==null);
        assert(testblock[0][1]!=null);
        assert(testblock[1][1]!=null);
        assert(testblock[2][1]==null);
        assert(testblock[0][2]==null);
        assert(testblock[1][2]!=null);
        assert(testblock[2][2]==null);
        dut.ReleaseBlock(dummy);

        assert(dut.IsBlocktypeAvailable(BlockShape.FOUR_SHAPE, BlockColor.BLACK)==true);
        dummy = dut.Allocate(BlockShape.FOUR_SHAPE, BlockColor.BLACK);
        assert(dummy!=null);
        dummy.setRotation(BlockRotation.DEGREES_90);
        assert(box.place(0,0,dummy)==false);
        dummy2 = box.remove(0,0);
        assert(dummy2==null);
        dummy2 = box.remove(1,0);
        assert(dummy2!=null);
        dut.ReleaseBlock(dummy2);
        box.place(0,1,dummy);
        assert(testblock[0][0]==null);
        assert(testblock[1][0]==null);
        assert(testblock[2][0]==null);
        assert(testblock[0][1]!=null);
        assert(testblock[1][1]!=null);
        assert(testblock[2][1]==null);
        assert(testblock[0][2]==null);
        assert(testblock[1][2]!=null);
        assert(testblock[2][2]!=null);
        Point c = testblock[0][1].getPosition();
        assert(c.x==1);
        assert(c.y==2);
        dut.ResetFactory();

        assert(dut.IsBlocktypeAvailable(BlockShape.FOUR_SHAPE, BlockColor.BLACK)==false);
        assert(dut.IsBlocktypeAvailable(BlockShape.L_SHAPE, BlockColor.GREEN)==false);
        assert(dut.IsBlocktypeAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==false);
        assert(dut.IsBlocktypeAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.BLACK)==false);

        assert(dut.GetNoBlocks()==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.FOUR_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.L_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.RED)==0);

        assert(dut.IsBlocktypeAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.RED)==false);

        dut.AddBlocks(5, BlockShape.FOUR_SHAPE, BlockColor.BLACK);
        dut.AddBlocks(2, BlockShape.L_SHAPE, BlockColor.GREEN);
        dut.AddBlocks(3, BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN);
        //serialization test
        {
            //one by one copy of serialization code see WarehouseActivity
            //public void LoadButton_onClick(View v)
            CBlockFactory factory = CBlockFactory.getInstance();
            String filename = "warehouse.dat";

            factory.writeObject(filename);

            dut.DeleteBlock(BlockShape.L_SHAPE, BlockColor.GREEN);
            assert(dut.IsBlocktypeAvailable(BlockShape.FOUR_SHAPE, BlockColor.BLACK));
            assert(dut.IsBlocktypeAvailable(BlockShape.L_SHAPE, BlockColor.GREEN));
            assert(dut.IsBlocktypeAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN));
            assert(dut.IsBlocktypeAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.BLACK)==false);
            assert(dut.GetNoBlocksAvailable(BlockShape.L_SHAPE, BlockColor.GREEN)==1);

            factory.readObject(filename);
            assert(factory.GetFactoryState()== CBlockFactory.FactoryState.OK);

            dut = CBlockFactory.getInstance();
            assert(dut.GetNoBlocks()==10);
            assert(dut.GetNoBlocksAvailable(BlockShape.FOUR_SHAPE, BlockColor.BLACK)==5);
            assert(dut.GetNoBlocksAvailable(BlockShape.L_SHAPE, BlockColor.GREEN)==2);
            assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==3);
            assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.RED)==0);
        }

        //test of serialization
        SeedBox testbox = new SeedBox();
        testbox.setSize(SeedBoxSize.FIVEBYFOUR);
        dut.ResetFactory();
        dut.AddBlock(BlockShape.SIMPLE_SQUARE, BlockColor.YELLOW);
        dut.AddBlock(BlockShape.QUADRUPLE_SQUARE, BlockColor.GREEN);
        dut.AddBlock(BlockShape.MIRRORED_T_SHAPE, BlockColor.BLACK);
        dut.AddBlock(BlockShape.I_SHAPE, BlockColor.BLUE);
        dut.AddBlock(BlockShape.MIRRORED_L_SHAPE, BlockColor.YELLOW);
        dut.AddBlock(BlockShape.FOUR_SHAPE, BlockColor.RED);

        Block four = dut.Allocate(BlockShape.FOUR_SHAPE, BlockColor.RED);
        four.rotateClockwise();
        testbox.place(0,0,four);

        Block tshape = dut.Allocate(BlockShape.MIRRORED_T_SHAPE, BlockColor.BLACK);
        tshape.rotateClockwise();
        testbox.place(0,1,tshape);

        Block square = dut.Allocate(BlockShape.SIMPLE_SQUARE, BlockColor.YELLOW);
        testbox.place(1,3,square);

        Block quadsquare = dut.Allocate(BlockShape.QUADRUPLE_SQUARE, BlockColor.GREEN);
        testbox.place(2,2,quadsquare);

        Block I = dut.Allocate(BlockShape.I_SHAPE, BlockColor.BLUE);
        I.rotateClockwise();
        testbox.place(2,0,I);

        Block L = dut.Allocate(BlockShape.MIRRORED_L_SHAPE, BlockColor.YELLOW);
        testbox.place(3,1,L);

        Block[][] seedboxcontent = testbox.GetTestSeedBox();
        //checking for 4
        assert(seedboxcontent[0][0]==four);
        assert(seedboxcontent[1][0]==four);
        assert(seedboxcontent[1][1]==four);
        assert(seedboxcontent[2][1]==four);

        //checking for tshape
        assert(seedboxcontent[0][1]==tshape);
        assert(seedboxcontent[0][2]==tshape);
        assert(seedboxcontent[0][3]==tshape);
        assert(seedboxcontent[1][2]==tshape);

        //checking for square
        assert(seedboxcontent[1][3]==square);

        //checking for quadsquare
        assert(seedboxcontent[2][2]==quadsquare);
        assert(seedboxcontent[2][3]==quadsquare);
        assert(seedboxcontent[3][2]==quadsquare);
        assert(seedboxcontent[3][3]==quadsquare);

        //checking for L
        assert(seedboxcontent[3][1]==L);
        assert(seedboxcontent[4][1]==L);
        assert(seedboxcontent[4][2]==L);
        assert(seedboxcontent[4][3]==L);

        //checking for I
        assert(seedboxcontent[2][0]==I);
        assert(seedboxcontent[3][0]==I);
        assert(seedboxcontent[4][0]==I);

        String result = testbox.toString();
        assert(result.matches("6;1;51221;61130;00244;10342;21413;30534"));

        dut.ResetFactory();
        testbox.reset();
        dut.AddBlock(BlockShape.SIMPLE_SQUARE, BlockColor.RED);
        dut.AddBlock(BlockShape.I_SHAPE, BlockColor.GREEN);
        dut.AddBlock(BlockShape.L_SHAPE, BlockColor.BLACK);
        dut.AddBlock(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN);
        dut.AddBlock(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.RED);
        dut.AddBlock(BlockShape.MIRRORED_T_SHAPE, BlockColor.BLUE);

        Block rsqare = dut.Allocate(BlockShape.SIMPLE_SQUARE, BlockColor.RED);
        testbox.place(0,3,rsqare);

        Block gi = dut.Allocate(BlockShape.I_SHAPE, BlockColor.GREEN);
        testbox.place(0,0,gi);

        Block sl = dut.Allocate(BlockShape.L_SHAPE, BlockColor.BLACK);
        testbox.place(1,0,sl);

        Block gt = dut.Allocate(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN);
        gt.rotateClockwise();
        gt.rotateClockwise();
        testbox.place(1,2,gt);

        Block ri4 = dut.Allocate(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.RED);
        ri4.rotateClockwise();
        ri4.rotateClockwise();
        ri4.rotateClockwise();
        testbox.place(2,0,ri4);

        Block bt = dut.Allocate(BlockShape.MIRRORED_T_SHAPE, BlockColor.BLUE);
        bt.rotateCounterClockwise();
        testbox.place(3,1,bt);

        String res = testbox.toString();
        assert(res.matches("6;1;00141;20122;40220;62342;73421;63533"));

        dut.ResetFactory();
        testbox.reset();
        dut.AddBlock(BlockShape.L_SHAPE,BlockColor.BLUE);
        dut.AddBlock(BlockShape.L_SHAPE,BlockColor.YELLOW);
        dut.AddBlock(BlockShape.L_SHAPE,BlockColor.RED);
        dut.AddBlock(BlockShape.QUADRUPLE_SQUARE,BlockColor.BLACK);
        dut.AddBlock(BlockShape.MIRRORED_L_SHAPE,BlockColor.GREEN);
        Block bl = dut.Allocate(BlockShape.L_SHAPE,BlockColor.BLUE);
        testbox.place(0,0,bl);
        bl = dut.Allocate(BlockShape.L_SHAPE,BlockColor.YELLOW);
        bl.rotateClockwise();
        bl.rotateClockwise();
        testbox.place(0,1,bl);
        bl = dut.Allocate(BlockShape.L_SHAPE,BlockColor.RED);
        bl.rotateClockwise();
        bl.rotateClockwise();
        bl.rotateClockwise();
        testbox.place(2,0,bl);
        bl = dut.Allocate(BlockShape.QUADRUPLE_SQUARE,BlockColor.BLACK);
        testbox.place(2,1,bl);
        bl = dut.Allocate(BlockShape.MIRRORED_L_SHAPE,BlockColor.GREEN);
        bl.rotateClockwise();
        bl.rotateClockwise();
        bl.rotateClockwise();
        testbox.place(2,2,bl);

        res = testbox.toString();
        assert(res.matches("5;1;40123;42234;43411;10330;33442"));

        assert (dut.GetNoBlocks()==5);

        //available blocks
        assert(dut.GetNoBlocksAvailable(BlockShape.SIMPLE_SQUARE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.SIMPLE_SQUARE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.SIMPLE_SQUARE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.SIMPLE_SQUARE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.SIMPLE_SQUARE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksAvailable(BlockShape.QUADRUPLE_SQUARE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.QUADRUPLE_SQUARE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.QUADRUPLE_SQUARE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.QUADRUPLE_SQUARE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.QUADRUPLE_SQUARE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksAvailable(BlockShape.I_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.I_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.I_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.I_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.I_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_L_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_L_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_L_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_L_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_L_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksAvailable(BlockShape.FOUR_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.FOUR_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.FOUR_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.FOUR_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.FOUR_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksAvailable(BlockShape.L_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.L_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.L_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.L_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.L_SHAPE, BlockColor.RED)==0);

        //placed blocks
        assert(dut.GetNoBlocksDrawn(BlockShape.SIMPLE_SQUARE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.SIMPLE_SQUARE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.SIMPLE_SQUARE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.SIMPLE_SQUARE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.SIMPLE_SQUARE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksDrawn(BlockShape.QUADRUPLE_SQUARE, BlockColor.BLACK)==1);
        assert(dut.GetNoBlocksDrawn(BlockShape.QUADRUPLE_SQUARE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.QUADRUPLE_SQUARE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.QUADRUPLE_SQUARE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.QUADRUPLE_SQUARE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksDrawn(BlockShape.I_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.I_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.I_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.I_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.I_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_L_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_L_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_L_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_L_SHAPE, BlockColor.GREEN)==1);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_L_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_T_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_T_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_T_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_T_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksDrawn(BlockShape.FOUR_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.FOUR_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.FOUR_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.FOUR_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.FOUR_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksDrawn(BlockShape.L_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.L_SHAPE, BlockColor.BLUE)==1);
        assert(dut.GetNoBlocksDrawn(BlockShape.L_SHAPE, BlockColor.YELLOW)==1);
        assert(dut.GetNoBlocksDrawn(BlockShape.L_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.L_SHAPE, BlockColor.RED)==1);

        assert(dut.RestoreBlockFactory());
        assert (dut.GetNoBlocks()==5);

        //placed blocks
        assert(dut.GetNoBlocksDrawn(BlockShape.SIMPLE_SQUARE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.SIMPLE_SQUARE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.SIMPLE_SQUARE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.SIMPLE_SQUARE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.SIMPLE_SQUARE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksDrawn(BlockShape.QUADRUPLE_SQUARE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.QUADRUPLE_SQUARE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.QUADRUPLE_SQUARE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.QUADRUPLE_SQUARE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.QUADRUPLE_SQUARE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksDrawn(BlockShape.I_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.I_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.I_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.I_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.I_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_L_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_L_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_L_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_L_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_L_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_T_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_T_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_T_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORED_T_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksDrawn(BlockShape.FOUR_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.FOUR_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.FOUR_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.FOUR_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.FOUR_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksDrawn(BlockShape.L_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.L_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.L_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.L_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksDrawn(BlockShape.L_SHAPE, BlockColor.RED)==0);

        //available blocks
        assert(dut.GetNoBlocksAvailable(BlockShape.SIMPLE_SQUARE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.SIMPLE_SQUARE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.SIMPLE_SQUARE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.SIMPLE_SQUARE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.SIMPLE_SQUARE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksAvailable(BlockShape.QUADRUPLE_SQUARE, BlockColor.BLACK)==1);
        assert(dut.GetNoBlocksAvailable(BlockShape.QUADRUPLE_SQUARE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.QUADRUPLE_SQUARE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.QUADRUPLE_SQUARE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.QUADRUPLE_SQUARE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksAvailable(BlockShape.I_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.I_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.I_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.I_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.I_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_L_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_L_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_L_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_L_SHAPE, BlockColor.GREEN)==1);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_L_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORED_T_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksAvailable(BlockShape.FOUR_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.FOUR_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.FOUR_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.FOUR_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.FOUR_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.BLUE)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.YELLOW)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.MIRRORRED_FOUR_SHAPE, BlockColor.RED)==0);

        assert(dut.GetNoBlocksAvailable(BlockShape.L_SHAPE, BlockColor.BLACK)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.L_SHAPE, BlockColor.BLUE)==1);
        assert(dut.GetNoBlocksAvailable(BlockShape.L_SHAPE, BlockColor.YELLOW)==1);
        assert(dut.GetNoBlocksAvailable(BlockShape.L_SHAPE, BlockColor.GREEN)==0);
        assert(dut.GetNoBlocksAvailable(BlockShape.L_SHAPE, BlockColor.RED)==1);

    }
}
