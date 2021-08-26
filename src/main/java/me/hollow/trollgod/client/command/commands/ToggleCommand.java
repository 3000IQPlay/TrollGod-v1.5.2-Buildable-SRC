package me.hollow.trollgod.client.command.commands;

import me.hollow.trollgod.TrollGod;
import me.hollow.trollgod.api.util.MessageUtil;
import me.hollow.trollgod.client.command.Command;
import me.hollow.trollgod.client.command.CommandManifest;
import me.hollow.trollgod.client.modules.Module;

@CommandManifest(label="Toggle", aliases={"t"})
public class ToggleCommand
extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            return;
        }
        Module module = TrollGod.INSTANCE.getModuleManager().getModuleByLabel(args[1]);
        if (module != null) {
            module.toggle();
            MessageUtil.sendClientMessage(module.getLabel() + " has been toggled " + (module.isEnabled() ? "on" : "off"), false);
        }
    }
}

