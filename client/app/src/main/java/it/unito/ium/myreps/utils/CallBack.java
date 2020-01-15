package it.unito.ium.myreps.utils;

@FunctionalInterface
public interface CallBack<T> {
    void action(T... values);
}
