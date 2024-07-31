package roomescape.restAssured;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class WaitingRestAssured {

    private static final String 토큰 = "token";

    public static ExtractableResponse<Response> 예약_대기_삭제_요청(String token, Long waitingId, String url) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(토큰, token)
                .when().delete(url + "/" + waitingId)
                .then().log().all()
                .extract();
    }
}
