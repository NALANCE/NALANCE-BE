package nalance.backend.domain.member.controller;

import static  nalance.backend.domain.member.dto.MemberDTO.MemberRequest.*;
import static  nalance.backend.domain.member.dto.MemberDTO.MemberResponse.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nalance.backend.domain.member.service.EmailCommandService;
import nalance.backend.domain.member.service.MemberCommandService;
import nalance.backend.global.error.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v0/members")
@Tag(name = "로그인/회원가입 컨트롤러")
public class MemberController {

    private MemberCommandService memberCommandService;
    private EmailCommandService emailCommandService;

    @PostMapping("/")
    @Operation(summary = "회원가입 API", description = "회원가입에 사용하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4002", description = "회원가입에 실패했습니다.")
    })
    public ApiResponse<String> join(@RequestBody JoinRequest joinRequest) {
        memberCommandService.joinMember(joinRequest);
        return ApiResponse.onSuccess("회원가입 성공");
    }

    @PostMapping("/login")
    @Operation(
            summary = "로그인, 토큰 발급 API",
            description = "회원 로그인을 처리하고 JWT 토큰을 발급하는 API입니다.",
            security = @SecurityRequirement(name = "JWT TOKEN")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4003", description = "아이디와 비밀번호가 일치하지 않습니다."),
    })
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = memberCommandService.login(loginRequest);
        return ApiResponse.onSuccess(loginResponse);
    }

    @PatchMapping("/email")
    @Operation(
            summary = "이메일 수정 API",
            description = "로그인된 상태에서 이메일을 수정합니다.",
            security = @SecurityRequirement(name = "JWT TOKEN")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4004", description = "아이디 변경에 실패했습니다."),
    })
    public ApiResponse<String> updateEmail(@RequestBody MemberEmailUpdateRequest request) {
        memberCommandService.updateEmail(request);
        return ApiResponse.onSuccess("이메일 변경 성공");
    }

    @PatchMapping("/password")
    @Operation(
            summary = "비밀번호 변경 API",
            description = "로그인된 상태에서 새로운 비밀번호와 확인 비밀번호를 입력하는 비밀번호 변경 API입니다.",
            security = @SecurityRequirement(name = "JWT TOKEN")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4005", description = "변경하려는 비밀번호와 비밀번호 확인이 일치하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4006", description = "비밀번호 변경에 실패했습니다.")
    })
    public ApiResponse<String> updatePassword(@RequestBody MemberPasswordUpdateRequest request) {
        memberCommandService.updatePassword(request);
        return ApiResponse.onSuccess("비밀번호 변경 성공");
    }

    @PatchMapping("/")
    @Operation(
            summary = "회원탈퇴 API",
            description = "회원 탈퇴에 사용하는 API입니다.",
            security = @SecurityRequirement(name = "JWT TOKEN")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4007", description = "회원 탈퇴에 실패했습니다.")
    })
    public ApiResponse<String> deleteMember() {
        memberCommandService.deleteMember();
        return ApiResponse.onSuccess("회원 탈퇴 성공");
    }

}
