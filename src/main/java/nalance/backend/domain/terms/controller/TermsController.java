package nalance.backend.domain.terms.controller;

import static nalance.backend.domain.terms.dto.TermsDTO.TermsResponse.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nalance.backend.domain.member.dto.MemberDTO;
import nalance.backend.domain.terms.dto.TermsDTO;
import nalance.backend.domain.terms.entity.Terms;
import nalance.backend.domain.terms.service.TermsQueryService;
import nalance.backend.global.validation.annotation.ExistTerms;
import nalance.backend.global.validation.validator.TermsExistValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import nalance.backend.global.error.ApiResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v0/terms")
@Tag(name = "약관 컨트롤러")
public class TermsController {
    private final TermsQueryService termsQueryService;

    @GetMapping
    @Operation(summary = "전체 약관 조회 API", description = "전체 약관 목록을 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TERMS4001", description = "전체 약관 목록 조회에 실패했습니다.")
    })
    public ApiResponse<List<TermsDetailResponse>> getAllTerms() {
        List<TermsDTO.TermsResponse.TermsDetailResponse> termsList = termsQueryService.getAllTerms();
        return ApiResponse.onSuccess(termsList);
    }

    @GetMapping("/{termsId}")
    @Operation(summary = "약관 개별 조회 API", description = "특정 약관의 세부 정보를 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TERMS4002", description = "잘못된 형식의 약관 ID 입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TERMS4003", description = "해당 약관이 존재하지 않습니다.")
    })
    public ApiResponse<TermsDetailResponse> getTermsById(@PathVariable Long termsId) {
        TermsDetailResponse terms = termsQueryService.getTermsById(termsId);
        return ApiResponse.onSuccess(terms);
    }
}
