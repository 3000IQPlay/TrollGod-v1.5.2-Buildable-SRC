package me.hollow.trollgod.client.modules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface ModuleManifest {
    String label ( ) default "";

    Module.Category category ( );

    int key ( ) default 0;

    boolean persistent ( ) default false;

    boolean drawn ( ) default true;

    boolean listen ( ) default true;

    int color ( ) default -1;
}

