package net.xoroshio.castriil;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;


public class ChatLogger {

    public static final Color ERROR = new Color(200, 0, 0, 255);
    public static final Color OK = new Color(0, 200, 0, 255);
    public static final Color INFO = new Color(0, 200, 200, 255);
    public static final Color DEBUG = new Color(255, 255, 255, 255);

    public static void send(Component text){
        Minecraft.getInstance().player.sendSystemMessage(text);
    }

    public static void sendLiteralStringWithColor(String text, Color color){
        send(Component.literal(text).withColor(FastColor.ARGB32.color(color.alpha, color.reg, color.green, color.blue)));
    }

    public static void error(String text){
        sendLiteralStringWithColor(text, ERROR);
    }

    public static void ok(String text){
        sendLiteralStringWithColor(text, OK);
    }

    public static void info(String text){
        sendLiteralStringWithColor(text, INFO);
    }

    public static void debug(String text){
        sendLiteralStringWithColor(text, DEBUG);
    }

    public record Color(int reg, int green, int blue, int alpha){
    }
}
