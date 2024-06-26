package roomescape.domain.time.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.domain.time.domain.Time;

import java.util.List;

public interface TimeJpaRepository extends JpaRepository<Time, Long>, TimeRepositoryCustom {
    Time save(Time time);

    List<Time> findAll();

    void delete(Time time);

    List<Time> findByThemeIdAndDate(String themeId, String date);
}
