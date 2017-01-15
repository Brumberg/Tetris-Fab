package com.example.crunky.smartminifab;

import android.graphics.*;

/**
 * Created by Daniel on 31.12.2016.
 * Point sucks. I got an uninitialized array. Figuring out what happend took me a while
 *
 */
class Point {
    Point () {
        x = 0;
        y = 0;
    }
    Point(int tx, int ty) {
        x = tx;
        y = ty;
    }
    public int x;
    public int y;
}
enum BlockRotation {DEGREES_0, DEGREES_90, DEGREES_180, DEGREES_270}

public class Block extends BlockType {
    private static BlockRotation[] blockRotationValues = BlockRotation.values();
    private BlockRotation m_rotation;
    private static boolean[][][] shapeDataTemplates = {
            {{true}}, // SIMPLE_SQUARE
            {{true, true}, {true, true}}, // QUADRUPLE_SQUARE
            {{true, true, true}}, // I_SHAPE
            {{true, false, false}, {true, true, true}}, // MIRRORED_L_SHAPE
            {{true, true, true}, {true, false, false}}, // L_SHAPE
            {{false, true, true}, {true, true, false}}, // FOUR_SHAPE
            {{true, false}, {true, true}, {true, false}}, // MIRRORED_T_SHAPE
            {{true, true, false}, {false, true, true}}, // MIRRORRED_FOUR_SHAPE
    };
    final static Point[] centerOfGravity = {
            new Point(0, 0), // SIMPLE_SQUARE
            new Point(0, 1), // QUADRUPLE_SQUARE
            new Point(0, 1), // I_SHAPE
            new Point(1, 1), // MIRRORED_L_SHAPE
            new Point(0, 1), // L_SHAPE
            new Point(0, 1), // FOUR_SHAPE
            new Point(1, 0), // MIRRORED_T_SHAPE
            new Point(1, 1), // MIRRORRED_FOUR_SHAPE
    };

    private Point m_position;

    public Block(BlockShape shape, BlockColor color, BlockRotation rotation) {
        super(shape, color);
        setRotation(rotation);
        m_position = null;
    }

    public int GetWidth() {
        int width;
        switch (m_rotation) {
            case DEGREES_0:
                width = shapeDataTemplates[getShape().ordinal()].length;
                break;
            case DEGREES_90:
                width = shapeDataTemplates[getShape().ordinal()][0].length;
                break;
            case DEGREES_180:
                width = shapeDataTemplates[getShape().ordinal()].length;
                break;
            case DEGREES_270:
                width = shapeDataTemplates[getShape().ordinal()][0].length;
                break;
            default:
                width = shapeDataTemplates[getShape().ordinal()][0].length;
                break;
        }
        return width;
    }

    public int GetHeight() {
        int height;
        switch (m_rotation) {
            case DEGREES_0:
                height = shapeDataTemplates[getShape().ordinal()][0].length;
                break;
            case DEGREES_90:
                height = shapeDataTemplates[getShape().ordinal()].length;
                break;
            case DEGREES_180:
                height = shapeDataTemplates[getShape().ordinal()][0].length;
                break;
            case DEGREES_270:
                height = shapeDataTemplates[getShape().ordinal()].length;
                break;
            default:
                height = shapeDataTemplates[getShape().ordinal()].length;
                break;
        }
        return height;
    }

    public Point getCenterOfGravity() {
        Point center = new Point();
        int lengthx = shapeDataTemplates[getShape().ordinal()].length;
        int lengthy = shapeDataTemplates[getShape().ordinal()][0].length;
        int x = centerOfGravity[getShape().ordinal()].x;
        int y = centerOfGravity[getShape().ordinal()].y;
        switch (m_rotation) {
            case DEGREES_0:
                center.x = x;
                center.y = y;
                break;
            case DEGREES_90:
                center.x = y;
                center.y = lengthx-x-1;
                break;
            case DEGREES_180:
                center.x = lengthx-1-x;
                center.y = lengthy-1-y;
                break;
            case DEGREES_270:
                center.x = lengthy-1-y;
                center.y = x;
                break;
            default:
                center.x = x;
                center.y = y;
                break;
        }
        return center;
    }

    public Point getPosition() {
        return m_position;
    }

    public void setPosition(Point position) {
        m_position = position;
    }

    public void setRotation(BlockRotation rotation) {
        m_rotation = rotation;
    }

    boolean GetBlockData(int x,int y) {
        boolean retVal=false;
        boolean[][] shapetemplate = shapeDataTemplates[getShape().ordinal()];
        int width = shapeDataTemplates[getShape().ordinal()].length;
        int height = shapeDataTemplates[getShape().ordinal()][0].length;
        switch (m_rotation) {
            case DEGREES_0:
                if (x>=0&&x<width) {
                    if (y>=0&&y<height) {
                        //valid enty
                        retVal = shapetemplate[x][y];
                    }
                }
                break;
            case DEGREES_90:
                if (y>=0&&y<width) {
                    if (x>=0&&x<height) {
                        //valid enty
                        retVal = shapetemplate[width-y-1][x];
                    }
                }
                break;
            case DEGREES_180:
                if (x>=0&&x<width) {
                    if (y>=0&&y<height) {
                        //valid enty
                        retVal = shapetemplate[width-x-1][height-y-1];
                    }
                }
                break;
            case DEGREES_270:
                if (y>=0&&y<width) {
                    if (x>=0&&x<height) {
                        //valid enty
                        retVal = shapetemplate[y][height-x-1];
                    }
                }
                break;
            default:
                if (x>=0&&x<shapetemplate.length) {
                    if (y>=0&&y<shapetemplate[x].length) {
                        //valid enty
                        retVal = shapetemplate[x][y];
                    }
                }
                break;
        }
        return retVal;
    }

    public BlockRotation getRotation() {
        return m_rotation;
    }

    public void rotateClockwise() {
        setRotation(blockRotationValues[(m_rotation.ordinal() + 1) % blockRotationValues.length]);
    }

    public void rotateCounterClockwise() {
        setRotation(blockRotationValues[(m_rotation.ordinal() - 1) % blockRotationValues.length]);
    }

    public String toString() {
        return Integer.toString(getShape().ordinal()) + Integer.toString(getRotation().ordinal()) + Integer.toString(m_position.x + 1) + Integer.toString(m_position.y + 1) + Integer.toString(getColor().ordinal());
    }
}