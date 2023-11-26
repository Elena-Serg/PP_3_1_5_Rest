package ru.kata.spring.boot_security.demo.handler;

public record Violation(
        String fieldName,
        String message
) { }