package nalance.backend.domain.member.controller;

import static  nalance.backend.domain.member.dto.MemberDTO.MemberRequest.*;
import static  nalance.backend.domain.member.dto.MemberDTO.MemberResponse.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nalance.backend.domain.member.service.MemberCommandService;
import nalance.backend.domain.member.service.MemberQueryService;
import nalance.backend.global.error.ApiResponse;
import nalance.backend.global.jwt.dto.TokenDTO.TokenResponse;
import nalance.backend.global.jwt.dto.TokenDTO.TokenRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v0/members")
@Tag(name = "로그인/회원가입 컨트롤러")
public class MemberController {
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입 API",
            description = """
           회원가입을 처리하는 API입니다.
           
           회원 정보를 입력하고 동의한 약관과 생성한 카테고리 정보로 회원 가입을 진행합니다.
           
           동의한 약관의 ID 리스트와, 생성한 카테고리 정보 리스트가 필요합니다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "EMAIL4005", description = "이미 존재하는 이메일입니다.")
    })
    public ApiResponse<String> join(@RequestBody @Valid JoinRequest joinRequest) {
        memberCommandService.joinMember(joinRequest);
        return ApiResponse.onSuccess("회원가입 성공");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인, 토큰 발급 API",
            description = """
           회원 로그인을 처리하고 JWT 토큰을 발급하는 API입니다.
           
           생성된 accessToken으로 Authorize할 수 있습니다.
           
           accessToken 만료 시간은 30분입니다.
            """,
            security = @SecurityRequirement(name = "JWT TOKEN"))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4002", description = "아이디와 비밀번호가 일치하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4003", description = "탈퇴한 회원입니다."),
    })
    public ApiResponse<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
         TokenResponse loginResponse = memberCommandService.login(loginRequest);
        return ApiResponse.onSuccess(loginResponse);
    }

    @PostMapping("/reissue")
    @Operation(summary = "accessToken 재발급 API",
            description = """
            accessToken 만료시 refreshToken으로 accessToken을 재발급하는 API입니다.
            
            String 타입의 기존 accessToken과 refreshToken 값이 필요합니다.
            
            refreshToken 만료 시간은 7일입니다.
            
            refreshToken 만료 후에는 재로그인이 필요합니다.
            """,
            security = @SecurityRequirement(name = "JWT TOKEN"))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4004", description = "유효하지 않은 토큰입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4005", description = "로그아웃된 사용자입니다.")
    })
    public ApiResponse<TokenResponse> reissue(@RequestBody TokenRequest request) {
        TokenResponse reissueResponse = memberCommandService.reissue(request);
        return ApiResponse.onSuccess(reissueResponse);
    }

    @PatchMapping("/email")
    @Operation(
            summary = "이메일 수정 API",
            description = "로그인된 상태에서 이메일을 수정하는 api입니다. 변경할 이메일 문자열을 포함합니다.",
            security = @SecurityRequirement(name = "JWT TOKEN")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4006", description = "아이디가 비어있습니다."),
    })
    public ApiResponse<String> updateEmail(@RequestBody @Valid MemberEmailUpdateRequest request) {
        memberCommandService.updateEmail(request);
        return ApiResponse.onSuccess("이메일 변경 성공");
    }

    @PostMapping("/validate-password")
    @Operation(
            summary = "비밀번호 검증 API",
            description = """
            새로운 비밀번호가 기존 비밀번호와 같은지 검증하는 API입니다.
            
            새로운 비밀번호가 기존의 비밀번호와 일치하면 true를 반환합니다.
            
            새로운 비밀번호가 기존의 비밀번호와 다르면 false를 반환합니다.
            """,
            security = @SecurityRequirement(name = "JWT TOKEN")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "해당 멤버가 존재하지 않습니다.")
    })
    public ApiResponse<Boolean> validatePassword(@RequestBody @Valid MemberPasswordValidationRequest request) {
        boolean isSame = memberCommandService.validatePassword(request);
        return ApiResponse.onSuccess(isSame);
    }

    @PatchMapping("/password")
    @Operation(
            summary = "비밀번호 변경 API",
            description = "로그인된 상태에서 새로운 비밀번호와 확인 비밀번호를 입력하는 비밀번호 변경 API입니다.",
            security = @SecurityRequirement(name = "JWT TOKEN")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4007", description = "비밀번호가 비어있습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4008", description = "비밀번호와 확인 비밀번호가 일치하지 않습니다.")
    })
    public ApiResponse<String> updatePassword(@RequestBody @Valid MemberPasswordUpdateRequest request) {
        memberCommandService.updatePassword(request);
        return ApiResponse.onSuccess("비밀번호 변경 성공");
    }

    @PatchMapping
    @Operation(
            summary = "회원탈퇴 API",
            description = "회원 탈퇴에 사용하는 API입니다. 회원의 활성화 상태가 false로 변경됩니다.",
            security = @SecurityRequirement(name = "JWT TOKEN")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4009", description = "회원 탈퇴에 실패했습니다.")
    })
    public ApiResponse<String> deleteMember() {
        memberCommandService.deleteMember();
        return ApiResponse.onSuccess("회원 탈퇴 성공");
    }

    @GetMapping("me")
    @Operation(
            summary = "회원 정보 조회 API",
            description = "로그인된 회원이 자신의 정보를 조회할 수 있는 API입니다.",
            security = @SecurityRequirement(name = "JWT TOKEN")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "해당 멤버가 존재하지 않습니다.")
    })
    public ApiResponse<MemberProfileResponse> getMemberProfile() {
        MemberProfileResponse memberProfile = memberQueryService.getMemberProfile();
        return ApiResponse.onSuccess(memberProfile);
    }

}
