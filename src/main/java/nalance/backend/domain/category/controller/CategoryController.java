package nalance.backend.domain.category.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nalance.backend.domain.category.dto.CategoryDTO;
import nalance.backend.domain.category.entity.Category;
import nalance.backend.domain.category.service.CategoryCommandServiceImpl;
import nalance.backend.domain.category.service.CategoryQueryServiceImpl;
import nalance.backend.global.error.ApiResponse;
import org.springframework.http.ResponseEntity;
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
    public ApiResponse<?> createManyCategory(@RequestBody List<CategoryDTO.CategoryRequest> categoryRequest){
        categoryCommandService.createManyCateory(categoryRequest);
        return ApiResponse.onSuccess("카테고리가 생성되었습니다.");
    }

    // 특정 멤버의 모든 카테고리 조회
    // Todo 로그인 기능 완료 시 , 토큰에서 member값 가져옴
    @GetMapping("/members/{memberId}/categories")
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
    public ApiResponse<Category> updateCategory(
            @RequestParam Long memberId,
            @RequestBody CategoryDTO.CategoryUpdateRequest categoryUpdateRequest) {
        Category updatedCategory = categoryCommandService.updateCategory(memberId, categoryUpdateRequest);
        return ApiResponse.onSuccess(updatedCategory);
    }

    // 특정 멤버의 카테고리 삭제
    // Todo 로그인 기능 완료 시 , 토큰에서 member값 가져옴
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @RequestParam Long memberId,
            @PathVariable Long categoryId) {
        categoryCommandService.deleteCategory(memberId, categoryId);
        return ResponseEntity.noContent().build();
    }

}
