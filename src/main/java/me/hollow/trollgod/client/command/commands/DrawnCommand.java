package me.hollow.trollgod.client.command.commands;

import me.hollow.trollgod.TrollGod;
import me.hollow.trollgod.api.util.MessageUtil;
import me.hollow.trollgod.client.command.Command;
import me.hollow.trollgod.client.command.CommandManifest;
import me.hollow.trollgod.client.modules.Module;

@CommandManifest(label="Drawn", aliases={"Hide", "d"})
public class DrawnCommand
extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            return;
        }
        Module module = TrollGod.INSTANCE.getModuleManager().getModuleByLabel(args[1]);
        if (module != null) {
            module.setDrawn(module.isHidden());
            MessageUtil.sendClientMessage(module.getLabel() + " has been " + (module.isHidden() ? "hidden" : "unhidden"), false);
        }
    }
}

