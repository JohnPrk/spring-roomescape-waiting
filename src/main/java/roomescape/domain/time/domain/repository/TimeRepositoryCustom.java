package roomescape.domain.time.domain.repository;

import roomescape.domain.time.domain.Time;

import java.util.List;

public interface TimeRepositoryCustom {

    List<Time> findByThemeIdAndDate(String themeId, String date);
}
