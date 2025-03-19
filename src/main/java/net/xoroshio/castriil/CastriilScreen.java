package net.xoroshio.castriil;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;

public class CastriilScreen extends Screen {

    private static final Style RED = Style.EMPTY.withColor(FastColor.ARGB32.color(255, 255, 0, 0));
    private static final Style GREEN = Style.EMPTY.withColor(FastColor.ARGB32.color(255, 0, 255, 0));

    public CastriilScreen() {
        super(Component.translatable("screen.castriil.main"));
    }

    @Override
    protected void init() {

        GridLayout gridlayout = new GridLayout();
        gridlayout.defaultCellSetting().paddingHorizontal(5).paddingBottom(4).alignHorizontallyCenter();
        GridLayout.RowHelper rowHelper = gridlayout.createRowHelper(2);

        rowHelper.addChild(new Button.Builder(getCancelVelocityMessage(), button -> {
            Castriil.CANCEL_SERVER_VELOCITY = !Castriil.CANCEL_SERVER_VELOCITY;
            button.setMessage(getCancelVelocityMessage());
        })
                .width(150)
                .build());

        rowHelper.addChild(new Button.Builder(getSpamAttackMessage(), button -> {
            Castriil.SPAM_ATTACK = !Castriil.SPAM_ATTACK;
            button.setMessage(getSpamAttackMessage());
        })
                .width(150)
                .build());

        rowHelper.addChild(new Button.Builder(getAttackDelayMessage(), button -> {
            Castriil.toggleAttackDelay();
            button.setMessage(getAttackDelayMessage());
        })
                .width(150)
                .build());


        gridlayout.arrangeElements();
        FrameLayout.alignInRectangle(gridlayout, 0, this.height / 6 - 12, this.width, this.height, 0.5F, 0.0F);
        gridlayout.visitWidgets(this::addRenderableWidget);
    }

    private Component getCancelVelocityMessage(){
        return Component.translatable("screen.button.castriil.velocity_cancel").append(onOrOf(Castriil.CANCEL_SERVER_VELOCITY));
    }

    private Component getSpamAttackMessage(){
        return Component.translatable("screen.button.castriil.spam_attack").append(onOrOf(Castriil.SPAM_ATTACK));
    }

    private Component getAttackDelayMessage(){
        return Component.translatable("screen.button.castriil.attack_delay").append(number(Castriil.ATTACK_DELAY));
    }

    private Component onOrOf(boolean p){
        return p ? Component.translatable("text.castriil.on").withStyle(GREEN) : Component.translatable("text.castriil.off").withStyle(RED);
    }

    private Component number(int n){
        return Component.translatable("text.castriil.number." + n);
    }
}
