package nalance.backend.domain.terms.controller;

import static nalance.backend.domain.terms.dto.TermsDTO.TermsResponse.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nalance.backend.domain.member.dto.MemberDTO;
import nalance.backend.domain.terms.dto.TermsDTO;
import nalance.backend.domain.terms.service.TermsQueryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import nalance.backend.global.error.ApiResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v0/terms")
@Tag(name = "약관 컨트롤러")
public class TermsController {
    private final TermsQueryService termsQueryService;

    @GetMapping("/")
    @Operation(summary = "전체 약관 조회 API", description = "전체 약관 목록을 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TERMS4001", description = "전체 약관 목록 조회에 실패했습니다.")
    })
    public ApiResponse<List<TermsDetailResponse>> getAllTerms() {
        List<TermsDetailResponse> termsList = termsQueryService.getAllTerms();
        return ApiResponse.onSuccess(termsList);
    }

    @GetMapping("/{termsId}")
    @Operation(summary = "약관 개별 조회 API", description = "특정 약관의 세부 정보를 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TERMS4002", description = "약관 개별 조회에 실패했습니다.")
    })
    public ApiResponse<TermsDetailResponse> getTermsById(@PathVariable Long termsId) {
        TermsDetailResponse terms = termsQueryService.getTermsById(termsId);
        return ApiResponse.onSuccess(terms);
    }
}
