package io.github.moulberry.moulconfig.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface RenderContext {
    void pushMatrix();

    void popMatrix();

    void translate(float x, float y, float z);

    void scale(float x, float y, float z);

    void color(float r, float g, float b, float a);

    boolean isMouseButtonDown(int mouseButton);

    boolean isKeyboardKeyDown(int keyboardKey);

    default boolean isShiftDown() {
        return isKeyboardKeyDown(KeyboardConstants.INSTANCE.getShiftLeft()) || isKeyboardKeyDown(KeyboardConstants.INSTANCE.getShiftRight());
    }

    default boolean isCtrlDown() {
        return isKeyboardKeyDown(KeyboardConstants.INSTANCE.getCtrlLeft()) || isKeyboardKeyDown(KeyboardConstants.INSTANCE.getCtrlRight());
    }

    default void drawStringScaledMaxWidth(@NotNull String text, @NotNull IFontRenderer fontRenderer, int x, int y, boolean shadow, int width, int color) {
        pushMatrix();
        translate(x, y, 0);
        float scale = Math.min(1F, Math.max(0.1F, width / (float) fontRenderer.getStringWidth(text)));
        scale(scale, scale, 1F);
        drawString(fontRenderer, text, 0, 0, color, shadow);
        popMatrix();
    }

    int drawString(IFontRenderer fontRenderer, String text, int x, int y, int color, boolean shadow);

    void drawColoredRect(float left, float top, float right, float bottom, int color);

    void invertedRect(float left, float top, float right, float bottom);

    void drawTexturedRect(float x, float y, float width, float height);

    void renderDarkRect(int x, int y, int width, int height);

    void pushScissor(int left, int top, int right, int bottom);

    void popScissor();

    void renderItemStack(@NotNull IItemStack itemStack, int x, int y, @Nullable String overlayText);

    void scheduleDrawTooltip(@NotNull List<String> tooltipLines);

    void doDrawTooltip();
}
