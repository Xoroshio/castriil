package net.xoroshio.castriil;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class PlayerController {

    private LocalPlayer player;
    private Options options;

    public PlayerController(LocalPlayer player, Minecraft minecraft){
        this.player = player;
        this.options = minecraft.options;
    }

    public void validate(){
        this.player = Minecraft.getInstance().player;
        this.options = Minecraft.getInstance().options;
    }

    public void runForward(){
        stopMovement();
        on(options.keySprint);
        on(options.keyUp);
    }

    public void startJumping(){
        on(options.keyJump);
        player.setJumping(true);
    }

    public void stopJumping(){
        release(options.keyJump);
        player.setJumping(false);
    }

    public void moveBackward(){
        stopMovement();
        on(options.keyDown);
    }

    public void placeSolidBlock(BlockPos pos){
        BlockHitResult hit = Util.getBlockHitResult(pos, player.level());
        if(hit != null){
            Vec3 targetPos = hit.getLocation();
            lookAt(targetPos);
            player.getInventory().selected = Util.getBlockIndex(player.getInventory(), Util.SOLID_BLOCK_TEST);
            click(options.keyUse);
        }
    }


    public void lookAt(Direction direction){
        lookAt(new Vec3(direction.getStepX(), direction.getStepY(), direction.getStepZ()).add(player.getEyePosition()));
    }

    public void lookAt(Vec3 pos){
        player.lookAt(EntityAnchorArgument.Anchor.EYES, pos);
    }

    public void stopMovement(){
        release(options.keySprint);
        release(options.keyUp);
        release(options.keyLeft);
        release(options.keyRight);
        release(options.keyDown);
    }

    private void click(KeyMapping key){
        KeyMapping.click(key.getKey());
    }

    private void on(KeyMapping key){
        key.setDown(true);
    }

    private void release(KeyMapping key){
        key.setDown(false);
        while (key.consumeClick()){
        }
    }


    public LocalPlayer getPlayer() {
        return player;
    }
}
