package nalance.backend.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import nalance.backend.domain.terms.entity.MemberAgree;
import nalance.backend.global.common.BaseEntity;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.error.handler.MemberException;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseEntity {
    @Id
    @Column(name = "rt_key")
    private String key;

    @Column(name = "rt_value")
    private String value;

    @Builder
    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }
}
