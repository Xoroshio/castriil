package net.xoroshio.castriil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.Properties;
import java.util.function.Predicate;

public class Util {

    public static Predicate<Block> SOLID_BLOCK_TEST = block -> block.defaultBlockState().isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);

    public static AABB around(Vec3 pos, float radius){
        return new AABB(pos.x - radius, pos.y - radius, pos.z - radius, pos.x + radius, pos.y + radius, pos.z + radius);
    }

    public static int getInt(Properties properties, String name, int def){
        try {
            return Integer.parseInt(properties.getProperty(name));
        } catch (Throwable ignored){
            return def;
        }
    }

    public static float getFloat(Properties properties, String name, float def){
        try {
            return Float.parseFloat(properties.getProperty(name));
        } catch (Throwable ignored){
            return def;
        }
    }

    public static boolean getBool(Properties properties, String name, boolean def){
        try {
            return Boolean.parseBoolean(properties.getProperty(name));
        } catch (Throwable ignored){
            return def;
        }
    }

    public static int getBestSwordIndex(Inventory inventory){
        return getBestItemIndex(inventory, null, (x, stack) -> stack.getItem() instanceof SwordItem ? 1 : 0);
    }

    public static int getBestToolIndex(Inventory inventory, BlockState state){
        return getBestItemIndex(inventory, state, (block, stack) -> stack.getDestroySpeed(state));
    }

    public static int getBlockIndex(Inventory inventory, Predicate<Block> blockTest){
        return getBestItemIndex(inventory, null, (x, stack) -> {
            if(stack.getItem() instanceof BlockItem blockItem){
                if(blockTest.test(blockItem.getBlock())){
                    return stack.getCount();
                }
            }
            return 0;
        });
    }

    public static boolean isSolid(BlockPos pos, Level level){
        return level.getBlockState(pos).isCollisionShapeFullBlock(level, pos);
    }

    public static BlockHitResult getBlockHitResult(BlockPos pos, Level level){
        for(Direction direction : Direction.values()){
            BlockPos p = pos.relative(direction);
            if(isSolid(p, level)){
                Direction side = direction.getOpposite();
                Vec3 hit = p.getCenter().add((double) side.getStepX() / 2, (double) side.getStepY() / 2, (double) side.getStepZ() / 2);
                return new BlockHitResult(hit, side, p, false);
            }
        }
        return null;
    }

    public static Direction getHorizontalDirection(Player player){
        float rot = player.getYRot();
        if(rot > 45 && rot < 135)return Direction.WEST;
        if((rot > 135 && rot < 180) || (rot > -180 && rot < -135))return Direction.NORTH;
        if(rot > -135 && rot < -45)return Direction.EAST;
        return Direction.SOUTH;
    }

    public static float getYRot(Direction direction){
        if(direction == Direction.EAST)return -90.0f;
        if(direction == Direction.WEST)return 90.0f;
        if(direction == Direction.SOUTH)return 0.0f;
        if(direction == Direction.NORTH)return -179.99f;

        return 0.0f;
    }

    public static <T> int getBestItemIndex(Inventory inventory, T context, FindValueFunction<T> findValueFunction){
        int j = inventory.selected;
        float d = 1.0f;
        for(int i = 0; i < 9; i++){
            ItemStack stack = inventory.getItem(i);
            float s = findValueFunction.find(context, stack);
            if(s > d){
                d = s;
                j = i;
            }
        }
        return j;
    }

    public static float getDamage(ItemStack stack){
        ItemAttributeModifiers modifiers = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if(modifiers == null)return 0;
        float damage = 0;
        for(ItemAttributeModifiers.Entry entry : modifiers.modifiers()){
            if(entry.attribute() == Attributes.ATTACK_DAMAGE){
                damage = (float) entry.modifier().amount();
            }
        }
        return damage;
    }

    @FunctionalInterface
    public interface FindValueFunction<T> {
        float find(T context, ItemStack stack);
    }
}
