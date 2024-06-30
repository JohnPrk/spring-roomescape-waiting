package roomescape.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberTest {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String TOKEN = "token";
    private static final String ROLE = "role";
    private static final String ID = "id";

    @Test
    void 회원가입을_한다() {

        joinAndGetMemberId();
    }

    @Test
    void 로그인을_하고_쿠키를_얻는다() {

        joinAndGetMemberId();
        loginAndGetToken();
    }

    @Test
    void 쿠키를_이용해서_사용자_정보를_조회한다() {

        //given
        joinAndGetMemberId();
        String token = loginAndGetToken();

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(TOKEN, token)
                .when().get("/login/check")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString(NAME)).isEqualTo("박민욱");
    }

    @Test
    void 쿠키를_이용해서_사용자를_확인하고_관리자_권한을_부여한다() {

        //given
        joinAndGetMemberId();
        String token = loginAndGetToken();

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(TOKEN, token)
                .when().post("/members/role")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString(ROLE)).isEqualTo("ADMIN");
    }

    protected String loginAndGetToken() {

        //given
        Map<String, Object> member = new HashMap<>();
        member.put(EMAIL, "test@naver.com");
        member.put(PASSWORD, "password123");

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(member)
                .when().post("/login")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.cookie(TOKEN)).isNotNull();
        return response.cookie(TOKEN);
    }

    protected Long joinAndGetMemberId() {

        //given
        Map<String, Object> member = new HashMap<>();
        member.put(EMAIL, "test@naver.com");
        member.put(PASSWORD, "password123");
        member.put(NAME, "박민욱");

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(member)
                .when().post("/members")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString(NAME)).isEqualTo("박민욱");
        return response.jsonPath().getLong(ID);
    }

    protected void grantAdminRole(String token) {

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(TOKEN, token)
                .when().post("/members/role")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString(ROLE)).isEqualTo("ADMIN");
    }
}
