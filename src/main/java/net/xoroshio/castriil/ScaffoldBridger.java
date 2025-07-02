package net.xoroshio.castriil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ScaffoldBridger {

    private final PlayerController controller;
    private Player player;

    private boolean started;
    private boolean placing;
    private Direction direction;
    private BlockPos pos;

    private boolean stopRequested;

    public ScaffoldBridger(PlayerController controller) {
        this.controller = controller;
        this.player = controller.getPlayer();
    }

    public void start(Direction direction){
        this.player = controller.getPlayer();
        started = true;
        this.direction = direction;
        controller.runForward();
        controller.startJumping();
        pos = player.blockPosition().below();
    }

    public void tick(){
        if(!valid()){
            stop();
            return;
        }
        controller.startJumping();
        if(placing) {
            controller.moveBackward();
            pos = pos.relative(direction);
            float xo = player.getXRot();
            float yo = player.getYRot();
            controller.placeSolidBlock(pos);
            player.xRotO = xo;
            player.yRotO = yo;
            player.setYRot(Util.getYRot(direction.getOpposite()));
            if(pos.getX() == player.blockPosition().getX() && pos.getZ() == player.blockPosition().getZ()){
                placing = false;
            }
        } else {
            placing = shouldStartPlacing();
            float xo = player.getXRot();
            float yo = player.getYRot();
            controller.lookAt(direction);
            player.xRotO = xo;
            player.yRotO = yo;
            controller.runForward();
        }
    }

    public boolean shouldStartPlacing(){
        return player.position().y - pos.getY() < 2.5f && player.getDeltaMovement().y < 0 && airBelow(player.blockPosition(), 2, player.level());
    }

    public boolean valid(){
        if(player.position().y < pos.getY() + 0.99f)return false;
        BlockPos b = player.blockPosition().subtract(pos);
        if(b.getX() == 0 && b.getZ() == 0)return true;
        return sameDir(b.getX(), direction.getStepX()) && sameDir(b.getZ(), direction.getStepZ());
    }

    private boolean sameDir(float a, float b){
        if(a < 0 && b < 0)return true;
        if(a > 0 && b > 0)return true;
        return a == 0 && b == 0;
    }

    private boolean airBelow(BlockPos pos, int depth, Level level){
        for (int i = 0; i < depth; i++) {
            pos = pos.below();
            if(!level.getBlockState(pos).isAir())return false;
        }
        return true;
    }

    private void stop(){
        started = false;
        placing = false;
        stopRequested = false;
        controller.stopMovement();
        controller.stopJumping();
        direction = null;
    }

    public void requestStop(){
        stop();
    }

    public boolean isStarted() {
        return started;
    }

    public PlayerController getController() {
        return controller;
    }
}
