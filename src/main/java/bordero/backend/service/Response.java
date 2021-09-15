package bordero.backend.service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

public class Response<T> {

    private T value;
    private int code;

    private Response() {
        this.code = 0;
        this.value = null;
    }

    public Response(T value) {
        this.code = 0;
        this.value = Objects.requireNonNull(value);
    }

    public Response(int code) {
        this.code = Objects.requireNonNull(code);
        this.value = null;
    }

    public Response(T value, int code) {
        this.value = value;
        this.code = code;
    }

    public static<T> Response<T> ofError(int msg) {
        return new Response<T>(msg);
    }

    public static<T> Response<T> ofValid(T value) {
        return new Response<>(value);
    }

    public static<T> Response<T> ofMayBe(T value) {
        return value == null ? ofError(0) : ofValid(value);
    }

    public T get() {
        if (value == null) { throw new NoSuchElementException("Se intenta recuperar un valor de un error");  }
        return value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isValid() {
        return value != null;
    }

    public void ifValid(Consumer<? super T> action) {
        if (value != null) {
            action.accept(value);
        }
    }
}
