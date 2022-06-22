package edu.zieit.scheduler.api;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

/**
 * 0-based cell position
 */
public record SheetPoint(int col, int row) {

    public static class Serializer implements TypeSerializer<SheetPoint> {

        @Override
        public SheetPoint deserialize(Type type, ConfigurationNode node) throws SerializationException {
            int col = node.node("col").getInt();
            int row = node.node("row").getInt();
            return new SheetPoint(col, row);
        }

        @Override
        public void serialize(Type type, @Nullable SheetPoint point, ConfigurationNode node) throws SerializationException {
            if (point != null) {
                node.node("col").set(point.col());
                node.node("row").set(point.row());
            }
        }

    }

}
