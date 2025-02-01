package nalance.backend.domain.member.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import nalance.backend.domain.category.dto.CategoryDTO;
import nalance.backend.domain.terms.dto.MemberAgreeDTO;
import nalance.backend.global.validation.annotation.PasswordMatch;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public class MemberDTO {

    public static class MemberRequest {
        @Getter
        // @PasswordMatch 비밀번호와 확인 비밀번호가 일치하는지 검증 (추후 필요하면 사용예정)
        public static class JoinRequest {
            @NotBlank(message = "이메일은 필수 입력 값입니다.")
            @Email(message = "올바른 이메일 형식이 아닙니다.")
            private String email;

            @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
            @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
            @Pattern(
                    regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
                    message = "비밀번호는 영어, 숫자, 특수문자를 포함하며 8자 이상이어야 합니다."
            )
            private String password;

            // 닉네임은 고려하지 않았음, 추후 수정 가능
            @NotEmpty(message = "약관 동의 항목은 비어 있을 수 없습니다.")
            private List<MemberAgreeDTO.AgreeTermsRequest> terms; //약관 동의 리스트

            // 회원가입 과정에서 생성하는 카테고리 정보
            private List<CategoryDTO.CategoryRequest> categories;
        }

        @Getter
        public static class LoginRequest {
            @NotBlank(message = "이메일은 필수 입력 값입니다.")
            @Email(message = "올바른 이메일 형식이 아닙니다.")
            private String email;

            @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
            @Pattern(
                    regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
                    message = "비밀번호는 영어, 숫자, 특수문자를 포함하며 8자 이상이어야 합니다."
            )
            private String password;

            public UsernamePasswordAuthenticationToken toAuthentication() {
                return new UsernamePasswordAuthenticationToken(email, password);
            }
        }

        @Getter
        public static class MemberEmailUpdateRequest {
            @NotBlank(message = "이메일은 필수 입력 값입니다.")
            @Email(message = "올바른 이메일 형식이 아닙니다.")
            private String email;
        }

        @Getter
        @PasswordMatch
        public static class MemberPasswordUpdateRequest {
            @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
            @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
            @Pattern(
                    regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
                    message = "비밀번호는 영어, 숫자, 특수문자를 포함하며 8자 이상이어야 합니다."
            )
            private String password;

            @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
            private String confirmPassword; // 비밀번호 확인용
        }
    }

    public static class MemberResponse {
        @Builder
        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class MemberProfileResponse {
            private Long memberId;
            private String email;
            private String nickname;
            private boolean isActivated;
            private List<MemberAgreeDTO.AgreeTermsResponse> agreedTerms; // 회원이 동의한 약관 목록
        }
    }

}
