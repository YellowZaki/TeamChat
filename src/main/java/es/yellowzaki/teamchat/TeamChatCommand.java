package es.yellowzaki.teamchat;

import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;

public class TeamChatCommand extends CompositeCommand {

    public static final String TEAMCHAT_COMMAND = "teamchat";
    private final BentoBox plugin;
    private final TeamChat addon;

    public TeamChatCommand(TeamChat addon, CompositeCommand cmd) {
        super(addon, cmd, TEAMCHAT_COMMAND, "tc");
        this.plugin = this.getPlugin();
        this.addon = addon;
    }

    @Override
    public boolean execute(User user, String string, List<String> list) {
        UUID playerUUID = user.getUniqueId();
        // User has a team
        if (plugin.getIslands().inTeam(getWorld(), playerUUID)) {
            // Check if team members are online
            boolean online = false;
            for (UUID teamMember : plugin.getIslands().getMembers(getWorld(), playerUUID)) {
                if (!teamMember.equals(playerUUID) && plugin.getServer().getPlayer(teamMember) != null) {
                    online = true;
                }
            }

            if (!online) {
                user.sendMessage("teamchat.general.no-members-online");
                return true;
            }

            if (addon.getChatListener().isTeamChat(playerUUID)) {
                // Toggle
                user.sendMessage("teamchat.general.disabled");
                addon.getChatListener().unSetPlayer(playerUUID);
            } else {
                user.sendMessage("teamchat.general.enabled");
                addon.getChatListener().setPlayer(playerUUID);
            }

        } else {
            user.sendMessage("teamchat.general.not-in-team");
        }

        return false;

    }

    @Override
    public void setup() {
        this.setPermission("teamchat");
        this.setParametersHelp("teamchat.commands.teamchat.parameters");
        this.setDescription("teamchat.commands.teamchat.description");
        this.setOnlyPlayer(true);
    }

}
