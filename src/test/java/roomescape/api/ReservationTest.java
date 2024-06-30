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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTest {

    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String TIME_ID = "timeId";
    private static final String THEME_ID = "themeId";
    private static final String TOKEN = "token";
    private static final String CURRENT_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    private ThemeTest themeTest = new ThemeTest();
    private TimeTest timeTest = new TimeTest();
    private MemberTest memberTest = new MemberTest();

    @Test
    void 예약을_등록한다() {
        Long themeId = themeTest.createThemeAndGetThemeId();
        Long timeId = timeTest.createTimeAndTimeId();
        memberTest.joinAndGetMemberId();
        String token = memberTest.loginAndGetToken();
        createReservationAndReservationId(themeId, timeId, token);
    }

    @Test
    void 예약을_조회한다() {

        //given
        Long themeId = themeTest.createThemeAndGetThemeId();
        Long timeId = timeTest.createTimeAndTimeId();
        memberTest.joinAndGetMemberId();
        String token = memberTest.loginAndGetToken();
        createReservationAndReservationId(themeId, timeId, token);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 예약을_삭제한다() {

        //given
        Long themeId = themeTest.createThemeAndGetThemeId();
        Long timeId = timeTest.createTimeAndTimeId();
        memberTest.joinAndGetMemberId();
        String token = memberTest.loginAndGetToken();
        Long id = createReservationAndReservationId(themeId, timeId, token);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/reservations/" + id)
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 내_예약을_조회한다() {

        //given
        Long firstThemeId = themeTest.createThemeAndGetThemeId();
        Long firstTimeId = timeTest.createTimeAndTimeId();
        Long secondThemeId = themeTest.createThemeAndGetThemeId();
        Long secondTimeId = timeTest.createTimeAndTimeId();
        memberTest.joinAndGetMemberId();
        String token = memberTest.loginAndGetToken();
        createReservationAndReservationId(firstThemeId, firstTimeId, token);
        createReservationAndReservationId(secondThemeId, secondTimeId, token);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie(TOKEN, token)
                .when().get("/reservations/mine")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    protected Long createReservationAndReservationId(Long themeId, Long timeId, String token) {

        //given
        Map<String, Object> reservation = new HashMap<>();
        reservation.put(NAME, "박민욱");
        reservation.put(DATE, CURRENT_DATE);
        reservation.put(TIME_ID, timeId);
        reservation.put(THEME_ID, themeId);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(TOKEN, token)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getLong(ID);
    }
}
