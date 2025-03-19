package net.xoroshio.castriil;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Util {

    public static AABB around(Vec3 pos, float radius){
        return new AABB(pos.x - radius, pos.y - radius, pos.z - radius, pos.x + radius, pos.y + radius, pos.z + radius);
    }

    public static int getBestSwordIndex(Inventory inventory){
        int j = inventory.selected;
        float d = 0.0f;
        for(int i = 0; i < 9; i++){
            ItemStack stack = inventory.getItem(i);
            float s = getDamage(stack);
            if(s > d){
                d = s;
                j = i;
            }
        }
        return j;
    }

    public static int getBestToolIndex(Inventory inventory, BlockState state){
        int j = inventory.selected;
        float d = 1.0f;
        for(int i = 0; i < 9; i++){
            ItemStack stack = inventory.getItem(i);
            float s = stack.getDestroySpeed(state);
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
}
