package com.example.crunky.testapp;

import android.graphics.*;

/**
 * Created by Daniel on 31.12.2016.
 */

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
    private static Point[] centerOfGravity = {
            new Point(0, 0), // SIMPLE_SQUARE
            new Point(0, 1), // QUADRUPLE_SQUARE
            new Point(0, 1), // I_SHAPE
            new Point(1, 1), // MIRRORED_L_SHAPE
            new Point(0, 1), // L_SHAPE
            new Point(0, 1), // FOUR_SHAPE
            new Point(1, 0), // MIRRORED_T_SHAPE
            new Point(1, 1), // MIRRORRED_FOUR_SHAPE
    };
    private int m_xFactor;
    private int m_yFactor;
    private int m_xShifting;
    private int m_yShifting;
    private Point m_position;

    public Block(BlockShape shape, BlockColor color, BlockRotation rotation) {
        super(shape, color);
        setRotation(rotation);
        m_position = null;
    }

    public Point getCenterOfGravity() {
        Point center = centerOfGravity[getShape().ordinal()];
        return new Point(center.x * m_xFactor + m_xShifting, center.y * m_yFactor + m_yShifting);
    }

    public Point getPosition() {
        return m_position;
    }

    public void setPosition(Point position) {
        m_position = position;
    }

    public boolean[][] getShapeData() {
        boolean[][] template = shapeDataTemplates[getShape().ordinal()];
        boolean[][] result = new boolean[template.length][template[0].length];
        for (int i = 0; i < template.length; i++) {
            for (int j = 0; j < template[0].length; j++) {
                result[i * m_xFactor + m_xShifting][i * m_yFactor + m_yShifting] = template[i][j];
            }
        }
        return result;
    }

    public void setRotation(BlockRotation rotation) {
        m_rotation = rotation;
        int count = m_rotation.ordinal();
        if ((count == 0) || (count == 1)) {
            m_xFactor = 1;
            m_xShifting = 0;
        } else {
            m_xFactor = -1;
            m_xShifting = shapeDataTemplates[getShape().ordinal()].length - 1;
        }
        if ((count == 0) || (count == 3)) {
            m_yFactor = 0;
            m_yShifting = 0;
        } else {
            m_yFactor = -1;
            m_yShifting = shapeDataTemplates[getShape().ordinal()][0].length - 1;
        }
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
