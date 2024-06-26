package roomescape.domain.theme.domain.repository;

import roomescape.domain.theme.domain.Theme;

import java.util.List;

public interface ThemeRepository {
    Long save(Theme theme);

    Theme findById(Long id);

    List<Theme> findAll();

    void delete(Long id);
}
