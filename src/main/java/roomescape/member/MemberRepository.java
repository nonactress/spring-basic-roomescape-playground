package roomescape.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

    public Optional<Member> findByEmailAndPassword(String email, String password);

    public Optional<Member> findByEmail(String email);

}
