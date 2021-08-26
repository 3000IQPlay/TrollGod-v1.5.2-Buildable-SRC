package me.hollow.trollgod.client.managers;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import me.hollow.trollgod.api.util.MessageUtil;
import net.minecraft.entity.player.EntityPlayer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FriendManager {
    private Set<String> friends = new HashSet <> ( );
    private File directory;

    public void init() {
        if (!this.directory.exists()) {
            try {
                this.directory.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.loadFriends();
    }

    public void unload() {
        this.saveFriends();
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public void saveFriends() {
        if (this.directory.exists()) {
            try (FileWriter writer = new FileWriter(this.directory)){
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(this.friends));
            }
            catch (IOException e) {
                this.directory.delete();
            }
        }
    }

    public void loadFriends() {
        if (!this.directory.exists()) {
            return;
        }
        try (FileReader inFile = new FileReader(this.directory)){
            this.friends = new HashSet <> ( new GsonBuilder ( ).setPrettyPrinting ( ).create ( ).fromJson ( inFile , new TypeToken < HashSet < String > > ( ) {
            }.getType ( ) ) );
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void addFriend(String name) {
        MessageUtil.sendClientMessage("Added " + name + " as a friend ", false);
        this.friends.add(name);
    }

    public final boolean isFriend(String ign) {
        return this.friends.contains(ign);
    }

    public boolean isFriend(EntityPlayer ign) {
        return this.friends.contains(ign.getName());
    }

    public void clearFriends() {
        this.friends.clear();
    }

    public void removeFriend(String name) {
        this.friends.remove(name);
    }
}

