package me.hollow.trollgod.client.command.commands;

import me.hollow.trollgod.TrollGod;
import me.hollow.trollgod.api.property.Bind;
import me.hollow.trollgod.api.util.MessageUtil;
import me.hollow.trollgod.client.command.Command;
import me.hollow.trollgod.client.command.CommandManifest;
import me.hollow.trollgod.client.modules.Module;
import org.lwjgl.input.Keyboard;

@CommandManifest(label="Bind", aliases={"b"})
public class BindCommand
extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            MessageUtil.sendClientMessage("Use the command like this -> (module, bind)", true);
            return;
        }
        Module module = TrollGod.INSTANCE.getModuleManager().getModuleByLabel(args[1]);
        if (module != null) {
            int index = Keyboard.getKeyIndex( args[2].toUpperCase() );
            module.bind.setValue(new Bind(index));
            MessageUtil.sendClientMessage(module.getLabel() + " has been bound to " + Keyboard.getKeyName( index ), false);
        }
    }
}

