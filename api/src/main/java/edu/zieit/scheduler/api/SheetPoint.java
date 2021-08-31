package edu.zieit.scheduler.api;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class SheetPoint {

    private final int col;
    private final int row;

    public SheetPoint(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public static class Serializer implements TypeSerializer<SheetPoint> {

        @Override
        public SheetPoint deserialize(TypeToken<?> type, ConfigurationNode node) {
            int col = node.getNode("col").getInt();
            int row = node.getNode("row").getInt();
            return new SheetPoint(col, row);
        }

        @Override
        public void serialize(TypeToken<?> type, SheetPoint point, ConfigurationNode node) {
            if (point != null) {
                node.getNode("col").setValue(point.getCol());
                node.getNode("row").setValue(point.getRow());
            }
        }

    }

}