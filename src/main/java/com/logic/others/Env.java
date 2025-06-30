package com.logic.others;

import java.util.*;

public class Env<K, T> {

    private final Map<K, T> table;
    private Env<K, T> prev;
    private List<Env<K, T>> children;

    public Env() {
        table = new LinkedHashMap<>();
        children = new ArrayList<>();
    }

    Env(Env<K, T> prev) {
        this();
        this.prev = prev;
    }

    public Env<K, T> root() {
        if (prev == null)
            return this;
        return prev.root();
    }

    public void bind(K id, T val) {
        table.put(id, val);
    }
    public void unbind(K id) {
        table.remove(id);
    }

    public Map<K, T> mapParent() {
        return mapParent(new LinkedHashMap<>());
    }

    private Map<K, T> mapParent(Map<K, T> map) {
        if (prev != null)
            map = prev.mapParent(map);
        map.putAll(table); // Merge current table values
        return map;
    }

    public Map<K, T> mapChild() {
        Map<K, T> map = new LinkedHashMap<>();
        mapChild(map);
        return map;
    }

    private void mapChild(Map<K, T> map) {
        map.putAll(table);

        for (Env<K, T> child : children)
            child.mapChild(map);
    }

    public T findParent(K id) {
        T value = table.get(id);

        if (value != null)
            return value;
        else if (prev != null)
            return prev.findParent(id);
        return null;
    }

    public T findChild(K id) {
        T value = table.get(id);

        if (value != null)
            return value;

        for (Env<K, T> child : children) {
            if (value == null)
                value = child.findChild(id);
        }

        return value;
    }

    public void removeAllChildren(K id) {
        table.remove(id);
        for (Env<K, T> child : children)
            child.removeAllChildren(id);
    }

    public Set<K> getMatchingParent(T value) {
        Set<K> set = new HashSet<>();
        getMatchingParent(value, set);
        return set;
    }

    private void getMatchingParent(T value, Set<K> list) {
        list.addAll(table.entrySet().stream().filter(k -> value.equals(k.getValue()))
                .map(Map.Entry::getKey).toList());
        if (prev != null)
            prev.getMatchingParent(value, list);
    }

    public Set<K> getMatchingChild(T value) {
        Set<K> set = new HashSet<>();
        getMatchingChild(value, set);
        return set;
    }

    private void getMatchingChild(T value, Set<K> list) {
        list.addAll(table.entrySet().stream().filter(k -> value.equals(k.getValue()))
                .map(Map.Entry::getKey).toList());

        for (Env<K, T> child : children)
            child.getMatchingChild(value, list);
    }

    public Env<K, T> beginScope() {
        Env<K, T> env = new Env<>(this);
        children.add(env);
        return env;
    }

    public Env<K, T> endScope() {
        return prev;
    }


}
