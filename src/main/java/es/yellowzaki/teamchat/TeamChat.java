package es.yellowzaki.teamchat;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.World;
import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.GameModeAddon;



public class TeamChat extends Addon {

    // Team chat listener
    private ChatListener chatListener;
    private List<World> world;

    @Override
    public void onEnable() {
        world = new ArrayList<>();
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
    
    public void addWorld(World world){
        this.world.add(world);
    }
    
    public List<World> getWorlds(){
        return this.world;
    }

}
