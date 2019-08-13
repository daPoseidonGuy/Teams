package com.daposeidonguy.teamsmod;

import com.daposeidonguy.teamsmod.client.GuiTeam;
import com.daposeidonguy.teamsmod.client.Keybind;
import com.daposeidonguy.teamsmod.commands.CommandTeam;
import com.daposeidonguy.teamsmod.handlers.ClientEventHandler;
import com.daposeidonguy.teamsmod.network.MessageHunger;
import com.daposeidonguy.teamsmod.network.MessageRequestHunger;
import com.daposeidonguy.teamsmod.network.MessageSaveData;
import com.daposeidonguy.teamsmod.network.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = TeamsMod.MODID, name = TeamsMod.NAME, version = TeamsMod.VERSION)
public class TeamsMod
{

    public static final String MODID = "teamsmod";
    public static final String NAME = "Teams Mod";
    public static final String VERSION = "0.6";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PacketHandler.INSTANCE.registerMessage(MessageSaveData.MessageHandler.class,MessageSaveData.class,0, Side.SERVER);
        PacketHandler.INSTANCE.registerMessage(MessageHunger.MessageHandler.class, MessageHunger.class,1,Side.SERVER);
        PacketHandler.INSTANCE.registerMessage(MessageRequestHunger.MessageHandler.class, MessageRequestHunger.class,2,Side.SERVER);
        if(FMLCommonHandler.instance().getSide()==Side.CLIENT) {
            Keybind.register();
            MinecraftForge.EVENT_BUS.register(GuiTeam.GuiHandler.instance());
            MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
            PacketHandler.INSTANCE.registerMessage(MessageSaveData.MessageHandler.class,MessageSaveData.class,0,Side.CLIENT);
            PacketHandler.INSTANCE.registerMessage(MessageHunger.MessageHandler.class, MessageHunger.class,1,Side.CLIENT);
            PacketHandler.INSTANCE.registerMessage(MessageRequestHunger.MessageHandler.class, MessageRequestHunger.class,2,Side.CLIENT);

        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandTeam());
    }
}
