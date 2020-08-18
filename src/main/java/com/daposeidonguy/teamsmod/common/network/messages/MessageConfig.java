package com.daposeidonguy.teamsmod.common.network.messages;

import com.daposeidonguy.teamsmod.common.config.ConfigHelper;
import com.daposeidonguy.teamsmod.common.config.TeamConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageConfig extends AbstractMessage {

    public MessageConfig() {
        tag.setBoolean("disablePing", TeamConfig.common.disablePing);
        tag.setBoolean("disableTransfer", TeamConfig.server.disableInventoryTransfer);
        tag.setBoolean("disableDeathSound", TeamConfig.common.disableDeathSound);
        tag.setBoolean("disableCompass", TeamConfig.server.forceDisableCompass);
        tag.setBoolean("disableStatus", TeamConfig.server.forceDisableStatus);
        tag.setBoolean("disableBubble", TeamConfig.common.disableChatBubble);
    }

    public static class MessageHandler implements IMessageHandler<MessageConfig, IMessage> {
        @Override
        public IMessage onMessage(MessageConfig message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                ConfigHelper.serverDisablePing = message.tag.getBoolean("disablePing");
                ConfigHelper.serverDisableTransfer = message.tag.getBoolean("disableTransfer");
                ConfigHelper.serverDisableDeathSound = message.tag.getBoolean("disableDeathSound");
                ConfigHelper.serverDisableCompass = message.tag.getBoolean("disableCompass");
                ConfigHelper.serverDisableStatus = message.tag.getBoolean("disableStatus");
                ConfigHelper.serverDisableBubble = message.tag.getBoolean("disableBubble");
            });
            return null;
        }
    }
}
