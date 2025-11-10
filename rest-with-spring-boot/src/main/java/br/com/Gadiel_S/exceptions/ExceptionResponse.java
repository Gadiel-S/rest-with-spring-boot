package br.com.Gadiel_S.exceptions;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) {}
