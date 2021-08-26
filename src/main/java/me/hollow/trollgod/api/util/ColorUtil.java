package me.hollow.trollgod.api.util;

import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ColorUtil {
    public static final int FRIEND_COLOR = -11157267;

    public static int getRainbow(int speed, int offset, float s) {
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        return Color.getHSBColor(hue / (float)speed, s, 1.0f).getRGB();
    }

    public static void color(int color) {
        GL11.glColor4f( (float)(color >> 16 & 0xFF) / 255.0f , (float)(color >> 8 & 0xFF) / 255.0f , (float)(color & 0xFF) / 255.0f , (float)(color >> 24 & 0xFF) / 255.0f );
    }

    public static int toRGBA(int r, int g, int b) {
        return ColorUtil.toRGBA(r, g, b, 255);
    }

    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }

    public static int toRGBA(Color color) {
        return ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static Color getGradientOffset(Color color1, Color color2, double offset) {
        if (offset > 1.0) {
            double left = offset % 1.0;
            int off = (int)offset;
            offset = off % 2 == 0 ? left : 1.0 - left;
        }
        double inverse_percent = 1.0 - offset;
        int redPart = (int)((double)color1.getRed() * inverse_percent + (double)color2.getRed() * offset);
        int greenPart = (int)((double)color1.getGreen() * inverse_percent + (double)color2.getGreen() * offset);
        int bluePart = (int)((double)color1.getBlue() * inverse_percent + (double)color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }
}

