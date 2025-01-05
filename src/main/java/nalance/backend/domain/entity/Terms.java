package nalance.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import nalance.backend.global.common.BaseEntity;
import nalance.backend.global.common.enums.Type;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Terms extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termsId;

    private String content;

    @Enumerated(EnumType.STRING)
    private Type type;
}
