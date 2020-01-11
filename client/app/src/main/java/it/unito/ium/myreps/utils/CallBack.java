package it.unito.ium.myreps.utils;

@FunctionalInterface
public interface CallBack<T> {
    void doit(T value);
}
