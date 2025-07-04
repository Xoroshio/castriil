package net.xoroshio.castriil.mixin;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Overwrite
    public static float getNightVisionScale(LivingEntity entity, float nanoTime){
        return 1.0f;
    }
}
