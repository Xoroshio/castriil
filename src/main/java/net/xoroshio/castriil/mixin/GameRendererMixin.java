package net.xoroshio.castriil.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.xoroshio.castriil.IGameRendererMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements IGameRendererMixin {
    @Override
    public float getFov(Camera camera, float partialTicks) {
        return (float) getFov(camera, partialTicks, true);
    }

    @Shadow
    private double getFov(Camera camera, float partialTicks, boolean useFovSettings){
        throw new IllegalStateException("Mixin failed!");
    }
}
