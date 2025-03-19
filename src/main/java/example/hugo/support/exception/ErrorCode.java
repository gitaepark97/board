package example.hugo.support.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    WRONG_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다."),

    // Article
    NOT_FOUND_ARTICLE(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),

    // Comment
    NOT_FOUND_PARENT_COMMENT(HttpStatus.NOT_FOUND, "부모 댓글을 찾을 수 없습니다."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),

    // Article Like
    NOT_FOUND_ARTICLE_LIKE(HttpStatus.NOT_FOUND, "좋아요를 찾을 수 없습니다."),
    ;
    
    private final HttpStatus httpStatus;
    private final String message;

    public ApplicationException toException() {
        return new ApplicationException(this.httpStatus, this.message);
    }
}
