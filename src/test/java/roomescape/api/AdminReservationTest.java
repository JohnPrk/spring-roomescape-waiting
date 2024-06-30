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
public class AdminReservationTest {

    private static final String DATE = "date";
    private static final String TIME_ID = "timeId";
    private static final String THEME_ID = "themeId";
    private static final String MEMBER_ID = "memberId";
    private static final String CURRENT_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private static final String TOKEN = "token";

    private ThemeTest themeTest = new ThemeTest();
    private TimeTest timeTest = new TimeTest();
    private MemberTest memberTest = new MemberTest();

    @Test
    void 쿠키를_이용하여_사용자를_확인하고_예약을_등록한다() {

        //given
        Long themeId = themeTest.createThemeAndGetThemeId();
        Long timeId = timeTest.createTimeAndTimeId();
        Long memberId = memberTest.joinAndGetMemberId();
        String token = memberTest.loginAndGetToken();
        memberTest.grantAdminRole(token);

        Map<String, Object> reservation = new HashMap<>();
        reservation.put(DATE, CURRENT_DATE);
        reservation.put(TIME_ID, timeId);
        reservation.put(THEME_ID, themeId);
        reservation.put(MEMBER_ID, memberId);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(TOKEN, token)
                .body(reservation)
                .when().post("admin/reservations")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
