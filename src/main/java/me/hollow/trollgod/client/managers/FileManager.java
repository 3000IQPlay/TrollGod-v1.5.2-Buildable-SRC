package me.hollow.trollgod.client.managers;

import me.hollow.trollgod.client.modules.Module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
    private final Path base = this.getMkDirectory(this.getRoot(), "TrollGod/modules/");

    private Path lookupPath(Path root, String ... paths) {
        return Paths.get(root.toString(), paths);
    }

    private Path getRoot() {
        return Paths.get("" );
    }

    private void createDirectory(Path dir) {
        try {
            if (!Files.isDirectory(dir )) {
                if (Files.exists(dir )) {
                    Files.delete(dir);
                }
                Files.createDirectories(dir );
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path getMkDirectory(Path parent, String ... paths) {
        if (paths.length < 1) {
            return parent;
        }
        Path dir = this.lookupPath(parent, paths);
        this.createDirectory(dir);
        return dir;
    }

    public FileManager() {
        for (Module.Category category : Module.Category.values()) {
            this.getMkDirectory(this.base, category.name());
        }
    }
}

