package me.hollow.trollgod.client.command.commands;

import me.hollow.trollgod.TrollGod;
import me.hollow.trollgod.client.command.Command;
import me.hollow.trollgod.client.command.CommandManifest;

@CommandManifest(label="Friend", aliases={"friends", "friend, f"})
public class FriendCommand
extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            return;
        }
        try {
            String name = args[2];
            switch (args[1].toUpperCase()) {
                case "ADD": {
                    TrollGod.INSTANCE.getFriendManager().addFriend(name);
                    break;
                }
                case "DEL": {
                    TrollGod.INSTANCE.getFriendManager().removeFriend(name);
                    break;
                }
                case "DELETE": {
                    TrollGod.INSTANCE.getFriendManager().removeFriend(name);
                    break;
                }
                case "CLEAR": {
                    TrollGod.INSTANCE.getFriendManager().clearFriends();
                    break;
                }
                case "INSIDE": {
                    TrollGod.INSTANCE.getFriendManager().clearFriends();
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

