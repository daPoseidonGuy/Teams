package com.daposeidonguy.teamsmod.client;

import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ToastInvite implements IToast {

    String teamName;
    boolean firstDraw = true;
    long firstDrawTime;

    public ToastInvite(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public Visibility draw(GuiToast toastGui, long delta) {
        if (firstDraw) {
            firstDrawTime = delta;
            firstDraw = false;
        }
        toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        toastGui.drawTexturedModalRect(0, 0, 0, 64, 160, 32);
        toastGui.getMinecraft().fontRenderer.drawString("Invited to team: " + this.teamName, 24, 7, Color.WHITE.getRGB());
        toastGui.getMinecraft().fontRenderer.drawString("Press " + Keyboard.getKeyName(Keybind.accept.getKeyCode()) + " to accept", 24, 18, -16777216);

        return delta - this.firstDrawTime < 15000L && this.teamName != null ? IToast.Visibility.SHOW : IToast.Visibility.HIDE;
    }
}
