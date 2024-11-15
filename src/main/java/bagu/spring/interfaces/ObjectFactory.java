package bagu.spring.interfaces;

@FunctionalInterface
public interface ObjectFactory<T> {
    T getObject() throws RuntimeException;
}