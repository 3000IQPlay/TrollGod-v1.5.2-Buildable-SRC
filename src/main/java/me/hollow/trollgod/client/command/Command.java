package me.hollow.trollgod.client.command;

public class Command {
    String label;
    String[] aliases;

    public Command() {
        if (this.getClass().isAnnotationPresent(CommandManifest.class)) {
            CommandManifest manifest = this.getClass().getAnnotation(CommandManifest.class);
            this.label = manifest.label();
            this.aliases = manifest.aliases();
        }
    }

    public void execute(String[] args) {
    }

    public String getLabel() {
        return this.label;
    }

    public String[] getAliases() {
        return this.aliases;
    }
}

