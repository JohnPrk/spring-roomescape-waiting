package roomescape.domain.time.domain.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import roomescape.domain.time.domain.Time;

import java.util.List;

public class TimeRepositoryCustomImpl implements TimeRepositoryCustom {

    private static final String FIND_BY_THEME_ID_AND_DATE_JPQL = """
            SELECT rt FROM Time rt
            WHERE rt.id NOT IN (
                SELECT rt.time.id
                FROM ReservationTime rt
                JOIN rt.reservation r
                JOIN r.reservationThemes rtThemes
                WHERE rtThemes.theme.id = :themeId
                AND r.date = :date
            )
            """;

    private final EntityManager entityManager;

    public TimeRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Time> findByThemeIdAndDate(String themeId, String date) {
        TypedQuery<Time> query = entityManager.createQuery(FIND_BY_THEME_ID_AND_DATE_JPQL, Time.class);
        query.setParameter("themeId", themeId);
        query.setParameter("date", date);
        return query.getResultList();
    }
}
