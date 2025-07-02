package net.xoroshio.castriil.gui;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.xoroshio.castriil.Castriil;
import org.joml.Math;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.function.Supplier;

public class CastriilScreen extends Screen {

    private static final Style RED = Style.EMPTY.withColor(FastColor.ARGB32.color(245, 245, 0, 0));
    private static final Style GREEN = Style.EMPTY.withColor(FastColor.ARGB32.color(245, 0, 245, 0));

    private static final NumberFormat FORMAT = new DecimalFormat("##.#");

    public CastriilScreen() {
        super(Component.translatable("screen.castriil.main"));
    }

    @Override
    protected void init() {

        GridLayout gridlayout = new GridLayout();
        gridlayout.defaultCellSetting().paddingHorizontal(5).paddingBottom(4).alignHorizontallyCenter();
        GridLayout.RowHelper rowHelper = gridlayout.createRowHelper(2);

        rowHelper.addChild(optionButton(this::getCancelVelocityMessage, () -> Castriil.CANCEL_SERVER_VELOCITY = !Castriil.CANCEL_SERVER_VELOCITY));
        rowHelper.addChild(new AbstractSliderButton(0, 0, 150, 20, getGlowRangeMessage(), Castriil.GLOW_RANGE / 500f) {
            @Override
            protected void updateMessage() {
                this.setMessage(getGlowRangeMessage());
            }

            @Override
            protected void applyValue() {
                Castriil.GLOW_RANGE = (int)Math.lerp(0, 500, value);
            }
        });
        rowHelper.addChild(new AbstractSliderButton(0, 0, 150, 20, getKillAuraRadiusMessage(), Castriil.KILL_AURA_RADIUS / 50.0f) {
            @Override
            protected void updateMessage() {
                this.setMessage(getKillAuraRadiusMessage());
            }

            @Override
            protected void applyValue() {
                Castriil.KILL_AURA_RADIUS = (int)Math.lerp(0, 50.0f, value);
            }
        });

        gridlayout.arrangeElements();
        FrameLayout.alignInRectangle(gridlayout, 0, this.height / 6 - 12, this.width, this.height, 0.5F, 0.0F);
        gridlayout.visitWidgets(this::addRenderableWidget);
    }

    private static Button optionButton(Supplier<Component> name, Runnable onPress){
        return new Button.Builder(name.get(), button -> {
            onPress.run();
            button.setMessage(name.get());
        })
                .width(150)
                .build();
    }

    private Component getCancelVelocityMessage(){
        return Component.translatable("option.castriil.velocity_cancel").append(onOrOf(Castriil.CANCEL_SERVER_VELOCITY));
    }

    private Component getGlowRangeMessage(){
        return Component.translatable("option.castriil.glow_range").append(Component.literal(Integer.toString(Castriil.GLOW_RANGE)));
    }

    private Component getKillAuraRadiusMessage(){
        return Component.translatable("option.castriil.kill_aura_radius").append(FORMAT.format(Castriil.KILL_AURA_RADIUS));
    }

    private Component onOrOf(boolean p){
        return p ? Component.translatable("text.castriil.on").withStyle(GREEN) : Component.translatable("text.castriil.off").withStyle(RED);
    }

    private Component number(int n){
        return Component.translatable("text.castriil.number." + n);
    }
}
