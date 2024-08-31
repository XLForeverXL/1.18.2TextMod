package xl.time.Utils;

import com.mojang.math.Matrix4f;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.awt.*;
import java.util.function.Function;

public class FontRender extends Font {


    public FontRender(Function<ResourceLocation, FontSet> p_92717_) {
        super(p_92717_);
    }


    public static FontRender getFontRender() {
        return new FontRender(Minecraft.getInstance().font.fonts);
    }





    @Override
    public int drawInBatch(FormattedCharSequence formattedCharSequence, float x, float y, int c, boolean b, Matrix4f p_92739_, MultiBufferSource p_92740_, boolean p_92741_, int i, int i1) {
        StringBuilder stringBuilder = new StringBuilder();
        formattedCharSequence.accept((index, style, codePoint) -> {
            stringBuilder.appendCodePoint(codePoint);
            return true;
        });
        String text = ChatFormatting.stripFormatting(stringBuilder.toString());
        if (text != null) {
            for (int index = 0; index < text.length(); index++) {
                float hueOffset = (float) index;
                int color = c & -16777216| Color.HSBtoRGB(hueOffset, 0.8f, 0.9f);
                float yOffset = (float) Math.sin((float) index + (float) Util.getMillis() / 300.0F);
                super.drawInBatch(String.valueOf(text.charAt(index)),x ,y + yOffset , color, b, p_92739_, p_92740_, p_92741_, i, i1);
                x += this.width(String.valueOf(text.charAt(index)));
            }
        }
        return (int) x;
    }
}
