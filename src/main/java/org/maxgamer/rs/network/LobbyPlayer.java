package org.maxgamer.rs.network;

import org.maxgamer.rs.core.Core;
import org.maxgamer.rs.logon.game.LogonAPI.RemoteWorld;
import org.maxgamer.rs.model.entity.mob.persona.player.CheatLog;
import org.maxgamer.rs.model.entity.mob.persona.player.FriendsList;
import org.maxgamer.rs.model.entity.mob.persona.player.NoSuchProtocolException;
import org.maxgamer.rs.network.io.packet.RSOutgoingPacket;
import org.maxgamer.rs.network.protocol.Lobby637Protocol;
import org.maxgamer.rs.network.protocol.LobbyProtocol;
import org.maxgamer.rs.structure.configs.ConfigSection;

import java.io.IOException;
import java.util.Collection;

/**
 * Represents a player who is currently logged in to the Lobby. Authenticated
 * lobby players are stored in the Lobby, at Core.getServer().getLobby().
 *
 * @author netherfoam
 */
public class LobbyPlayer implements Client {
    /**
     * The session used to create this client
     */
    private Session s;
    private String name;
    private FriendsList friends;
    private LobbyProtocol protocol;
    private ConfigSection config;

    /**
     * A log of all cheats this player has attempted to perform. The violation
     * level slowly decreases over time, however this allows for tracking of
     * potential cheats or abuse.
     */
    private CheatLog cheatLog;

    /**
     * This number is randomly generated by the client when it starts. It is not
     * persistent after restarting the client, as it will change.
     */
    private long uuid = 0;

    /**
     * Creates a new lobby player
     *
     * @param s       the session
     * @param p       the profile the session logged in with
     * @param version the runescape revision they're using
     * @throws NoSuchProtocolException
     */
    public LobbyPlayer(Session s, String name, long uuid) throws NoSuchProtocolException {
        switch (s.getRevision()) {
            case 637:
                this.protocol = new Lobby637Protocol(this);
                break;
            default:
                throw new NoSuchProtocolException("No protocol for revision " + s.getRevision() + " for lobby.");
        }

        this.s = s;
        this.name = name;
        this.uuid = uuid;
        this.cheatLog = new CheatLog(this);
        this.friends = new FriendsList(this);

        //User is now successfully logged in to the lobby! :)
        Core.getServer().getLobby().add(this);

        s.addCloseHandler(new Runnable() {
            @Override
            public void run() {
                destroy();
            }
        });
    }

    /**
     * A logger against this particular player for anything that is suspicious,
     * such as picking up items they can't see, sending bad packets, using
     * interfaces they don't have open, and so on.
     *
     * @return the cheat log
     */
    public CheatLog getCheats() {
        return this.cheatLog;
    }

    /**
     * Fetches the UUID for this client. The client generates this UUID on
     * startup, and therefore it is not persistent through restarts of the
     * client.
     *
     * @return the UUID for this client.
     */
    @Override
    public long getUUID() {
        return this.uuid;
    }

    @Override
    public Session getSession() {
        return s;
    }

    @Override
    public int getVersion() {
        return s.getRevision();
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Writes the given packet to this player. This simply calls write() on the
     * session.
     *
     * @param out the packet to write
     * @throws IOException if there was an IO error (Eg closed session)
     */
    public boolean write(RSOutgoingPacket out) {
        try {
            this.getSession().write(out);
            return true;
        } catch (IOException e) {
            //TODO: How do we handle this?
            return false;
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public FriendsList getFriends() {
        return friends;
    }

    @Override
    public void deserialize(ConfigSection map) {
        this.config = map;
        this.friends.deserialize(this.config.getSection("friends"));
    }

    @Override
    public LobbyProtocol getProtocol() {
        return protocol;
    }

    @Override
    public ConfigSection serialize() {
        return config;
    }

    @Override
    public void destroy() {
        Core.getServer().getLobby().remove(this);
        if (s.isConnected()) s.close(true);

        /*
         * RemoteClient remote =
         * Core.getServer().getLogon().getRemoteServer(Core
         * .getServer().getLogon().getWorldId()).get(this.getName()); if(remote
         * != null && remote.getSessionId() ==
         * this.getSession().getSessionId()){
         * Core.getServer().getLogon().leave(this); }
         */

        Core.getServer().getLogon().getAPI().leave(this);
    }

    @Override
    public void sendMessage(String string) {
        getProtocol().sendMessage(string);
    }

    @Override
    public ConfigSection getConfig() {
        return config;
    }
}