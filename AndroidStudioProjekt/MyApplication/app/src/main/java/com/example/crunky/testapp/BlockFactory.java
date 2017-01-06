package com.example.crunky.testapp;

import android.content.*;
import android.graphics.*;

import java.io.*;
import java.net.*;
import java.util.*;

enum SeedBoxSize {THREEBYTHREE, FOURBYTHREE, FIVEBYFOUR};

public class BlockFactory extends ContextDependentObject {
    private WiFiConnection m_connection;
    private HashMap<BlockType, Integer> m_storage;
    private LinkedList<Block> m_placedBlocks;
    private Block[][] m_seedBox;
    private SeedBoxSize m_size;
    private boolean m_connected;
    private boolean m_loggedIn;

    public BlockFactory(Context context, InetAddress address, int port) {
        super(context);
        m_storage = new HashMap<BlockType, Integer>();
        m_placedBlocks = new LinkedList<Block>();
        setSize(SeedBoxSize.THREEBYTHREE);
        m_connection = new WiFiConnection(context, address, port);
        m_connected =
                m_loggedIn = false;
    }

    public void setSize(SeedBoxSize size) {
        m_size = size;
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

    public boolean connect() {
        try {
            m_connection.connect();
            m_connection.writeLine("HELLO");
            m_connected = m_connection.readLine().equals("ARDUINO");
        } catch (IOException e) {
            m_connected = false;
        }
        return m_connected;
    }

    public boolean disconnect() {
        if (m_connected) {
            try {
                m_connection.close();
                m_connection.writeLine("BYE");
            } catch (IOException e) {
            }
            m_connected =
                    m_loggedIn = false;
        }
        return !m_connected;
    }

    public boolean login(String password)
            throws IllegalArgumentException {
        if ((m_connected) && (!m_loggedIn)) {
            try {
                m_connection.writeLine("PASSWORD " + password);
                m_loggedIn = m_connection.readLine().equals("OK");
            } catch (IOException e) {
                disconnect();
            }
        }
        return m_loggedIn;
    }

    public boolean transferSeedBox() {
        boolean transferred = false;
        if ((m_connected) && (m_loggedIn)) {
            try {
                m_connection.writeLine("SEEDBOX " + toString());
                transferred = m_connection.readLine().equals("OK");
            } catch (IOException e) {
                disconnect();
            }
        }
        return transferred;
    }

    public void reset() {
        m_storage.clear();
        m_placedBlocks.clear();
        setSize(m_size);
    }

    public void setBlockStorage(BlockType blockType, int count) {
        m_storage.remove(blockType);
        m_storage.put(blockType, count);
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

    public boolean isPlaceable(Block block) {
        boolean placeable = m_storage.get((BlockType) (block)) > countBlocks((BlockType) (block));
        if (placeable) {
            boolean[][] shapeData = block.getShapeData();
            Point position = block.getPosition();
            Point centerOfGravity = block.getCenterOfGravity();
            placeable = (position.x - centerOfGravity.x >= 0) &&
                    (position.x - centerOfGravity.x + shapeData.length <= m_seedBox.length) &&
                    (position.y - centerOfGravity.y >= 0) &&
                    (position.y - centerOfGravity.y + shapeData.length <= m_seedBox.length);
            for (int i = 0; (placeable) && (i < shapeData.length); i++) {
                for (int j = 0; (placeable) && (j < shapeData[0].length); j++) {
                    placeable = (!shapeData[i][j]) || (m_seedBox[position.x - centerOfGravity.x + i][position.y - centerOfGravity.y + j] == null);
                }
            }
        }
        return placeable;
    }

    public boolean place(Block block) {
        boolean placeable = isPlaceable(block);
        if (placeable) {
            boolean[][] shapeData = block.getShapeData();
            Point position = block.getPosition();
            Point centerOfGravity = block.getCenterOfGravity();
            for (int i = 0; i < shapeData.length; i++) {
                for (int j = 0; j < shapeData[0].length; j++) {
                    if(shapeData[i][j]) {
                        m_seedBox[position.x - centerOfGravity.x + i][position.y - centerOfGravity.y + j] = block;
                    }
                }
            }
            m_placedBlocks.add(block);
        }
        return placeable;
    }

    public void remove(Block block) {
        for (int i = 0; i < m_seedBox.length; i++) {
            for (int j = 0; j < m_seedBox[0].length; j++) {
                if (m_seedBox[i][j] == block) {
                    m_seedBox[i][j] = null;
                }
            }
        }
        m_placedBlocks.remove(block);
    }

    public String toString() {
        String serialized = Integer.toString(m_placedBlocks.size()) + ";" + Integer.toString(m_size.ordinal());
        for (Block b : m_placedBlocks) {
            serialized += ";" + b.toString();
        }
        return serialized;
    }

    public InetAddress getHost() {
        return m_connection.getHost();
    }

    public Block[][] getSeedBox() {
        return m_seedBox;
    }
}