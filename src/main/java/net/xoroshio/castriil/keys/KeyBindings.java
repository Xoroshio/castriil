package net.xoroshio.castriil.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

import java.util.function.Consumer;

public class KeyBindings {

    public static final String CATEGORY = "key.categories.castriil";

    public static final KeyMapping OPEN_GUI = new KeyMapping(
            "key.castriil.gui",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM.getOrCreate(InputConstants.KEY_N),
            CATEGORY);

    public static final KeyMapping ATTACK = new KeyMapping(
            "key.castriil.attack",
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            CATEGORY);

    public static final KeyMapping ATTACK_THOUGH_WALLS = new KeyMapping(
            "key.castriil.attack_through_walls",
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            CATEGORY);

    public static final KeyMapping LOOK_AT_ENEMY_PLAYER = new KeyMapping(
            "key.castriil.look_at_enemy_player",
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            CATEGORY);

    public static final KeyMapping BRIDGE = new KeyMapping(
            "key.castriil.bridge",
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            CATEGORY);

    public static void register(Consumer<KeyMapping> register){
        register.accept(OPEN_GUI);
        register.accept(ATTACK);
        register.accept(ATTACK_THOUGH_WALLS);
        register.accept(LOOK_AT_ENEMY_PLAYER);
        register.accept(BRIDGE);
    }
}
