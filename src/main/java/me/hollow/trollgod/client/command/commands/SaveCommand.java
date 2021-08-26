package me.hollow.trollgod.client.command.commands;

import me.hollow.trollgod.TrollGod;
import me.hollow.trollgod.client.command.Command;
import me.hollow.trollgod.client.command.CommandManifest;

@CommandManifest(label="Save", aliases={"s"})
public class SaveCommand
extends Command {
    @Override
    public void execute(String[] args) {
        TrollGod.INSTANCE.getConfigManager().saveConfig(TrollGod.INSTANCE.getConfigManager().config.replaceFirst("TrollGod/", ""));
    }
}

