package roomescape.domain.waiting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.domain.Member;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.time.domain.Time;
import roomescape.domain.waiting.domain.Waiting;
import roomescape.domain.waiting.domain.WaitingRank;
import roomescape.domain.waiting.domain.repository.WaitingRepository;
import roomescape.domain.waiting.error.exception.WaitingErrorCode;
import roomescape.domain.waiting.error.exception.WaitingException;
import roomescape.domain.waiting.service.dto.WaitingResponse;

import java.util.List;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;

    public WaitingService(WaitingRepository waitingRepository) {
        this.waitingRepository = waitingRepository;
    }

    @Transactional
    public WaitingResponse save(Time time, Theme theme, Member loginMember, String date) {
        duplicateCheck(time, theme, loginMember, date);
        Waiting waiting = new Waiting(null, theme, loginMember, time, date);
        Waiting savedWaiting = waitingRepository.save(waiting);
        Waiting findWaiting = waitingRepository.findById(savedWaiting.getId()).orElseThrow(() -> new WaitingException(WaitingErrorCode.NO_WAITING_ERROR));
        return mapToWaitingResponseDto(findWaiting);
    }

    @Transactional(readOnly = true)
    public List<WaitingRank> findWaitingRankByMemberId(Long memberId) {
        return waitingRepository.findWaitingRankByMemberId(memberId);
    }

    @Transactional
    public void delete(Long memberId, Long id) {
        Waiting deleteWaiting = waitingRepository.findById(id).orElseThrow(() -> new WaitingException(WaitingErrorCode.NO_WAITING_ERROR));
        deleteWaiting.memberAuthenticationCheck(memberId);
        waitingRepository.delete(deleteWaiting);
    }

    @Transactional
    public boolean existsByMemberIdAndTimeIdAndThemeIdAndDate(Time time, Theme theme, Member loginMember, String date) {
        return waitingRepository.existsByMemberIdAndTimeIdAndThemeIdAndDate(loginMember.getId(), time.getId(), theme.getId(), date);
    }

    private void duplicateCheck(Time time, Theme theme, Member loginMember, String date) {
        if(existsByMemberIdAndTimeIdAndThemeIdAndDate(time, theme, loginMember, date)) {
            throw new WaitingException(WaitingErrorCode.DUPLICATION_ERROR);
        }
    }

    private WaitingResponse mapToWaitingResponseDto(Waiting waiting) {
        return new WaitingResponse(
                waiting.getId(),
                waiting.getTheme(),
                waiting.getMember(),
                waiting.getTime(),
                waiting.getDate());
    }
}
