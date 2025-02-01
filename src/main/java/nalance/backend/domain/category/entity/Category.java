package nalance.backend.domain.category.entity;

import jakarta.persistence.*;
import lombok.*;
import nalance.backend.domain.member.entity.Member;
import nalance.backend.global.common.BaseEntity;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.error.handler.CategoryException;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String categoryName;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false, unique = true)
    private Integer displayOrder;

    public void setDisplayOrder(Integer displayOrder) {
        if (displayOrder == null || displayOrder <= 0) {
            throw new CategoryException(ErrorStatus.CATEGORY_DISPLAYORDER_NOT_ALLOWED);
        }
        this.displayOrder = displayOrder;
    }


    public void updateCategoryDetails(String categoryName, String color) {
        if (categoryName != null && !categoryName.isBlank()) {
            this.categoryName = categoryName;
        }
        if (color != null && !color.isBlank()) {
            this.color = color;
        }
    }

}
