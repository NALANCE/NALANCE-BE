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
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isActivated;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberAgree> memberAgreeList = new ArrayList<>();

    public void encodePassword(String password) {
        this.password = password;
    }

    public void updateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new MemberException(ErrorStatus.INVALID_EMAIL);
        }
        this.email = email;
    }

    public void updatePassword(String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isBlank()) {
            throw new MemberException(ErrorStatus.INVALID_PASSWORD);
        }
        this.password = encodedPassword;
    }

    public void deactivate() {
        this.isActivated = false;
    }
}
