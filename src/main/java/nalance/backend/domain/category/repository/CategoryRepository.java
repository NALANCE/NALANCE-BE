package nalance.backend.domain.category.repository;

import nalance.backend.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findCategoriesByMember_MemberId(Long memberId); // 특정 멤버의 카테고리 조회

    // 특정 멤버의 카테고리를 ID로 조회
    Optional<Category> findByCategoryIdAndMember_MemberId(Long categoryId, Long memberId);

    // 특정 멤버의 카테고리 존재 여부 확인
    boolean existsByCategoryIdAndMember_MemberId(Long categoryId, Long memberId);

    @Query("SELECT c FROM Category c WHERE c.member.memberId = :memberId AND c.categoryName = :categoryName")
    Optional<Category> findByMemberIdAndCategoryName(@Param("memberId") Long memberId, @Param("categoryName") String categoryName);


    @Query("SELECT COALESCE(MAX(mc.displayOrder), 0) FROM Category mc WHERE mc.member.memberId = :memberId")
    Integer findMaxDisplayOrderByMember(@Param("memberId") Long memberId);


}

