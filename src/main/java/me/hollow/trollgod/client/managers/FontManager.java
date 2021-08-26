package me.hollow.trollgod.client.managers;

import me.hollow.trollgod.api.interfaces.Minecraftable;
import me.hollow.trollgod.api.util.font.CFontRenderer;
import me.hollow.trollgod.client.modules.client.FontModule;

import java.awt.*;

public class FontManager
implements Minecraftable {
    public CFontRenderer fontRenderer = new CFontRenderer(new Font("Verdana", 0, 18), true, true);
    public boolean customFont;

    public void drawString(String text, float x, float y, int color) {
        if (this.customFont) {
            this.fontRenderer.drawStringWithShadow(text, x, y, color);
        } else {
            FontManager.mc.fontRenderer.drawStringWithShadow(text, x, y, color);
        }
    }

    public int getStringHeight() {
        if (this.customFont) {
            return FontManager.mc.fontRenderer.FONT_HEIGHT;
        }
        return this.fontRenderer.getStringHeight("A");
    }

    public void updateFont() {
        this.fontRenderer = new CFontRenderer(new Font(FontModule.INSTANCE.font.getValue(), 0, FontModule.INSTANCE.size.getValue()), FontModule.INSTANCE.antiAlias.getValue(), true);
    }

    public int getStringWidth(String text) {
        if (this.customFont) {
            return (int)this.fontRenderer.getStringWidth(text);
        }
        return FontManager.mc.fontRenderer.getStringWidth(text);
    }
}

