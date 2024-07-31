package roomescape.acceptance.user;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.waiting.error.exception.WaitingErrorCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.restAssured.MemberRestAssured.로그인_요청;
import static roomescape.restAssured.MemberRestAssured.회원가입_요청;
import static roomescape.restAssured.ReservationRestAssured.*;
import static roomescape.restAssured.ThemeRestAssured.테마_생성_요청;
import static roomescape.restAssured.TimeRestAssured.예약_시간_생성_요청;
import static roomescape.restAssured.WaitingRestAssured.예약_대기_삭제_요청;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WaitingAcceptance {

    private static final String CURRENT_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private static final String FIVE_MINUTES_AHEAD_OF_THE_CURRENT_TIME = LocalTime.now().plusMinutes(5L).format(DateTimeFormatter.ofPattern("HH:mm"));
    private static final String ID = "id";
    private static final String TOKEN = "token";
    private static final String STATUS = "status";
    private static final String ERROR_MESSAGES = "errorMessages";

    private Long 공포_테마_아이디 = null;
    private Long 오_분_뒤_예약시간_아이디 = null;
    private String 첫_번째_사용자_토큰 = null;
    private String 두_번째_사용자_토큰 = null;
    private String 세_번째_사용자_토큰 = null;

    /**
     * given: 예약 생성에 필요한 테마, 예약 시간 및 사용자 생성을 요청한다.
     */
    @BeforeEach
    void 계정을_생성하고_예약_생성에_필요한_테마와_예약_시간을_요청한다() {

        //given
        회원가입_요청("uo5234@naver.com", "Password12!", "박민욱", "/members").jsonPath().getLong(ID);
        회원가입_요청("orange@naver.com", "Orange12@", "우주황", "/members").jsonPath().getLong(ID);
        회원가입_요청("picachu@naver.com", "Picachu12@", "김성찬", "/members").jsonPath().getLong(ID);
        첫_번째_사용자_토큰 = 로그인_요청("uo5234@naver.com", "Password12!", "/login").cookie(TOKEN);
        두_번째_사용자_토큰 = 로그인_요청("orange@naver.com", "Orange12@", "/login").cookie(TOKEN);
        세_번째_사용자_토큰 = 로그인_요청("picachu@naver.com", "Picachu12@", "/login").cookie(TOKEN);
        공포_테마_아이디 = 테마_생성_요청("오싹 공포 테마", "오싹 그잡채", "https://youtube.com/fear", "/themes").jsonPath().getLong(ID);
        오_분_뒤_예약시간_아이디 = 예약_시간_생성_요청(FIVE_MINUTES_AHEAD_OF_THE_CURRENT_TIME, "/times").jsonPath().getLong(ID);
    }

    /**
     * given : @BeforeEach + 예약 생성 요청(같은 날짜와 같은 테마와 같은 시간대의 상대방 예약 * 1 + 나의 예약 대기  * 1)
     * when : 사용자가 자신의 예약 대기를 삭제하면
     * then : 204 상태코드를 검증할 수 있다.
     */
    @Test
    void 사용자는_자신의_예약_대기를_삭제할_수_있다() {

        //given
        예약_생성_요청(첫_번째_사용자_토큰, "박민욱", CURRENT_DATE, 오_분_뒤_예약시간_아이디, 공포_테마_아이디, "/reservations");
        Long 예약_대기_아이디 = 예약_대기_생성_요청(두_번째_사용자_토큰, CURRENT_DATE, 오_분_뒤_예약시간_아이디, 공포_테마_아이디, "/reservations/wait").jsonPath().getLong(ID);

        //when
        ExtractableResponse<Response> response = 예약_대기_삭제_요청(두_번째_사용자_토큰, 예약_대기_아이디, "/waiting");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * given : @BeforeEach + 예약 생성 요청(같은 날짜와 같은 테마와 같은 시간대의 상대방 예약 * 1 + 상대방 예약 대기 * 1 + 나의 예약 대기  * 1 )
     * when : 나보다 먼저 예약 대기를 진행한 상대방의 예약 대기가 삭제되면
     * then : 나의 예약 대기 순번이 낮아지는 것을 검증할 수 있다.
     */
    @Test
    void 자신과_동일한_조건의_예약_시간에_대한_예약_대기를_취소한_인원이_있는_경우_자신의_예약_예약_대기_순번이_낮아진다() {

        //given
        예약_생성_요청(첫_번째_사용자_토큰, "박민욱", CURRENT_DATE, 오_분_뒤_예약시간_아이디, 공포_테마_아이디, "/reservations").jsonPath().getLong(ID);
        Long 예약_대기_아이디 = 예약_대기_생성_요청(두_번째_사용자_토큰, CURRENT_DATE, 오_분_뒤_예약시간_아이디, 공포_테마_아이디, "/reservations/wait").jsonPath().getLong(ID);
        예약_대기_생성_요청(세_번째_사용자_토큰, CURRENT_DATE, 오_분_뒤_예약시간_아이디, 공포_테마_아이디, "/reservations/wait").jsonPath().getLong(ID);

        //when
        ExtractableResponse<Response> 삭제_이_전의_두_번째_예약_대기 = 내_예약_조회_요청(세_번째_사용자_토큰, "/reservations/mine");
        int 삭제_이_전의_두_번째_예약_대기_순번 = Character.getNumericValue(삭제_이_전의_두_번째_예약_대기.jsonPath().getString(STATUS).charAt(1));
        예약_대기_삭제_요청(두_번째_사용자_토큰, 예약_대기_아이디, "/waiting");
        ExtractableResponse<Response> 삭제_이_후의_두_번째_예약_대기 = 내_예약_조회_요청(세_번째_사용자_토큰, "/reservations/mine");
        int 삭제_이_후의_두_번째_예약_대기_순번 = Character.getNumericValue(삭제_이_후의_두_번째_예약_대기.jsonPath().getString(STATUS).charAt(1));

        //then
        assertThat(삭제_이_후의_두_번째_예약_대기_순번).isLessThan(삭제_이_전의_두_번째_예약_대기_순번);
    }

    /**
     * given : @BeforeEach + 예약 생성 요청(같은 날짜와 같은 테마와 같은 시간대의 상대방 예약 * 1 + 나의 예약 대기  * 1)
     * when : 자신의 예약 대기가 아닌 예약 대기를 삭제하면
     * then : 예외가 발생한다.
     */
    @Test
    void 자신의_예약_대기가_아닌_예약_대기를_삭제하려고_하면_예외가_발생한다() {

        //given
        예약_생성_요청(첫_번째_사용자_토큰, "박민욱", CURRENT_DATE, 오_분_뒤_예약시간_아이디, 공포_테마_아이디, "/reservations");
        Long 두_번째_예약_대기_아이디 = 예약_대기_생성_요청(두_번째_사용자_토큰, CURRENT_DATE, 오_분_뒤_예약시간_아이디, 공포_테마_아이디, "/reservations/wait").jsonPath().getLong(ID);

        //when
        ExtractableResponse<Response> response = 예약_대기_삭제_요청(첫_번째_사용자_토큰, 두_번째_예약_대기_아이디, "/waiting");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString(ERROR_MESSAGES)).isEqualTo("[" + WaitingErrorCode.NOT_AUTHENTICATION_ERROR.getErrorMessage() + "]");
    }

    /**
     * given : @BeforeEach + 예약 생성 요청(같은 날짜와 같은 테마와 같은 시간대의 상대방 예약 * 1 + 나의 예약 대기  * 2)
     * when : 이미 예약 대기 생성을 하고 동일한 조건의 예약 대기를 재 생성하려면
     * then : 예외가 발생한다.
     */
    @Test
    void 똑같은_조건의_예약_대기를_생성하려고_하면_예외가_발생한다() {

        //given
        예약_생성_요청(첫_번째_사용자_토큰, "박민욱", CURRENT_DATE, 오_분_뒤_예약시간_아이디, 공포_테마_아이디, "/reservations");
        예약_대기_생성_요청(두_번째_사용자_토큰, CURRENT_DATE, 오_분_뒤_예약시간_아이디, 공포_테마_아이디, "/reservations/wait");


        //when
        ExtractableResponse<Response> response = 예약_대기_생성_요청(두_번째_사용자_토큰, CURRENT_DATE, 오_분_뒤_예약시간_아이디, 공포_테마_아이디, "/reservations/wait");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString(ERROR_MESSAGES)).isEqualTo("[" + WaitingErrorCode.DUPLICATION_ERROR.getErrorMessage() + "]");
    }
}
