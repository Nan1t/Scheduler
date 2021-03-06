package edu.zieit.scheduler.api;

import edu.zieit.scheduler.api.util.Preconditions;

import java.io.Serializable;
import java.util.Objects;

/**
 * Useful Id wrapper to identify schedule by namespace (filename) and key (sheet name)
 */
public record NamespacedKey(String namespace, String key) implements Serializable {

    /**
     * Get same key but only with namespace name, without key
     * @return Namespaced key instance
     */
    public NamespacedKey withoutKey() {
        return of(namespace);
    }

    /**
     * Check is namespace equals to another
     * @param another Another NamespacedKey
     * @return true is namespaces equals or false otherwise
     */
    public boolean compareNamespace(NamespacedKey another) {
        return another.namespace().equals(this.namespace());
    }

    public boolean hasKey() {
        return key != null && !key.isEmpty();
    }

    public boolean isSimilar(NamespacedKey another) {
        if (!compareNamespace(another)) return false;
        if (!this.hasKey() && !another.hasKey()) return true;
        return this.hasKey() && another.hasKey() && this.key.startsWith(another.key());
    }

    @Override
    public String toString() {
        return key.isEmpty() ? namespace : String.format("%s:%s", namespace, key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof NamespacedKey key) {
            return this.namespace.equalsIgnoreCase(key.namespace)
                    && this.key.equalsIgnoreCase(key.key);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace.toLowerCase(), key.toLowerCase());
    }

    /**
     * Create namespaced key from namespace and key
     * @param namespace Namespace name
     * @param key Key name
     * @return New NamespaceKey instance
     */
    public static NamespacedKey of(String namespace, String key) {
        return new NamespacedKey(namespace.strip(), key.strip());
    }

    /**
     * Create namespaced key only from namespace
     * @param namespace Namespace name
     * @return New NamespaceKey instance
     */
    public static NamespacedKey of(String namespace) {
        return of(namespace, "");
    }

    /**
     * Parse key from string value like 'namespace:key'
     * @param str Raw key string
     * @return Parsed key instance
     */
    public static NamespacedKey parse(String str) {
        Preconditions.checkNotNull(str, "Raw NamespaceKey cannot be null");

        String[] arr = str.split(":");
        if (arr.length == 1) return of(arr[0]);
        if (arr.length != 2) throw new IllegalArgumentException("Invalid namespaced key: " + str);
        return of(arr[0], arr[1]);
    }
}
