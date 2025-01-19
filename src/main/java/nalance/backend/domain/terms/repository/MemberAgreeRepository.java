package nalance.backend.domain.terms.repository;

import nalance.backend.domain.terms.entity.MemberAgree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAgreeRepository extends JpaRepository<MemberAgree, Long> {
}
