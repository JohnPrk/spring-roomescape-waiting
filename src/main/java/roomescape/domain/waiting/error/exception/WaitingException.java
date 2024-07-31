package roomescape.domain.waiting.error.exception;

import roomescape.global.error.exception.UserException;

public class WaitingException extends UserException {

    public WaitingException(WaitingErrorCode waitingErrorCode) {
        super(waitingErrorCode.getStatus(), waitingErrorCode.getErrorMessage());
    }

    @Override
    public int getStatus() {
        return super.getStatus();
    }
}
