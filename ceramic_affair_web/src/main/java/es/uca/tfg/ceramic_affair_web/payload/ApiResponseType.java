package es.uca.tfg.ceramic_affair_web.payload;

/**
 * Clase que representa una respuesta genérica de la API.
 * @param <T> Tipo de dato que contendrá la respuesta.
 * 
 * @version 1.0
 */
public class ApiResponseType<T> {

    private boolean success;
    private String message;
    private T data;

    public ApiResponseType(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
