package roomescape.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

    public Optional<Member> findByEmailAndPassword(String email, String password);

    public Optional<Member> findByEmail(String email);

}
