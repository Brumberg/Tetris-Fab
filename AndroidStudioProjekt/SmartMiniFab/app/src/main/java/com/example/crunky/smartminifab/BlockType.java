package com.example.crunky.smartminifab;
import java.util.Objects;
/**
 * Created by Daniel on 31.12.2016.
 */
enum BlockShape {SIMPLE_SQUARE, QUADRUPLE_SQUARE, I_SHAPE, MIRRORED_L_SHAPE, L_SHAPE,
    FOUR_SHAPE, MIRRORED_T_SHAPE, MIRRORRED_FOUR_SHAPE}

enum BlockColor {BLACK, RED, GREEN, BLUE, YELLOW, TRANSPARENT}

public class BlockType {
    public static final int NOBLOCKCOLORS=6;
    public static final int NODIFFERENTBLOCKTYPES=8;
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
        return ((blockType != null) && (m_shape == blockType.m_shape) &&
                (m_color == blockType.m_color));
    }

    @Override
    public int hashCode() {
        //return Objects.hash(m_shape, m_color);
        int result = 17;
        result = 31 * result + m_shape.hashCode();
        result = 31 * result + m_color.hashCode();
        return result;
    }
}