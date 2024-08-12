package roomescape.domain.time.domain.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.time.domain.Time;
import roomescape.domain.time.service.dto.TimeWithStatus;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TimeJdbcRepository implements TimeRepository {

    private static final String FIND_BY_THEME_ID_AND_DATE_WITH_SINGLE_QUERY_SQL = """
            SELECT 
                t.id, t.start_at, CASE WHEN r.time_id IS NOT NULL THEN 'true' ELSE 'false' END AS status 
            FROM time t
            LEFT JOIN reservation r 
                ON t.id = r.time_id AND r.date = ? AND r.theme_id = ?;
            """;

    private static final String FIND_BY_THEME_ID_AND_DATE_WITH_MULTIPLE_QUERY_SQL = """
            SELECT 
                r.id AS id,
            FROM reservation r
            WHERE r.date = ? AND r.theme_id = ?;
            """;
    private static final String SAVE_SQL = "INSERT INTO reservation_time (start_at) VALUES (?);";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM reservation_time WHERE id = ?;";
    private static final String FIND_ALL_SQL = "SELECT * FROM reservation_time";
    private static final String DELETE_SQL = "DELETE FROM reservation_time WHERE id = ?;";
    private static final String ID = "id";
    private static final String START_AT = "start_at";
    private static final String STATUS = "status";

    private final JdbcTemplate jdbcTemplate;

    public TimeJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Time time) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, new String[]{ID});
            preparedStatement.setString(1, time.getStartAt());
            return preparedStatement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Optional<Time> findById(Long timeId) {
        return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID_SQL, timeRowMapper(), timeId));
    }

    @Override
    public List<Time> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, timeRowMapper());
    }

    @Override
    public void delete(Time time) {
        jdbcTemplate.update(DELETE_SQL, time.getId());
    }

    @Override
    public List<TimeWithStatus> findByThemeIdAndDateWithSingleQuery(String themeId, String date) {
        return jdbcTemplate.query(FIND_BY_THEME_ID_AND_DATE_WITH_SINGLE_QUERY_SQL, timeWithStatusRowMapper(), date, themeId);
    }

    @Override
    public List<Long> findByThemeIdAndDateWithMultipleQuery(String themeId, String date) {
        return jdbcTemplate.query(FIND_BY_THEME_ID_AND_DATE_WITH_MULTIPLE_QUERY_SQL, (rs, rowNum) -> rs.getLong(ID), date, themeId);
    }

    private RowMapper<TimeWithStatus> timeWithStatusRowMapper() {
        return (rs, rowNum) ->
                new TimeWithStatus(
                        rs.getLong(ID),
                        rs.getString(START_AT),
                        rs.getString(STATUS)
                );
    }

    private RowMapper<Time> timeRowMapper() {
        return (rs, rowNum) ->
                new Time(
                        rs.getLong(ID),
                        rs.getString(START_AT)
                );
    }
}
