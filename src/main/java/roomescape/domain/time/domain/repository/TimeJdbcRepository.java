package roomescape.domain.time.domain.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.time.domain.Time;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TimeJdbcRepository implements TimeRepository {

    private static final String FIND_BY_THEME_ID_AND_DATE_SQL = """
            SELECT rt.id, rt.start_at, rt.status
            FROM reservation_time rt
            LEFT JOIN reservation r ON rt.id = r.time_id AND r.date = ? AND r.theme_id = ?
            """;
    private static final String SAVE_SQL = "INSERT INTO reservation_time (start_at, status) VALUES (?, ?);";
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
            preparedStatement.setBoolean(2, time.isReserved());
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
    public List<Time> findByThemeIdAndDate(String themeId, String date) {
        return jdbcTemplate.query(FIND_BY_THEME_ID_AND_DATE_SQL, timeRowMapper(), date, themeId);
    }

    private RowMapper<Time> timeRowMapper() {
        return (rs, rowNum) ->
                new Time(
                        rs.getLong(ID),
                        rs.getString(START_AT),
                        rs.getBoolean(STATUS)
                );
    }
}
