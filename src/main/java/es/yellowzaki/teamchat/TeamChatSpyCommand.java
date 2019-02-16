package es.yellowzaki.teamchat;

import java.util.List;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;
import static es.yellowzaki.teamchat.TeamChatCommand.TEAMCHAT_COMMAND;


public class TeamChatSpyCommand extends CompositeCommand {

    public static final String TEAMCHATSPY_COMMAND = "teamchatspy";
    private final BentoBox plugin;
    private final TeamChat addon;

    public TeamChatSpyCommand(TeamChat addon, CompositeCommand cmd) {
        super(addon, cmd, TEAMCHATSPY_COMMAND);
        this.plugin = this.getPlugin();
        this.addon = addon;
    }

    @Override
    public void setup() {
        this.setPermission("admin.teamchatspy");
        this.setParametersHelp("teamchat.commands.teamchatspy.parameters");
        this.setDescription("teamchat.commands.teamchatspy.description");
    }

    @Override
    public boolean execute(User user, String string, List<String> list) {
            if (addon.getChatListener().isSpying(user.getUniqueId())) {
                // Toggle
                user.sendMessage("teamchat.general.spydisabled");
                addon.getChatListener().unSetSpying(user.getUniqueId());
            } else {
                user.sendMessage("teamchat.general.spyenabled");
                addon.getChatListener().setSpying(user.getUniqueId());
            }
            
            return true;
    }
    
    

}
