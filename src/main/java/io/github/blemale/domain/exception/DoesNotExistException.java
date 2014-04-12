package io.github.blemale.domain.exception;

public class DoesNotExistException extends RuntimeException {
    private DoesNotExistException(String message) {
        super(message);
    }

    public static final DoesNotExistException create(Class clazz, Object id) {
        return new DoesNotExistException(clazz.getName() + ": " + id + " does not exist.");
    }
}
