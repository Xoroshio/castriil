package net.xoroshio.castriil;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.GameShuttingDownEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xoroshio.castriil.gui.CastriilScreen;
import net.xoroshio.castriil.keys.KeyBindings;
import org.slf4j.Logger;

import java.io.*;
import java.util.List;

@Mod(Castriil.MOD_ID)
@Mod.EventBusSubscriber(modid = Castriil.MOD_ID)
public class Castriil {

    public static final String MOD_ID = "castriil";
    public static final Logger LOGGER = LogUtils.getLogger();



    // Castriil Properties
    public static final String SAVE_FILE_NAME = "castriil.properties";
    private static ScaffoldBridger scaffoldBridger;
    public static boolean CANCEL_SERVER_VELOCITY = false;
    public static boolean FLYING = false;
    public static int GLOW_RANGE = 100;
    public static float KILL_AURA_RADIUS = 4.0f;

    private static Entity shop;
    private static Entity upgrades;

    public Castriil() {
        // Register EventBus
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static boolean shouldGlowFor(Player player, Entity entity){
        return entity.distanceTo(player) < GLOW_RANGE;
    }

    public static boolean shouldCancelServerSetClientMotion(Player player, Vec3 motion){
        return CANCEL_SERVER_VELOCITY;
    }

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent.Pre event){

        try {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;
            ClientLevel level = player.clientLevel;
            scaffoldBridge(player);
            doGUI();
            setShopAndUpgrades();
            runDevTest(player, level);
            killAura(player, level);
            openShop(player, level);
            // nope
            openUpgrades(player, level);
            fly(player);
        } catch (Throwable t){
            LOGGER.error("Error during Castriil Client Tick", t);
        }
    }

    private static void runDevTest(Player player, Level level){
        if(KeyBindings.TEST.consumeClick()){
            level.getEntities(player, Util.around(player.position(), KILL_AURA_RADIUS), entity -> !entity.isInvulnerableTo(new DamageSource(DamageTypes.PLAYER_ATTACK.getOrThrow(level), player)) && entity instanceof LivingEntity)
                    .forEach(entity -> {
                        CompoundTag data = new CompoundTag();
                        entity.save(data);
                        System.out.println(data);
                    });
        }
    }

    private static void doGUI(){
        if (KeyBindings.OPEN_GUI.consumeClick()) {
            Minecraft.getInstance().setScreen(new CastriilScreen());
        }
    }

    private static void scaffoldBridge(LocalPlayer player){

        if(scaffoldBridger == null){
            scaffoldBridger = new ScaffoldBridger(new PlayerController(player, Minecraft.getInstance()));
        } else {
            scaffoldBridger.getController().validate();
        }


        if(KeyBindings.SCAFFOLD_BRIDGE.consumeClick() && !scaffoldBridger.isStarted()){
            scaffoldBridger.start(Util.getHorizontalDirection(player));
        }

        if(!KeyBindings.SCAFFOLD_BRIDGE.isDown() && scaffoldBridger.isStarted()){
            scaffoldBridger.requestStop();
        }

        if(scaffoldBridger.isStarted()){
            scaffoldBridger.tick();
        }
    }

    private static void killAura(Player player, Level level){
        if(KeyBindings.KILL_AURA.isDown()){
            Inventory inventory = player.getInventory();
            inventory.selected = Util.getBestSwordIndex(inventory);
            level.getEntities(player, Util.around(player.position(), KILL_AURA_RADIUS), e -> !e.isInvulnerableTo(new DamageSource(DamageTypes.PLAYER_ATTACK.getOrThrow(level), player)) && e instanceof LivingEntity)
                    .forEach(entity -> attack(player, entity));
        }
    }

    private static void fly(Player player){
        if(KeyBindings.TOGGLE_FLYING.consumeClick()){
            FLYING = !FLYING;
        }
        if(FLYING){
            player.setDeltaMovement(player.getDeltaMovement().x, 0.0D, player.getDeltaMovement().z);
            if(KeyBindings.FLY_UP.isDown()){
                Vec3 oldPos = player.getPosition(0.0f);
                player.setPos(oldPos.add(0.0f, 0.1f, 0.0f));
                player.xo = oldPos.x;
                player.yo = oldPos.y;
                player.zo = oldPos.z;
                player.xOld = oldPos.x;
                player.yOld = oldPos.y;
                player.zOld = oldPos.z;
            }
        }
    }

    private static void openShop(Player player, Level level){
        if(KeyBindings.OPEN_SHOP.consumeClick()){
            attack(player, shop);
        }
    }

    private static void openUpgrades(Player player, Level level){
        if(KeyBindings.OPEN_UPGRADES.consumeClick()){
            attack(player, upgrades);
        }
    }

    private static void setShopAndUpgrades(){
        if(Minecraft.getInstance().hitResult instanceof EntityHitResult hit){
            Entity entity = hit.getEntity();
            if(KeyBindings.SET_SHOP.consumeClick()){
                shop = entity;
            }
            if(KeyBindings.SET_UPGRADES.consumeClick()){
                upgrades = entity;
            }
        }
    }


    private static void attack(Player player, Entity entity){
        if(entity == null)return;
        MultiPlayerGameMode gameMode = Minecraft.getInstance().gameMode;
        if(gameMode != null){
            gameMode.attack(player, entity);
        }
    }

    @SubscribeEvent
    public static void stop(GameShuttingDownEvent event){
        save(getSaveFile());
    }

    public static void save(File file){
        new CastriilProperties().fromCastriil().write(file);
    }

    public static void read(File file){
        CastriilProperties.read(file).setCastriil();
    }

    public static File getSaveFile(){
        return new File(Minecraft.getInstance().gameDirectory, SAVE_FILE_NAME);
    }
}
