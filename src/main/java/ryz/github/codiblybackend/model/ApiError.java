package ryz.github.codiblybackend.model;

import java.time.LocalDateTime;

public record ApiError(
        String message,
        int statusCode,
        String error,
        LocalDateTime timestamp
) {
    public ApiError(String message, int statusCode, String error) {
        this(message, statusCode, error, LocalDateTime.now());
    }
}