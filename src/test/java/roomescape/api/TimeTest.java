package roomescape.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TimeTest {

    private static final String ID = "id";
    private static final String START_AT = "startAt";
    private static final String CURRENT_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private static final String REQUEST_TIME = LocalTime.now().plusMinutes(5L).format(DateTimeFormatter.ofPattern("HH:mm"));
    private static final Long ALREADY_DATA_SET_SIZE = 3L;

    @Test
    void 시간을_등록한다() {

        createTimeAndTimeId();
    }

    @Test
    void 시간을_조회한다() {

        //given
        Long id = createTimeAndTimeId();

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/times/available?date=" + CURRENT_DATE + "&themeId=" + String.valueOf(id))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 시간을_삭제한다() {

        //given
        Long id = createTimeAndTimeId();

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/times/" + String.valueOf(id))
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    protected Long createTimeAndTimeId() {

        //given
        Map<String, Object> time = new HashMap<>();
        time.put(START_AT, REQUEST_TIME);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/times")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getLong(ID);
    }
}
