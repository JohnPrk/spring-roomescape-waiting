package roomescape.domain.waiting.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.argumentResolver.annotation.Login;
import roomescape.domain.member.domain.Member;
import roomescape.domain.waiting.service.WaitingService;

@RestController
@RequestMapping("/waiting")
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@Login Member loginMember, @PathVariable("id") Long id) {
        waitingService.delete(loginMember.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
