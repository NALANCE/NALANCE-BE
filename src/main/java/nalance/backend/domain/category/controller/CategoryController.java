package nalance.backend.domain.category.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nalance.backend.domain.category.dto.CategoryDTO;
import nalance.backend.domain.category.entity.Category;
import nalance.backend.domain.category.service.CategoryCommandServiceImpl;
import nalance.backend.domain.category.service.CategoryQueryServiceImpl;
import nalance.backend.global.error.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v0")
@Tag(name = "카테고리 컨트롤러")
public class CategoryController {
    private final CategoryCommandServiceImpl categoryCommandService;
    private final CategoryQueryServiceImpl categoryQueryService;

    /**
     * 카테고리 1개 생성
     * 사용자가 초기 카테고리 세팅 이후에 추가할 때 사용
     *
     * */
    // Todo 로그인 기능 완료 시 , member추가
    @PostMapping("/categories")
    @Operation(summary = "카테고리 생성 API", description = "카테고리 하나 생성하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CATEGORY4002", description = "카테고리 생성에 실패했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CATEGORY4003", description = "카테고리명은 필수입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CATEGORY4004", description = "카테고리 색상은 필수 입니다.")
    })
    public ApiResponse<?> createOneCategory(@RequestBody CategoryDTO.CategoryRequest categoryRequest){
        categoryCommandService.createOneCateory(categoryRequest);
        return ApiResponse.onSuccess("카테고리가 생성되었습니다.");
    }

    /**
     * 카테고리 여러개 생성
     * 사용자가 초기 카테고리 세팅 때 사용
     *
     * */
    // Todo 로그인 기능 완료 시 , member추가
    @PostMapping("/categories/all")
    @Operation(summary = "카테고리 여러 개 생성 API", description = "카테고리 여러 개를 생성하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CATEGORY4002", description = "카테고리 생성에 실패했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CATEGORY4003", description = "카테고리명은 필수입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CATEGORY4004", description = "카테고리 색상은 필수 입니다.")
    })
    public ApiResponse<?> createManyCategory(@RequestBody List<CategoryDTO.CategoryRequest> categoryRequest){
        categoryCommandService.createManyCateory(categoryRequest);
        return ApiResponse.onSuccess("카테고리가 생성되었습니다.");
    }

    // 특정 멤버의 모든 카테고리 조회
    // Todo 로그인 기능 완료 시 , 토큰에서 member값 가져옴
    @GetMapping("/members/{memberId}/categories")
    @Operation(summary = "멤버의 카테고리 조회 API", description = "특정 멤버의 모든 카테고리를 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "카테고리 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "멤버를 찾을 수 없음")
    })
    public ApiResponse<List<CategoryDTO.CategoryResponse>> getCategoriesByMember(@PathVariable Long memberId) {
        List<Category> categories = categoryQueryService.getCategoriesByMember(memberId);
        List<CategoryDTO.CategoryResponse> categoryDTOS = categories.stream().map(category -> CategoryDTO.CategoryResponse.builder()
                .categoryName(category.getCategoryName())
                .color(category.getColor()).build()).collect(Collectors.toList());

        return ApiResponse.onSuccess(categoryDTOS);
    }

    // 특정 멤버의 카테고리 수정
    // Todo 로그인 기능 완료 시 , 토큰에서 member값 가져옴
    @PatchMapping("/categories/{categoryId}")
    @Operation(summary = "카테고리 수정 API", description = "특정 멤버의 카테고리를 수정하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "카테고리 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CATEGORY4002", description = "카테고리를 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CATEGORY4003", description = "카테고리명은 필수입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CATEGORY4004", description = "카테고리 색상은 필수 입니다.")
    })
    public ApiResponse<Category> updateCategory(
            @RequestParam Long memberId,
            @RequestBody CategoryDTO.CategoryUpdateRequest categoryUpdateRequest) {
        Category updatedCategory = categoryCommandService.updateCategory(memberId, categoryUpdateRequest);
        return ApiResponse.onSuccess(updatedCategory);
    }

    // 특정 멤버의 카테고리 삭제
    // Todo 로그인 기능 완료 시 , 토큰에서 member값 가져옴
    @DeleteMapping("/categories/{categoryId}")
    @Operation(summary = "카테고리 삭제 API", description = "특정 멤버의 카테고리를 삭제하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "카테고리 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CATEGORY4002", description = "카테고리를 찾을 수 없음")
    })
    public ApiResponse<?> deleteCategory(
            @RequestParam Long memberId,
            @PathVariable Long categoryId) {
        categoryCommandService.deleteCategory(memberId, categoryId);
        return ApiResponse.onSuccess("카테고리가 삭제되었습니다");
    }

}
