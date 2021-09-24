package edu.zieit.scheduler.api;

import com.google.common.base.Preconditions;

import java.util.Objects;

/**
 * Useful Id wrapper to identify schedule by namespace (filename) and key (sheet name)
 */
public record NamespaceKey(String namespace, String key) {

    /**
     * Get same key but only with namespace name, without key
     * @return Namespaced key instance
     */
    public NamespaceKey withoutKey() {
        return of(namespace);
    }

    /**
     * Check is namespace equals to another
     * @param another Another NamespacedKey
     * @return true is namespaces equals or false otherwise
     */
    public boolean compareNamespace(NamespaceKey another) {
        return another.namespace().equals(this.namespace());
    }

    @Override
    public String toString() {
        return key.isEmpty() ? namespace : String.format("%s:%s", namespace, key);
    }

    /**
     * Create namespaced key from namespace and key
     * @param namespace Namespace name
     * @param key Key name
     * @return New NamespaceKey instance
     */
    public static NamespaceKey of(String namespace, String key) {
        return new NamespaceKey(namespace.strip(), key.strip());
    }

    /**
     * Create namespaced key only from namespace
     * @param namespace Namespace name
     * @return New NamespaceKey instance
     */
    public static NamespaceKey of(String namespace) {
        return of(namespace, "");
    }

    /**
     * Parse key from string value like 'namespace:key'
     * @param str Raw key string
     * @return Parsed key instance
     */
    public static NamespaceKey parse(String str) {
        Preconditions.checkNotNull(str, "Raw NamespaceKey cannot be null");
        String[] arr = str.split(":");
        if (arr.length != 2) throw new IllegalArgumentException("Invalid namespaced key");
        return of(arr[0], arr[1]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof NamespaceKey key) {
            return this.namespace.equals(key.namespace)
                    && this.key.equals(key.key);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, key);
    }
}
