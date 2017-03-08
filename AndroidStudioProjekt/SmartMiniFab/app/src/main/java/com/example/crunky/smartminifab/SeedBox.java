package com.example.crunky.smartminifab;

/**
 * SeedBox models the logic of block placement. It is indirectly connected
 * to the CBlockFactory via the Seedbox UI.
 *
 * A drag and drop operation works as follows:
 * 1) checking if there is a specific block component available
 * 2) if available - allocate it
 * 3) system calls onDrop (GUI) - check if placement is valid (isPlaceable):
 *      if so insert block into m_placedBlocks (to do: save position)
 *      if not call Release from dispatch interface to put it back on stock
 * 4) (to do) Serialize "m_PlacedBlocks" - return a string that describes the placement of the
 *      blocks. The string can directly be interpreted by the Arduino counterpart.
 *      (placing blocks means transmitting the string)
 *
 * Deleting a block from the Seedbox:
 *  1) call remove(Block block). All references in the seedbox to the block will be removed.
 *      Call release function from IDispatch
 *  2) call remove(int x, int y). Determine block id (search in seedbox). Remove every reference
 *      to block from seedbox. Return block reference. Call release function from IDsipatch to
 *      update stock.
 */

import android.content.*;
import android.graphics.*;

import java.io.*;
import java.net.*;
import java.util.*;

enum SeedBoxSize {THREEBYTHREE, FOURBYTHREE, FIVEBYFOUR};

public class SeedBox {
    private LinkedList<Block> m_placedBlocks;
    private Block[][] m_seedBox;
    private SeedBoxSize m_size;

    public SeedBox() {

        //m_storage = new HashMap<BlockType, Integer>();
        m_placedBlocks = new LinkedList<Block>();
        setSize(SeedBoxSize.THREEBYTHREE);
    }

    public Block[][] GetTestSeedBox() {
        //only for testing
        return m_seedBox;
    }

    public void setSize(SeedBoxSize size) {
        m_size = size;
        m_placedBlocks.clear();
        switch (size) {
            case THREEBYTHREE:
                m_seedBox = new Block[3][3];
                break;
            case FOURBYTHREE:
                m_seedBox = new Block[4][3];
                break;
            case FIVEBYFOUR:
                m_seedBox = new Block[5][4];
                break;
        }
    }

    public SeedBoxSize getSize() {
        return m_size;
    }


    public void reset() {
        //moved into SetSize
        //m_placedBlocks.clear();
        setSize(m_size);
    }

    private int countBlocks(BlockType blockType) {
        int count = 0;
        for (BlockType tmp : m_placedBlocks) {
            if (blockType.equals(tmp)) {
                count++;
            }
        }
        return count;
    }

    /**
     * insert block into the seedbox.
     * @param block
     * @return true block can potentially be placed otherwise false
     */
    public boolean isPlaceable(int x, int y, Block block) {
        boolean placeable = true;
        int width = block.GetWidth();
        int height = block.GetHeight();
        for (int i = 0; (placeable) && i < width; i++) {
            for (int j = 0; (placeable) && j < height; j++) {
                boolean alloc = block.GetBlockData(i,j);
                if (alloc) {
                    if (x + i < m_seedBox.length && y + j < m_seedBox[0].length) {
                        if (m_seedBox[x + i][y + j] != null) {
                            placeable = false;
                        }
                    } else {
                        placeable = false;
                    }
                }
            }
        }
        return placeable;
    }

    /**
     * insert block into the seedbox.
     * @param block
     * @return true block is placed otherwise false
     */
    public boolean place(int x, int y, Block block) {
        boolean placeable = isPlaceable(x,y,block);
        if (placeable) {
            int width = block.GetWidth();
            int height = block.GetHeight();
            for (int i = 0; (placeable) && i < width; i++) {
                for (int j = 0; (placeable) && j < height; j++) {
                    boolean alloc = block.GetBlockData(i, j);
                    if (alloc) {
                        if (x + i < m_seedBox.length && y + j < m_seedBox[0].length) {
                            if (m_seedBox[x + i][y + j] == null) {
                                m_seedBox[x + i][y + j] = block;
                            }
                        } else {
                            placeable = false;
                        }
                    }
                }
            }
            Point c = block.getCenterOfGravity();
            c.x += x;
            c.y += y;
            block.setPosition(c);
            m_placedBlocks.add(block);
        }
        return placeable;
    }

    /**
     * Returns the block at a specific position
     * @param x coords
     * @param y coords
     * @return color
     */
    public Block getBlock(int x, int y) {
        if (x>=0 && x<m_seedBox.length) {
            if (y>=0 && y<m_seedBox[0].length) {
                return m_seedBox[x][y];
            }
        }
        return null;
    }

    /**
     * Returns the block color at a specific position
     * @param x coords
     * @param y coords
     * @return color
     */
    public BlockColor getBlockColor(int x, int y) {
        BlockColor color = BlockColor.TRANSPARENT;
        if (x>=0 && x<m_seedBox.length) {
            if (y>=0 && y<m_seedBox[0].length) {
                if (m_seedBox[x][y]!=null)
                    return m_seedBox[x][y].getColor();
            }
        }
        return color;
    }

    /**
     * Function can be used to remove a block from the seedbox. Block specify the
     * coordinates of the block to be removed.
     * @param block
     */
    public void remove(Block block) {
        for (int i = 0; i < m_seedBox.length; i++) {
            for (int j = 0; j < m_seedBox[i].length; j++) {
                if (m_seedBox[i][j] == block) {
                    m_seedBox[i][j] = null;
                }
            }
        }
        m_placedBlocks.remove(block);
    }

    /**
     * Function can be used to remove a block from the seedbox. x and  y specify the
     * coordinates of the block to be removed.
     * @param x
     * @param y
     * @return removed block (call IDispatchBlock of the Factory to update storage)
     */
    public Block remove(int x, int y) {
        if (x<m_seedBox.length&&y<m_seedBox[x].length) {
            Block block = m_seedBox[x][y];
            if (block != null) {
                for (int i = 0; i < m_seedBox.length; i++) {
                    for (int j = 0; j < m_seedBox[i].length; j++) {
                        if (m_seedBox[i][j] == block) {
                            m_seedBox[i][j] = null;
                        }
                    }
                }
            }
            m_placedBlocks.remove(block);
            return block;
        } else {
            return null;
        }
    }

    /**
     * returs an array of placed blocks
     * @return
     */
    public Block[] GetPlacedBlocks() {
        Block[] blocklist = null;
        if (m_placedBlocks!=null) {
            blocklist = new Block[m_placedBlocks.size()];
            int i=0;
            for (Block block: m_placedBlocks) {
                blocklist[i++] = block;
            }
        }
        return blocklist;
    }

    public String toString() {
        String seedboxsize;
        switch (m_size) {
            case FIVEBYFOUR:
                seedboxsize = "1";
                break;
            case FOURBYTHREE:
                seedboxsize = "2";
                break;
            default:
                seedboxsize = "3";
                break;
        }
        String serialized = Integer.toString(m_placedBlocks.size()) + ";" + seedboxsize;
        for (Block b : m_placedBlocks) {
            serialized += ";" + b.toString();
        }
        return serialized;
    }
}