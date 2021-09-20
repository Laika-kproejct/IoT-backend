package com.laika.IoT.core.security;

public interface AuthToken<T> {
    boolean validate();
    T getData();
}