package roomescape.domain.theme.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.domain.theme.domain.Theme;

public interface ThemeJpaRepository extends JpaRepository<Theme, Long> {
}
