package me.hollow.trollgod.api.util.font;

import me.hollow.trollgod.api.interfaces.Minecraftable;
import me.hollow.trollgod.client.modules.visual.HUD;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;
import java.util.regex.Pattern;

public class RainbowText
implements Minecraftable {
    public static Rainbow rainbow = new Rainbow();
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("\u00a7[0123456789abcdefklmnor]");
    private static final int[] colorCodes = new int[]{0, 170, 43520, 43690, 0xAA0000, 0xAA00AA, 0xFFAA00, 0xAAAAAA, 0x555555, 0x5555FF, 0x55FF55, 0x55FFFF, 0xFF5555, 0xFF55FF, 0xFFFF55, 0xFFFFFF};

    public static void drawStringWithShadow(String text, double x, double y, int color, boolean useCustom) {
        int rgb = color;
        int displayColor = 0;
        char[] characters = text.toCharArray();
        String[] parts = COLOR_CODE_PATTERN.split(text);
        int index = 0;
        for (String part : parts) {
            char colorCode;
            String s = part;
            String[] parts2 = s.split ( "" );
            for (String s1 : parts2) {
                if ( displayColor == 0 ) {
                    rgb = rainbow.drawStringWithShadow ( s1 , (float) x , (float) y , rgb );
                } else {
                    rgb = rainbow.updateRainbow ( rgb );
                    RainbowText.mc.fontRenderer.drawStringWithShadow ( s1 , (float) x , (float) y , displayColor );
                }
                x += rainbow.getCharWidth ( s1.charAt ( 0 ) );
                ++ index;
            }
            if ( index >= characters.length || ( colorCode = characters[index] ) != '\u00a7' ) continue;
            char colorChar = characters[index + 1];
            int codeIndex = "0123456789abcdef".indexOf ( colorChar );
            if ( codeIndex < 0 ) {
                if ( useCustom && colorChar == 'r' ) {
                    displayColor = - 1;
                } else if ( colorChar == ( useCustom ? '.' : 'r' ) ) {
                    displayColor = 0;
                }
            } else {
                displayColor = colorCodes[codeIndex];
            }
            index += 2;
        }
    }

    public static class Rainbow {
        private static int rgb;
        public static int a;
        public static int r;
        public static int g;
        public static int b;
        static float hue;
        FontRenderer fontRenderer;
        public int FONT_HEIGHT;

        public Rainbow() {
            this.fontRenderer = Minecraftable.mc.fontRenderer;
            this.FONT_HEIGHT = 9;
        }

        public void updateRainbow() {
            rgb = Color.HSBtoRGB(hue, (float) HUD.saturation.getValue ( ) / 255.0f, (float) HUD.brightness.getValue ( ) / 255.0f);
            a = rgb >>> 24 & 0xFF;
            r = rgb >>> 16 & 0xFF;
            g = rgb >>> 8 & 0xFF;
            b = rgb & 0xFF;
            if ((hue += 1.0E-5f) > 1.0f) {
                hue -= 1.0f;
            }
        }

        public int updateRainbow(int IN) {
            float hue2 = Color.RGBtoHSB(new Color(IN).getRed(), new Color(IN).getGreen(), new Color(IN).getBlue(), null)[0];
            if ((hue2 += (float) HUD.factor.getValue ( ) / 1000.0f) > 1.0f) {
                hue2 -= 1.0f;
            }
            return Color.HSBtoRGB(hue2, (float) HUD.saturation.getValue ( ) / 255.0f, (float) HUD.brightness.getValue ( ) / 255.0f);
        }

        public int drawStringWithShadow(String text, float x, float y, int color) {
            if (color == -1) {
                color = rgb;
                this.updateRainbow();
            } else {
                color = this.updateRainbow(color);
            }
            this.fontRenderer.drawStringWithShadow(text, x, y, color);
            return color;
        }

        public int drawString(String text, int x, int y, int color) {
            if (color == -1) {
                color = rgb;
            }
            this.updateRainbow();
            this.fontRenderer.drawStringWithShadow(text, (float)x, (float)y, color);
            return color;
        }

        public int getStringWidth(String text) {
            return this.fontRenderer.getStringWidth(text);
        }

        public int getCharWidth(char character) {
            return this.fontRenderer.getCharWidth(character);
        }

        public int getHeight() {
            return this.FONT_HEIGHT;
        }

        static {
            hue = 0.01f;
        }
    }
}

