package net.xoroshio.castriil;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.*;
import java.util.Properties;

public class CastriilProperties {

    public static final Logger LOGGER = LogUtils.getLogger();

    private static final CastriilProperties DEFAULT = new CastriilProperties().fromCastriil();
    private static final Properties DEFAULT_PROPERTIES = DEFAULT.createProperties();

    private boolean cancelServerVelocity;
    private int glowRange;
    private float killAuraRadius;

    public CastriilProperties(){
    }

    public void write(File file){
        try {
            OutputStream out = new FileOutputStream(file);
            createProperties().store(out, "Castriil Properties (Found in the GUI)");
            out.close();
        } catch (Throwable t){
            LOGGER.error("Error writing properties: ", t);
        }
    }

    private Properties createProperties(){
        Properties properties = new Properties();
        properties.put("cancelServerVelocity", cancelServerVelocity + "");
        properties.put("glowRange", glowRange + "");
        properties.put("killAuraRadius", killAuraRadius + "");
        return properties;
    }

    public static CastriilProperties read(File file){
        CastriilProperties castriilProperties = new CastriilProperties();
        try {
            Properties properties = new Properties(DEFAULT_PROPERTIES);
            InputStream in = new FileInputStream(file);
            properties.load(in);
            in.close();
            castriilProperties.cancelServerVelocity = Util.getBool(properties, "cancelServerVelocity", DEFAULT.cancelServerVelocity);
            castriilProperties.glowRange = Util.getInt(properties, "glowRange", DEFAULT.glowRange);
            castriilProperties.killAuraRadius = Util.getFloat(properties, "killAuraRadius", DEFAULT.killAuraRadius);
        } catch (Throwable t){
            LOGGER.error("Error writing properties: ", t);
        }
        return castriilProperties;
    }

    public void setCastriil(){
        Castriil.CANCEL_SERVER_VELOCITY = cancelServerVelocity;
        Castriil.GLOW_RANGE = glowRange;
        Castriil.KILL_AURA_RADIUS = killAuraRadius;
    }

    public CastriilProperties fromCastriil(){
        cancelServerVelocity = Castriil.CANCEL_SERVER_VELOCITY;
        glowRange = Castriil.GLOW_RANGE;
        killAuraRadius = Castriil.KILL_AURA_RADIUS;
        return this;
    }
}
