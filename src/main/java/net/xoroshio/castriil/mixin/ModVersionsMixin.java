package net.xoroshio.castriil.mixin;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.network.packets.ModVersions;
import net.xoroshio.castriil.Castriil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
@Mixin(ModVersions.class)
public class ModVersionsMixin {


    /**
     * @author Xoroshio
     * @reason Using @Overwrite because original method unlikely to change, and very simple impl, Injecting would do basically the same thing
     */
    @Overwrite(remap = false)
    public static ModVersions create(){
        return new ModVersions(ModList.get().getMods().stream().filter(mod -> !mod.getModId().equals(Castriil.MOD_ID)).collect(Collectors.toMap(
                IModInfo::getModId,
                mod -> new ModVersions.Info(mod.getDisplayName(), mod.getVersion().toString())
        )));
    }
}
