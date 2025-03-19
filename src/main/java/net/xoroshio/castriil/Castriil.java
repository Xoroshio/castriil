package net.xoroshio.castriil;

import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xoroshio.castriil.keys.KeyBindings;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Predicate;

@Mod(Castriil.MOD_ID)
@Mod.EventBusSubscriber(modid = Castriil.MOD_ID)
public class Castriil {

    public static final String MOD_ID = "castriil";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static boolean CANCEL_SERVER_VELOCITY = true;
    public static boolean SPAM_ATTACK = true;
    public static int ATTACK_DELAY = 1;
    private static long TIME_SINCE_LAST_ATTACK = 0;

    public Castriil() {
        // Register EventBus
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static boolean shouldGlowFor(Player player, Entity entity){
        return entity instanceof Player && entity.distanceTo(player) < 100;
    }

    public static boolean shouldCancelServerSetClientMotion(Player player, Vec3 motion){
        return CANCEL_SERVER_VELOCITY;
    }

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent e){

        try {
            LocalPlayer player = Minecraft.getInstance().player;
            if(player == null)return;
            ClientLevel level = player.clientLevel;
            TIME_SINCE_LAST_ATTACK++;
            if(KeyBindings.OPEN_GUI.consumeClick()){
                Minecraft.getInstance().setScreen(new CastriilScreen());
            }

            HitResult hitResult = Minecraft.getInstance().hitResult;
            if(hitResult instanceof EntityHitResult entityHitResult){
                if(!entityHitResult.getEntity().isInvulnerableTo(new DamageSource(DamageTypes.PLAYER_ATTACK.getOrThrow(level), player))){
                    if((SPAM_ATTACK && KeyBindings.ATTACK.isDown()) || (!SPAM_ATTACK && KeyBindings.ATTACK.consumeClick())){
                        if(!SPAM_ATTACK || TIME_SINCE_LAST_ATTACK > ATTACK_DELAY){
                            Inventory inventory = player.getInventory();
                            inventory.selected = Util.getBestSwordIndex(inventory);
                            KeyMapping.click(Minecraft.getInstance().options.keyAttack.getKey());
                            TIME_SINCE_LAST_ATTACK = 0;
                        }

                    }

                    if((SPAM_ATTACK && KeyBindings.ATTACK_THOUGH_WALLS.isDown()) || (!SPAM_ATTACK && KeyBindings.ATTACK_THOUGH_WALLS.consumeClick())){
                        if(!SPAM_ATTACK || TIME_SINCE_LAST_ATTACK > ATTACK_DELAY) {
                            Inventory inventory = player.getInventory();
                            inventory.selected = Util.getBestSwordIndex(inventory);
                            MultiPlayerGameMode gameMode = Minecraft.getInstance().gameMode;
                            if (gameMode != null) {
                                Entity target = getAttack(player.level(), player);
                                if (target != null) {
                                    gameMode.attack(player, target);
                                }
                            }
                            TIME_SINCE_LAST_ATTACK = 0;
                        }
                    }
                }
            } else if (hitResult instanceof BlockHitResult blockHitResult){
                KeyMapping.set(Minecraft.getInstance().options.keyAttack.getKey(), false);
                if(KeyBindings.ATTACK.isDown()){
                    Inventory inventory = player.getInventory();
                    inventory.selected = Util.getBestToolIndex(inventory, level.getBlockState(blockHitResult.getBlockPos()));
                    KeyMapping.set(Minecraft.getInstance().options.keyAttack.getKey(), true);
                }
                while(KeyBindings.ATTACK.consumeClick()){
                    KeyMapping.click(Minecraft.getInstance().options.keyAttack.getKey());
                }
            }

            if(KeyBindings.LOOK_AT_ENEMY_PLAYER.consumeClick()){
                Player closest = getClosest(level, player.position(), Player.class, 128.0f, p -> {
                    if(p.isInvulnerable())return false;
                    Team team = p.getTeam();
                    return team == null || player.getTeam() == null || player.getTeam().equals(team);
                });
                if(closest != null){
                    player.lookAt(EntityAnchorArgument.Anchor.EYES, closest.position());
                }
            }

            if(KeyBindings.BRIDGE.isDown()){
                MultiPlayerGameMode gameMode = Minecraft.getInstance().gameMode;
                if (gameMode != null && level.getBlockState(player.blockPosition().below()).getShape(level, player.blockPosition().below()).isEmpty()) {
                    gameMode.useItemOn(player, player.getUsedItemHand(), new BlockHitResult(player.position().subtract(0, 1, 0), Direction.UP, player.blockPosition().subtract(new Vec3i(0, 1, 0)), false));
                    player.swing(player.getUsedItemHand());
                    if (!player.getMainHandItem().isEmpty() && (gameMode.hasInfiniteItems())) {
                        Minecraft.getInstance().gameRenderer.itemInHandRenderer.itemUsed(player.getUsedItemHand());
                    }
                }
            }

        } catch (Throwable t){
            LOGGER.error("Error during Castriil Client Tick", t);
        }

    }

    public static Entity getAttack(Level level, Player player){
        Entity entity = null;

        List<Entity> entities = level.getEntities(player, Util.around(player.position(), 6.0f), e -> {
            if(e instanceof Player target){
                if(player.getTeam() != null){
                    if(target.getTeam() != null){
                        return !target.getTeam().equals(player.getTeam());
                    }
                }
                return true;
            }
            return false;
        });

        float dis = Float.MAX_VALUE;
        for(Entity e : entities){
            float d = (float) e.distanceToSqr(player);
            if(d < dis){
                dis = d;
                entity = e;
            }
        }

        return entity;
    }

    public static <T extends Entity> T getClosest(Level level, Vec3 pos, Class<T> clazz, float maxRange, Predicate<T> test){
        T entity = null;

        List<T> entities = level.getEntitiesOfClass(clazz, Util.around(pos, maxRange), test);

        float dis = Float.MAX_VALUE;
        for(T e : entities){
            float d = (float) e.distanceToSqr(pos);
            if(d < dis){
                dis = d;
                entity = e;
            }
        }

        return entity;
    }

    public static void toggleAttackDelay(){
        if(ATTACK_DELAY++ >= 10)ATTACK_DELAY = 1;
    }

}
