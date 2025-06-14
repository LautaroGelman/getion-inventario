// src/main/java/grupo5/gestion_inventario/exception/ApiExceptionHandler.java
package grupo5.gestion_inventario.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleMalformedJson(HttpMessageNotReadableException ex) {
        String msg = ex.getMostSpecificCause().getMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("JSON inválido o campo mal formado: " + msg);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        // Para cualquier otra excepción, devolvemos el mensaje
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno: " + ex.getMessage());
    }
}
