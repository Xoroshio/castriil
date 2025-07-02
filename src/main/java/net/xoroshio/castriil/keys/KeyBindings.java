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

    public static final KeyMapping SCAFFOLD_BRIDGE = new KeyMapping(
            "key.castriil.scaffold_bridge",
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            CATEGORY);

    public static final KeyMapping KILL_AURA = new KeyMapping(
            "key.castriil.kill_aura",
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            CATEGORY);

    public static final KeyMapping OPEN_SHOP = new KeyMapping(
            "key.castriil.open_shop",
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            CATEGORY);

    public static final KeyMapping SET_SHOP = new KeyMapping(
            "key.castriil.set_shop",
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            CATEGORY);

    public static final KeyMapping OPEN_UPGRADES = new KeyMapping(
            "key.castriil.open_upgrades",
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            CATEGORY);
    public static final KeyMapping SET_UPGRADES = new KeyMapping(
            "key.castriil.set_upgrades",
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            CATEGORY);

    public static final KeyMapping TOGGLE_FLYING = new KeyMapping(
            "key.castriil.toggle_flying",
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            CATEGORY);

    public static final KeyMapping FLY_UP = new KeyMapping(
            "key.castriil.fly_up",
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            CATEGORY);

    public static final KeyMapping TEST = new KeyMapping(
            "Test (Development Purposes)",
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            CATEGORY);

    public static void register(Consumer<KeyMapping> register){
        register.accept(OPEN_GUI);
        register.accept(KILL_AURA);
        register.accept(SCAFFOLD_BRIDGE);
        register.accept(OPEN_SHOP);
        register.accept(OPEN_UPGRADES);
        register.accept(TOGGLE_FLYING);
        register.accept(FLY_UP);
        register.accept(SET_SHOP);
        register.accept(SET_UPGRADES);
        register.accept(TEST);
    }
}
