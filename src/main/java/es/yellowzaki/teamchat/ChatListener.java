package es.yellowzaki.teamchat;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.util.Util;

public class ChatListener implements Listener {

    private final Map<UUID, Boolean> teamChatUsers;
    private final Set<UUID> spies;
    private final TeamChat addon;
    private final BentoBox plugin;

    public ChatListener(TeamChat addon) {
        this.addon = addon;
        this.teamChatUsers = new ConcurrentHashMap<>();
        this.spies = new HashSet<>();
        this.plugin = addon.getPlugin();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onTeamChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (!isTeamChat(playerUUID)) {
            return;
        }

        event.setCancelled(true);

        // Send chat to every GameMode Addon world
        for (World world : addon.getWorlds()) {

            if (plugin.getIslands().inTeam(world, playerUUID)) {

                // Tell only the team members if they are online
                boolean online = false;
                for (UUID teamMember : plugin.getIslands().getMembers(world, playerUUID)) {

                    if (plugin.getServer().getPlayer(teamMember) != null) {
                        User teamPlayer = plugin.getPlayers().getUser(teamMember);
                        teamPlayer.sendMessage("teamchat.general.message", "[player]", player.getName(), "[message]", event.getMessage());
                        if (!teamMember.equals(playerUUID)) {
                            online = true;
                        }

                    }

                }

                // Disable team chat if no members online
                if (!online) {
                    User sender = plugin.getPlayers().getUser(playerUUID);
                    sender.sendMessage("teamchat.general.no-members-online");
                    sender.sendMessage("teamchat.general.disabled");
                    unSetPlayer(playerUUID);
                }

                // There are admin spying
                if (!spies.isEmpty()) {
                    for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
                        if (isSpying(onlinePlayer.getUniqueId()) && !plugin.getIslands().getMembers(world, onlinePlayer.getUniqueId()).contains(playerUUID)) {
                            User spier = plugin.getPlayers().getUser(onlinePlayer.getUniqueId());
                            spier.sendMessage("teamchat.general.spy", "[player]", player.getName(), "[message]", event.getMessage());
                        }
                    }
                }

            } else {
                User sender = plugin.getPlayers().getUser(playerUUID);
                sender.sendMessage("teamchat.general.not-in-team");
                sender.sendMessage("teamchat.general.disabled");
                unSetPlayer(playerUUID);
            }
        }

    }

    /**
     * Adds player to team chat
     *
     * @param playerUUID - the player's UUID
     */
    public void setPlayer(UUID playerUUID) {
        this.teamChatUsers.put(playerUUID, true);
    }

    /**
     * Removes player from team chat
     *
     * @param playerUUID - the player's UUID
     */
    public void unSetPlayer(UUID playerUUID) {
        this.teamChatUsers.remove(playerUUID);
    }

    /**
     * Whether the player has team chat on or not
     *
     * @param playerUUID - the player's UUID
     * @return true if team chat is on
     */
    public boolean isTeamChat(UUID playerUUID) {
        return this.teamChatUsers.containsKey(playerUUID);
    }

    public void setSpying(UUID playerUUID) {
        this.spies.add(playerUUID);
    }

    public void unSetSpying(UUID playerUUID) {
        this.spies.remove(playerUUID);
    }

    public boolean isSpying(UUID playerUUID) {
        return this.spies.contains(playerUUID);
    }

}
