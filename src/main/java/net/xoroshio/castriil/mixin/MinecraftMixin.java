package net.xoroshio.castriil.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.xoroshio.castriil.Castriil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(at = @At("RETURN"), method = "shouldEntityAppearGlowing", cancellable = true)
    public void onShouldEntityAppearGlowing(Entity entity, CallbackInfoReturnable<Boolean> callback) {
        Player player = Minecraft.getInstance().player;
        if(player == null){
            callback.setReturnValue(false);
        } else {
            callback.setReturnValue(Castriil.shouldGlowFor(player, entity));
        }
    }

    @Inject(at = @At("RETURN"), method = "<init>*")
    public void onConstructed(CallbackInfo callback) {
        Castriil.read(Castriil.getSaveFile());
    }

}
