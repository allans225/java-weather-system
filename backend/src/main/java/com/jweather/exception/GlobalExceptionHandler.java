package com.jweather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Trata erros de chamadas externas (como cidade não encontrada na WeatherAPI)
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleCityNotFound(HttpClientErrorException ex) {
        // Verificação de Chave/Permissão (Prioridade alta)
        if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED || ex.getStatusCode() == HttpStatus.FORBIDDEN) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(new ErrorResponse(
                            "Chave da API inválida ou limite de requisições excedido.",
                            ex.getStatusCode().value())
                    );
        }

        // Verificação de Cidade/Input (Erro do Usuário)
        // Se a WeatherAPI retornar 400 (Bad Request), nós traduzimos para 404 (Not Found)
        if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Cidade não encontrada ou nome inválido.", 404));
        }

        // Fallback para outros erros 4xx (Genérico)
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ErrorResponse(
                        "Erro na integração: " + ex.getStatusText(), ex.getStatusCode().value())
                );
    }

    // Erro de Conectividade (Timeout, DNS, etc)
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> handleNetworkError(ResourceAccessException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse(
                        "Não foi possível conectar ao serviço de clima. Verifique a conexão.", 503)
                );
    }

    // Erro de Limite de Requisições (Rate Limit da API externa)
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleExternalServerError(HttpServerErrorException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorResponse("O serviço de clima encontrou um problema interno.", 502));
    }

    // Erro de Tipo de Argumento (Segurança extra)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("O formato do parâmetro enviado é inválido.", 400));
    }

    // Trata erros genéricos
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno no processamento dos dados.", 500));
    }
}
