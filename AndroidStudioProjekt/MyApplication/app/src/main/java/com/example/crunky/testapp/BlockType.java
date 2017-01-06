package com.example.crunky.testapp;

/**
 * Created by Daniel on 31.12.2016.
 */
enum BlockShape {SIMPLE_SQUARE, QUADRUPLE_SQUARE, I_SHAPE, MIRRORED_L_SHAPE, L_SHAPE,
    FOUR_SHAPE, MIRRORED_T_SHAPE, MIRRORRED_FOUR_SHAPE}

enum BlockColor {BLACK, RED, GREEN, BLUE, YELLOW}

public class BlockType {
    private final BlockShape m_shape;
    private final BlockColor m_color;

    public BlockType(BlockShape shape, BlockColor color) {
        m_shape=shape;
        m_color=color;
    }

    public BlockShape getShape() {
        return m_shape;
    }

    public BlockColor getColor() {
        return m_color;
    }

    public boolean equals(BlockType blockType) {
        return ((blockType != null) && (m_shape == blockType.m_shape) && (m_color == blockType.m_color));
    }
}