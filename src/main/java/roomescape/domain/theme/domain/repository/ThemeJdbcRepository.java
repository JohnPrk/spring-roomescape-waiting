package roomescape.domain.theme.domain.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.domain.Theme;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class ThemeJdbcRepository implements ThemeRepository {

    private static final String FIND_BY_ID_SQL = """
            SELECT * FROM theme WHERE id = ?;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT * FROM theme;
            """;
    private static final String SAVE_SQL = """
            INSERT INTO theme (name, description, thumbnail) 
            VALUES (?, ?, ?);
            """;
    private static final String DELETE_SQL = """
            DELETE FROM theme WHERE id = ?;
            """;
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String THUMBNAIL = "thumbnail";

    private final JdbcTemplate jdbcTemplate;

    public ThemeJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, new String[]{ID});
            preparedStatement.setString(1, theme.getName());
            preparedStatement.setString(2, theme.getDescription());
            preparedStatement.setString(3, theme.getThumbnail());
            return preparedStatement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Theme findById(Long id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, (rs, rowNum) ->
                        new Theme(
                                rs.getLong(ID),
                                rs.getString(NAME),
                                rs.getString(DESCRIPTION),
                                rs.getString(THUMBNAIL)),
                id);
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, (rs, rowNum) ->
                new Theme(
                        rs.getLong(ID),
                        rs.getString(NAME),
                        rs.getString(DESCRIPTION),
                        rs.getString(THUMBNAIL)));
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }
}
