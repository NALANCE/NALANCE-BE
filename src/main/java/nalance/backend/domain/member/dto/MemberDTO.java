package nalance.backend.domain.member.dto;

import lombok.*;
import nalance.backend.domain.terms.dto.MemberAgreeDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public class MemberDTO {

    public static class MemberRequest {
        @Getter
        public static class JoinRequest {
            private String email;
            private String password;
            // 닉네임은 고려하지 않았음, 추후 수정 가능
            private List<MemberAgreeDTO.AgreeTermsRequest> terms; //약관 동의 리스트
        }

        @Getter
        public static class LoginRequest {
            private String email;
            private String password;

            public UsernamePasswordAuthenticationToken toAuthentication() {
                return new UsernamePasswordAuthenticationToken(email, password);
            }
        }

        @Getter
        public static class MemberEmailUpdateRequest {
            private String email;
        }

        @Getter
        public static class MemberPasswordUpdateRequest {
            private String password;
            private String confirmPassword; // 비밀번호 확인용
        }
    }

    public static class MemberResponse {
        @Builder
        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class LoginResponse {
            private String token;
            private String refreshToken;
        }

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
