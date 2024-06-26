package roomescape.domain.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberJpaRepository extends MemberRepositoryCustom, JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findById(Long memberId);

    Member save(Member member);

    List<Member> findAll();

    Long updateAdminRole(Long id);
}
