package edu.zieit.scheduler.config;

import io.leangen.geantyref.TypeToken;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.*;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class Configuration implements ConfigurationNode {

    private final BufferedReader reader;
    private ConfigurationNode root;

    /**
     * Create configuration from file in file system
     * @param file Path to configuration file
     * @throws FileNotFoundException if loading fails
     */
    public Configuration(Path file) throws FileNotFoundException {
        reader = createReader(file);
    }

    /**
     * Create configuration from bundled resource
     * @param resPath Path to resource inside jar file
     * @param app Application instance. It can be main class of something else
     * @throws IOException if loading fails
     */
    public Configuration(String resPath, Object app) throws IOException {
        Path file = Paths.get("./", resPath);

        if (!Files.exists(file)) {
            InputStream input = app.getClass().getResourceAsStream(resPath);

            if (input == null) {
                throw new FileNotFoundException(String.format("Resource file '%s' not found", resPath));
            }

            Files.copy(input, file);
        }

        reader = createReader(file);
    }

    private BufferedReader createReader(Path file) throws FileNotFoundException {
        return new BufferedReader(new FileReader(file.toFile()));
    }

    /**
     * Load configuration from file
     */
    public void load() throws ConfigurateException {
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .source(()->reader)
                .build();

        root = loader.load();
    }

    /* Delegated methods */

    @Override
    @Nullable
    public Object key() {
        return root.key();
    }

    @Override
    public NodePath path() {
        return root.path();
    }

    @Override
    @Nullable
    public ConfigurationNode parent() {
        return root.parent();
    }

    @Override
    public ConfigurationNode node(Object... path) {
        return root.node(path);
    }

    @Override
    public ConfigurationNode node(Iterable<?> path) {
        return root.node(path);
    }

    @Override
    public boolean hasChild(Object... path) {
        return root.hasChild(path);
    }

    @Override
    public boolean hasChild(Iterable<?> path) {
        return root.hasChild(path);
    }

    @Override
    public boolean virtual() {
        return root.virtual();
    }

    @Override
    public ConfigurationOptions options() {
        return root.options();
    }

    @Override
    public boolean isNull() {
        return root.isNull();
    }

    @Override
    public boolean isList() {
        return root.isList();
    }

    @Override
    public boolean isMap() {
        return root.isMap();
    }

    @Override
    public boolean empty() {
        return root.empty();
    }

    @Override
    public List<? extends ConfigurationNode> childrenList() {
        return root.childrenList();
    }

    @Override
    public Map<Object, ? extends ConfigurationNode> childrenMap() {
        return root.childrenMap();
    }

    @Override
    public <V> Collector<Map.Entry<?, V>, ? extends ConfigurationNode, ? extends ConfigurationNode> toMapCollector(TypeToken<V> valueType) {
        return root.toMapCollector(valueType);
    }

    @Override
    public <V> Collector<Map.Entry<?, V>, ? extends ConfigurationNode, ? extends ConfigurationNode> toMapCollector(Class<V> valueType) {
        return root.toMapCollector(valueType);
    }

    @Override
    public <V> Collector<V, ? extends ConfigurationNode, ? extends ConfigurationNode> toListCollector(TypeToken<V> valueType) {
        return root.toListCollector(valueType);
    }

    @Override
    public <V> Collector<V, ? extends ConfigurationNode, ? extends ConfigurationNode> toListCollector(Class<V> valueType) {
        return root.toListCollector(valueType);
    }

    @Override
    public <V> V require(TypeToken<V> type) throws SerializationException {
        return root.require(type);
    }

    @Override
    public <V> V require(Class<V> type) throws SerializationException {
        return root.require(type);
    }

    @Override
    @Nullable
    public Object require(Type type) throws SerializationException {
        return root.require(type);
    }

    @Override
    public <V> @Nullable V get(TypeToken<V> type) throws SerializationException {
        return root.get(type);
    }

    @Override
    public <V> V get(TypeToken<V> type, V def) throws SerializationException {
        return root.get(type, def);
    }

    @Override
    public <V> V get(TypeToken<V> type, Supplier<V> defSupplier) throws SerializationException {
        return root.get(type, defSupplier);
    }

    @Override
    public <V> @Nullable V get(Class<V> type) throws SerializationException {
        return root.get(type);
    }

    @Override
    public <V> V get(Class<V> type, V def) throws SerializationException {
        return root.get(type, def);
    }

    @Override
    public <V> V get(Class<V> type, Supplier<V> defSupplier) throws SerializationException {
        return root.get(type, defSupplier);
    }

    @Override
    @Nullable
    public Object get(Type type) throws SerializationException {
        return root.get(type);
    }

    @Override
    public Object get(Type type, Object def) throws SerializationException {
        return root.get(type, def);
    }

    @Override
    public Object get(Type type, Supplier<?> defSupplier) throws SerializationException {
        return root.get(type, defSupplier);
    }

    @Override
    public @Nullable <V> List<V> getList(TypeToken<V> type) throws SerializationException {
        return root.getList(type);
    }

    @Override
    public <V> List<V> getList(TypeToken<V> elementType, List<V> def) throws SerializationException {
        return root.getList(elementType, def);
    }

    @Override
    public <V> List<V> getList(TypeToken<V> elementType, Supplier<List<V>> defSupplier) throws SerializationException {
        return root.getList(elementType, defSupplier);
    }

    @Override
    public @Nullable <V> List<V> getList(Class<V> type) throws SerializationException {
        return root.getList(type);
    }

    @Override
    public <V> List<V> getList(Class<V> elementType, List<V> def) throws SerializationException {
        return root.getList(elementType, def);
    }

    @Override
    public <V> List<V> getList(Class<V> elementType, Supplier<List<V>> defSupplier) throws SerializationException {
        return root.getList(elementType, defSupplier);
    }

    @Override
    @Nullable
    public String getString() {
        return root.getString();
    }

    @Override
    public String getString(String def) {
        return root.getString(def);
    }

    @Override
    public float getFloat() {
        return root.getFloat();
    }

    @Override
    public float getFloat(float def) {
        return root.getFloat(def);
    }

    @Override
    public double getDouble() {
        return root.getDouble();
    }

    @Override
    public double getDouble(double def) {
        return root.getDouble(def);
    }

    @Override
    public int getInt() {
        return root.getInt();
    }

    @Override
    public int getInt(int def) {
        return root.getInt(def);
    }

    @Override
    public long getLong() {
        return root.getLong();
    }

    @Override
    public long getLong(long def) {
        return root.getLong(def);
    }

    @Override
    public boolean getBoolean() {
        return root.getBoolean();
    }

    @Override
    public boolean getBoolean(boolean def) {
        return root.getBoolean(def);
    }

    @Override
    public ConfigurationNode set(@Nullable Object value) throws SerializationException {
        return root.set(value);
    }

    @Override
    public <V> ConfigurationNode set(TypeToken<V> type, @Nullable V value) throws SerializationException {
        return root.set(type, value);
    }

    @Override
    public <V> ConfigurationNode set(Class<V> type, @Nullable V value) throws SerializationException {
        return root.set(type, value);
    }

    @Override
    public ConfigurationNode set(Type type, @Nullable Object value) throws SerializationException {
        return root.set(type, value);
    }

    @Override
    public <V> ConfigurationNode setList(Class<V> elementType, @Nullable List<V> items) throws SerializationException {
        return root.setList(elementType, items);
    }

    @Override
    public <V> ConfigurationNode setList(TypeToken<V> elementType, @Nullable List<V> items) throws SerializationException {
        return root.setList(elementType, items);
    }

    @Override
    @Nullable
    public Object raw() {
        return root.raw();
    }

    @Override
    public ConfigurationNode raw(@Nullable Object value) {
        return root.raw(value);
    }

    @Override
    @Nullable
    public Object rawScalar() {
        return root.rawScalar();
    }

    @Override
    public ConfigurationNode from(ConfigurationNode other) {
        return root.from(other);
    }

    @Override
    public ConfigurationNode mergeFrom(ConfigurationNode other) {
        return root.mergeFrom(other);
    }

    @Override
    public boolean removeChild(Object key) {
        return root.removeChild(key);
    }

    @Override
    public ConfigurationNode appendListNode() {
        return root.appendListNode();
    }

    @Override
    public ConfigurationNode copy() {
        return root.copy();
    }

    @Override
    public <S, T, E extends Exception> T visit(ConfigurationVisitor<S, T, E> visitor) throws E {
        return root.visit(visitor);
    }

    @Override
    public <S, T, E extends Exception> T visit(ConfigurationVisitor<S, T, E> visitor, S state) throws E {
        return root.visit(visitor, state);
    }

    @Override
    public <S, T> T visit(ConfigurationVisitor.Safe<S, T> visitor) {
        return root.visit(visitor);
    }

    @Override
    public <S, T> T visit(ConfigurationVisitor.Safe<S, T> visitor, S state) {
        return root.visit(visitor, state);
    }

    @Override
    public <V> ConfigurationNode hint(RepresentationHint<V> hint, @Nullable V value) {
        return root.hint(hint, value);
    }

    @Override
    public <V> @Nullable V hint(RepresentationHint<V> hint) {
        return root.hint(hint);
    }

    @Override
    public <V> @Nullable V ownHint(RepresentationHint<V> hint) {
        return root.ownHint(hint);
    }

    @Override
    public Map<RepresentationHint<?>, ?> ownHints() {
        return root.ownHints();
    }

}
