package forms;

import java.util.Objects;

public class SelectItem<K, V> {

    private final K key;
    private final V value;

    public SelectItem(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value == null ? "" : value.toString();
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.key);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if(key != null && key.getClass() == obj.getClass()) {
            return key.equals(obj);
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SelectItem<?, ?> other = (SelectItem<?, ?>) obj;
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        return true;
    }

}
