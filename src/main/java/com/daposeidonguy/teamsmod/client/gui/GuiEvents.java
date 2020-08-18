package com.daposeidonguy.teamsmod.client.gui;

import com.daposeidonguy.teamsmod.TeamsMod;
import com.daposeidonguy.teamsmod.client.ClientHelper;
import com.daposeidonguy.teamsmod.client.gui.overlay.CompassOverlay;
import com.daposeidonguy.teamsmod.client.gui.overlay.StatusOverlay;
import com.daposeidonguy.teamsmod.client.gui.widget.ChatButton;
import com.daposeidonguy.teamsmod.client.keybind.KeyBindHandler;
import com.daposeidonguy.teamsmod.common.config.ConfigHelper;
import com.daposeidonguy.teamsmod.common.config.TeamConfig;
import com.daposeidonguy.teamsmod.common.network.NetworkHelper;
import com.daposeidonguy.teamsmod.common.network.messages.MessageTeamChat;
import com.daposeidonguy.teamsmod.common.storage.StorageHelper;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = TeamsMod.MODID, value = Side.CLIENT)
public class GuiEvents {

    public static Method renderName = ReflectionHelper.findMethod(Render.class, "renderLivingLabel", "func_147906_a", Entity.class, String.class, Double.TYPE, Double.TYPE, Double.TYPE, Integer.TYPE);

    /* Displays chat bubbles */
    @SubscribeEvent
    public static void onRenderPlayer(final RenderPlayerEvent.Pre event) throws InvocationTargetException, IllegalAccessException {
        String playerName = event.getEntityPlayer().getGameProfile().getName();
        String localName = ClientHelper.mc.player.getGameProfile().getName();
        if (!localName.equals(playerName) && GuiHandler.chatMap.containsKey(playerName) && !TeamConfig.common.disableChatBubble && !ConfigHelper.serverDisableBubble) {
            String text = GuiHandler.chatMap.get(playerName).first();
            long tick = GuiHandler.chatMap.get(playerName).second();
            if ((ClientHelper.ticks - tick) < 200) {
                GuiEvents.renderName.invoke(event.getRenderer(), event.getEntityPlayer(), text, event.getX(), event.getY() + 0.5, event.getZ(), 64);
            } else {
                GuiHandler.chatMap.remove(playerName);
            }
        }
    }

    /* Handles teams chat tab and button */
    @SubscribeEvent
    public static void onChatScreen(final GuiScreenEvent.InitGuiEvent.Post event) throws IllegalAccessException {
        if (event.getGui() instanceof GuiChat) {
            String myTeam = StorageHelper.getTeam(ClientHelper.mc.player.getUniqueID());
            if (myTeam != null) {
                int defaultWidth = ClientHelper.mc.fontRenderer.getStringWidth("Display: Server Chat");
                ChatButton button = new ChatButton(GuiHandler.BUTTON_CHAT, (int) (ClientHelper.getWindow().getScaledWidth() * 0.99 - defaultWidth), (int) (ClientHelper.getWindow().getScaledHeight() * 0.89), defaultWidth, 10);
                event.getButtonList().add(button);
            } else if (GuiHandler.displayTeamChat) {
                GuiHandler.displayTeamChat = false;
                GuiNewChat oldGui = (GuiNewChat) GuiHandler.persistentChatGUI.get(ClientHelper.mc.ingameGUI);
                GuiHandler.persistentChatGUI.set(ClientHelper.mc.ingameGUI, GuiHandler.backupChatGUI);
                GuiHandler.backupChatGUI = oldGui;
                NetworkHelper.sendToServer(new MessageTeamChat(ClientHelper.mc.player.getUniqueID(), GuiHandler.displayTeamChat));
            }
        }
    }

    /* Render HUD elements */
    @SubscribeEvent
    public static void renderHUDEvent(final RenderGameOverlayEvent.Post event) {
        //Check if clientside and HUD is enabled
        if (FMLCommonHandler.instance().getEffectiveSide().isClient() &&
                !event.isCancelable() &&
                event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            UUID id = ClientHelper.mc.player.getUniqueID();
            String team = StorageHelper.getTeam(id);
            if (team != null) {
                if (!TeamConfig.client.disableCompassOverlay && KeyBindHandler.doDisplayCompass && !ConfigHelper.serverDisableCompass) {
                    new CompassOverlay(ClientHelper.mc, team);
                }
                if (!TeamConfig.client.disableStatusOverlay && KeyBindHandler.doDisplayStatus && !ConfigHelper.serverDisableStatus) {
                    new StatusOverlay(ClientHelper.mc, team);
                }
            }
        }
    }

}