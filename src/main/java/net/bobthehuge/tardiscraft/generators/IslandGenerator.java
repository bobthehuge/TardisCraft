package net.bobthehuge.tardiscraft.generators;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public interface IslandGenerator extends Generator {
    static <ISLAND extends IslandGenerator> ISLAND makeIslandGenerator(Class<ISLAND> islandClass) {
        try {
            Constructor<ISLAND> constructor = islandClass.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
