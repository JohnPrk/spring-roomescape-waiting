package roomescape.domain.waiting.error.exception;

import org.springframework.http.HttpStatus;

public enum WaitingErrorCode {
    NO_WAITING_ERROR(HttpStatus.BAD_REQUEST.value(), "웨이팅이 존재하지 않습니다.");

    private final int status;
    private final String errorMessage;

    WaitingErrorCode(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
