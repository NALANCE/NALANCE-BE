package nalance.backend.domain.category.controller;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.category.dto.CategoryDTO;
import nalance.backend.domain.category.service.CategoryCommandServiceImpl;
import nalance.backend.global.error.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v0")
public class CategoryController {
    private final CategoryCommandServiceImpl categoryCommandService;

    /**
     * 카테고리 1개 생성
     * 사용자가 초기 카테고리 세팅 이후에 추가할 때 사용
     *
     * */
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
    @PostMapping("/categories")
    public ApiResponse<?> createManyCategory(@RequestBody List<CategoryDTO.CategoryRequest> categoryRequest){
        categoryCommandService.createManyCateory(categoryRequest);
        return ApiResponse.onSuccess("카테고리가 생성되었습니다.");
    }




}
