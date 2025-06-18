package board.backend.common.web.response;

import board.backend.common.exception.ApplicationException;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    HttpStatus status,
    String message,
    T data
) {

    public static <T> ApiResponse<T> of(ApplicationException e) {
        return new ApiResponse<>(HttpStatus.valueOf(e.getStatus()), e.getMessage(), null);
    }

    static <T> ApiResponse<T> of(ApplicationException e, T data) {
        return new ApiResponse<>(HttpStatus.valueOf(e.getStatus()), e.getMessage(), data);
    }

    static <T> ApiResponse<T> of(HttpStatus status, T data) {
        return switch (status) {
            case HttpStatus.OK, HttpStatus.CREATED -> ApiResponse.of(status, "성공", data);
            default -> ApiResponse.of(status, "실패", data);
        };
    }

    static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

}
