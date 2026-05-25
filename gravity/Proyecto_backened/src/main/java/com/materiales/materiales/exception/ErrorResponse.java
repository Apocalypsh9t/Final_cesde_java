package com.materiales.materiales.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private String mensaje;
    private String codigoError;
    private LocalDateTime timestamp;

    public ErrorResponse(String mensaje, String codigoError) {
        this.mensaje = mensaje;
        this.codigoError = codigoError;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public String getMensaje() { return mensaje; }
    public String getCodigoError() { return codigoError; }
    public LocalDateTime getTimestamp() { return timestamp; }
}