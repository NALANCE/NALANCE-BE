package nalance.backend.domain.member.controller;

import static nalance.backend.domain.member.dto.EmailDTO.EmailRequest.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nalance.backend.domain.member.service.EmailCommandService;
import nalance.backend.global.error.ApiResponse;
import nalance.backend.global.validation.annotation.CheckFile;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v0/emails")
@Tag(name = "이메일 관련 컨트롤러")
public class EmailController {

    private final EmailCommandService emailCommandService;

    @PostMapping("/send-verification")
    @Operation(summary = "인증 코드 이메일 전송 API", description = "회원가입시 이메일로 인증 코드를 보내는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "EMAIL4001", description = "이메일 전송에 실패했습니다.")
    })
    public ApiResponse<String> sendVerificationCodeToEmail(
            @RequestBody @Valid EmailSendVerificationRequest emailSendVerificationRequest) {
        emailCommandService.sendVerificationCodeToEmail(emailSendVerificationRequest);
        return ApiResponse.onSuccess("인증 코드 전송 성공");
    }

    @PostMapping("/verification")
    @Operation(summary = "인증 코드 일치 여부 확인 API", description = "이메일로 보낸 인증 코드와 입력된 코드의 일치 여부를 판단하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "EMAIL4002", description = "해당 이메일로 전송된 코드가 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "EMAIL4003", description = "인증번호가 일치하지 않습니다.")
    })
    public ApiResponse<String> verificationEmailCode(
            @RequestBody @Valid VerificationEmailCodeRequest verificationEmailCodeRequest) {
        emailCommandService.verificationEmailCode(verificationEmailCodeRequest);
        return ApiResponse.onSuccess("코드 인증 성공");
    }

    @PostMapping(value = "/picture/send-screenshot", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "캡쳐한 사진을 유저의 이메일로 보내는 API", description = """
            캡쳐한 사진을 유저의 이메일로 보내는 API입니다.
                    
            token을 필수적으로 넣어주세요.
                     
            파일 확장자는 jpg, jpeg, png를 허용합니다. 또한 파일 크기는 5MB 이내로 제한합니다. (5MB ~ 10MB에서만 커스텀 에러 리턴)
            """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "EMAIL4001", description = "이메일 전송에 실패했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "FILE4001", description = "파일 형식이 올바르지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "FILE4002", description = "파일이 업로드되지 않았습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "FILE4003", description = "파일 크기는 5MB를 초과할 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "FILE4004", description = "허용되지 않는 MIME 형식입니다. (허용 형식: image/jpeg, image/png)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "FILE4005", description = "잘못된 파일 이름입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "해당 멤버가 존재하지 않습니다.")
    })
    public ApiResponse<String> sendImageToEmail(@RequestParam("file") @CheckFile MultipartFile file) {
        emailCommandService.sendImageToEmail(file);
        return ApiResponse.onSuccess("사진 전송 성공");
    }
}
