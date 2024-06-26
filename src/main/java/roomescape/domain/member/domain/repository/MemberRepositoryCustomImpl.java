package roomescape.domain.member.domain.repository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.error.exception.MemberErrorCode;
import roomescape.domain.member.error.exception.MemberException;

@Repository
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager entityManager;

    public MemberRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long updateAdminRole(Long id) {
        Member member = entityManager.find(Member.class, id);
        if (member != null) {
            member.grantAdminRole();
            entityManager.persist(member);
            return member.getId();
        }
        throw new MemberException(MemberErrorCode.INVALID_MEMBER_DETAILS_ERROR);
    }
}
