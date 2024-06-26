package roomescape.domain.member.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.domain.Role;
import roomescape.domain.member.domain.repository.MemberJpaRepository;
import roomescape.domain.member.error.exception.MemberErrorCode;
import roomescape.domain.member.error.exception.MemberException;
import roomescape.domain.member.service.dto.MemberLoginRequest;
import roomescape.domain.member.service.dto.MemberRequest;

import java.util.List;

@Service
public class MemberService {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String NAME = "name";

    private final MemberJpaRepository memberRepository;

    public MemberService(MemberJpaRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public String login(MemberLoginRequest memberLoginRequest) {
        Member member = memberRepository.findByEmailAndPassword(memberLoginRequest.getEmail(), memberLoginRequest.getPassword()).orElseThrow(() -> new MemberException(MemberErrorCode.INVALID_EMAIL_OR_PASSWORD_ERROR));
        return encode(member);
    }

    @Transactional(readOnly = true)
    public Member findByToken(String token) {
        Long memberId = decode(token);
        return findById(memberId);
    }

    @Transactional
    public Member save(MemberRequest memberRequest) {
        return memberRepository.save(new Member(null, memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), Role.USER.getRole()));
    }

    @Transactional(readOnly = true)
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorCode.INVALID_MEMBER_DETAILS_ERROR));
    }

    @Transactional(readOnly = true)
    public List<Member> findAll() {
        List<Member> members = memberRepository.findAll();
        if (members.isEmpty()) {
            throw new MemberException(MemberErrorCode.NO_MEMBER_ERROR);
        }
        return members;
    }

    @Transactional
    public Member updateAdminRole(Long memberId) {
        Long id = memberRepository.updateAdminRole(memberId);
        return findById(id);
    }

    private String encode(Member member) {
        return Jwts.builder()
                .claim(ID, member.getId())
                .claim(EMAIL, member.getEmail())
                .claim(NAME, member.getName())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    private static Long decode(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().get(ID, Long.class);
    }
}
