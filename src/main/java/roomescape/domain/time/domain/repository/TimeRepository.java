package roomescape.domain.time.domain.repository;

import roomescape.domain.time.domain.Time;

import java.util.List;

public interface TimeRepository {
    Long save(Time time);

    Time findById(Long timeId);

    List<Time> findAll();

    void delete(Long timeId);

    List<Time> findByThemeIdAndDate(String themeId, String date);
}
