package me.hollow.trollgod.api.util.font;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class CFontRenderer
extends CFont {
    protected final CharData[] boldChars = new CharData[256];
    protected final CharData[] italicChars = new CharData[256];
    protected final CharData[] boldItalicChars = new CharData[256];
    private final int[] colorCode = new int[32];
    private final String colorcodeIdentifiers = "0123456789abcdefklmnor";
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;

    public CFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public float drawStringWithShadow(String text, double x, double y, int color) {
        float shadowWidth = this.drawString(text, x + 1.0, y + 1.0, color, true);
        return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
    }

    public float drawString(String text, float x, float y, int color) {
        return this.drawString(text, x, y, color, false);
    }

    public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
        return this.drawStringWithShadow(text, x - (float)this.getStringWidth(text) / 2.0f, y, color);
    }

    public float drawCenteredString(String text, float x, float y, int color) {
        return this.drawString(text, x - (float)this.getStringWidth(text) / 2.0f, y, color);
    }

    public float drawString(String text, double x, double y, int color, boolean shadow) {
        x -= 1.0;
        y -= 2.0;
        if (text == null) {
            return 0.0f;
        }
        if (color == 0x20FFFFFF) {
            color = 0xFFFFFF;
        }
        if ((color & 0xFC000000) == 0) {
            color |= 0xFF000000;
        }
        if (shadow) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        }
        CharData[] currentData = this.charData;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        boolean bold = false;
        boolean italic = false;
        x *= 2.0;
        y *= 2.0;
        GL11.glPushMatrix();
        GlStateManager.scale( 0.5 , 0.5 , 0.5 );
        GlStateManager.enableBlend();
        GlStateManager.blendFunc( 770 , 771 );
        GlStateManager.color( (float)(color >> 16 & 0xFF) / 255.0f , (float)(color >> 8 & 0xFF) / 255.0f , (float)(color & 0xFF) / 255.0f , alpha );
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture( this.tex.getGlTextureId() );
        GL11.glBindTexture( 3553 , this.tex.getGlTextureId() );
        for (int i = 0; i < text.length(); ++i) {
            char character = text.charAt(i);
            if (character == '\u00a7') {
                int colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    GlStateManager.bindTexture( this.tex.getGlTextureId() );
                    currentData = this.charData;
                    if (colorIndex < 0) {
                        colorIndex = 15;
                    }
                    if (shadow) {
                        colorIndex += 16;
                    }
                    int colorcode = this.colorCode[colorIndex];
                    GlStateManager.color( (float)(colorcode >> 16 & 0xFF) / 255.0f , (float)(colorcode >> 8 & 0xFF) / 255.0f , (float)(colorcode & 0xFF) / 255.0f , alpha );
                } else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        GlStateManager.bindTexture( this.texItalicBold.getGlTextureId() );
                        currentData = this.boldItalicChars;
                    } else {
                        GlStateManager.bindTexture( this.texBold.getGlTextureId() );
                        currentData = this.boldChars;
                    }
                } else if (colorIndex == 20) {
                    italic = true;
                    if (bold) {
                        GlStateManager.bindTexture( this.texItalicBold.getGlTextureId() );
                        currentData = this.boldItalicChars;
                    } else {
                        GlStateManager.bindTexture( this.texItalic.getGlTextureId() );
                        currentData = this.italicChars;
                    }
                } else {
                    bold = false;
                    italic = false;
                    GlStateManager.color( (float)(color >> 16 & 0xFF) / 255.0f , (float)(color >> 8 & 0xFF) / 255.0f , (float)(color & 0xFF) / 255.0f , alpha );
                    GlStateManager.bindTexture( this.tex.getGlTextureId() );
                    currentData = this.charData;
                }
                ++i;
                continue;
            }
            if (character >= currentData.length) continue;
            this.drawChar(currentData, character, (float)x, (float)y);
            this.getClass();
            x += currentData[character].width - 8 + 0;
        }
        GL11.glHint( 3155 , 4352 );
        GL11.glPopMatrix();
        return (float)x / 2.0f;
    }

    @Override
    public double getStringWidth(String text) {
        if (text == null) {
            return 0.0;
        }
        int width = 0;
        CharData[] currentData = this.charData;
        int size = text.length();
        for (int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == '\u00a7') {
                ++i;
                continue;
            }
            if (character >= currentData.length) continue;
            this.getClass();
            width += currentData[character].width - 8 + 0;
        }
        return (float)width / 2.0f;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setAntiAlias(boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setFractionalMetrics(boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        this.setupBoldItalicIDs();
    }

    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.texItalicBold = this.setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }

    private void setupMinecraftColorcodes() {
        for (int index = 0; index < 32; ++index) {
            int noClue = (index >> 3 & 1) * 85;
            int red = (index >> 2 & 1) * 170 + noClue;
            int green = (index >> 1 & 1) * 170 + noClue;
            int blue = (index & 1) * 170 + noClue;
            if (index == 6) {
                red += 85;
            }
            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCode[index] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        }
    }
}

