package com.daposeidonguy.teamsmod.client.gui.screen.team;

import com.daposeidonguy.teamsmod.client.gui.screen.AbstractScreenBase;
import com.daposeidonguy.teamsmod.common.config.ConfigHandler;
import com.daposeidonguy.teamsmod.common.storage.StorageHelper;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;


public class ScreenMain extends AbstractScreenBase {

    public ScreenMain() {
        super(new TranslationTextComponent("teamsmod.main.title"), null);
    }

    @Override
    protected void init() {
        super.init();
        this.goBack.setMessage(I18n.format("teamsmod.button.close"));
        boolean inTeam = StorageHelper.isPlayerInTeam(minecraft.player.getUniqueID());
        String editorText = inTeam ? I18n.format("teamsmod.main.manage") : I18n.format("teamsmod.main.create");
        this.addButton(new Button(BUTTON_CENTERED_X, guiTop + 25, BUTTON_WIDTH, BUTTON_HEIGHT, editorText, btn -> {
            if (inTeam) {
                minecraft.displayGuiScreen(new ScreenTeamEditor(this, StorageHelper.getTeam(minecraft.player.getUniqueID())));
            } else {
                minecraft.displayGuiScreen(new ScreenTeamCreator(this));
            }
        }));
        this.addButton(new Button(BUTTON_CENTERED_X, guiTop + 50, BUTTON_WIDTH, BUTTON_HEIGHT, I18n.format("teamsmod.main.list"), btn -> {
            minecraft.displayGuiScreen(new ScreenTeamList(this));
        }));
        Button buttonTransfer = this.addButton(new Button(BUTTON_CENTERED_X, guiTop + 75, BUTTON_WIDTH, BUTTON_HEIGHT, I18n.format("teamsmod.main.transfer"), btn -> {
            minecraft.displayGuiScreen(new ScreenTransferList(this));
        }));
        buttonTransfer.active = inTeam && !ConfigHandler.serverDisableTransfer;
        Button buttonHud = this.addButton(new Button(BUTTON_CENTERED_X, guiTop + 100, BUTTON_WIDTH, BUTTON_HEIGHT, I18n.format("teamsmod.main.hud"), btn -> {
            minecraft.displayGuiScreen(new ScreenHudConfig(this));
        }));
        buttonHud.active = inTeam;
    }
}
