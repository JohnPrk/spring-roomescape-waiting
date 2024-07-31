package roomescape.domain.waiting.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomescape.domain.waiting.domain.Waiting;
import roomescape.domain.waiting.domain.WaitingRank;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    Waiting save(Waiting waiting);

    @Query("SELECT new roomescape.domain.waiting.domain.WaitingRank(" +
            "    w, " +
            "    (SELECT COUNT(w2) + 1 " +
            "     FROM Waiting w2 " +
            "     WHERE w2.theme.id = w.theme.id " +
            "       AND w2.date = w.date " +
            "       AND w2.time.id = w.time.id " +
            "       AND w2.id < w.id)) " +
            "FROM Waiting w " +
            "WHERE w.member.id = :memberId")
    List<WaitingRank> findWaitingRankByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM Waiting w " +
            "WHERE w.member.id = :memberId AND w.time.id = :timeId AND w.theme.id = :themeId AND w.date = :date")
    boolean existsByMemberIdAndTimeIdAndThemeIdAndDate(
            @Param("memberId") Long memberId,
            @Param("timeId") Long timeId,
            @Param("themeId") Long themeId,
            @Param("date") String date
    );
}
