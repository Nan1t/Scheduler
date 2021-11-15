package edu.zieit.scheduler.api;

public record Pair<K, V>(K key, V value) {

    public static <K, V> Pair<K, V> of(K key, V val) {
        return new Pair<>(key, val);
    }

}
