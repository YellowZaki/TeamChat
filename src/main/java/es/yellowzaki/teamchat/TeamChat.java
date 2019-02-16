package es.yellowzaki.teamchat;

import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.GameModeAddon;



public class TeamChat extends Addon {

    // Team chat listener
    private ChatListener chatListener;

    @Override
    public void onEnable() {
        for (GameModeAddon gameModeAddon : getPlugin().getAddonsManager().getGameModeAddons()) {
            
            if (gameModeAddon.getPlayerCommand().isPresent()) {
                new TeamChatCommand(this, gameModeAddon.getPlayerCommand().get());
            }
            if (gameModeAddon.getAdminCommand().isPresent()) {
                new TeamChatSpyCommand(this, gameModeAddon.getAdminCommand().get());
            }            
            
        }
        chatListener = new ChatListener(this);
        registerListener(chatListener);
    }

    @Override
    public void onDisable() {
        
    }

    public ChatListener getChatListener() {
        return chatListener;
    }

}
