package nalance.backend.domain.category.dto;

import lombok.*;
import nalance.backend.global.validation.annotation.ExistCategoryColors;

public class CategoryDTO {
    @Getter
    public static class CategoryRequest {
        String categoryName;
        @ExistCategoryColors
        String color;
    }
    @Getter
    public static class CategoryUpdateRequest {
        Long categoryId;
        String categoryName;
        @ExistCategoryColors
        String color;
    }
    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CategoryResponse {
        Long categoryId;
        String categoryName;
        String color;
        public static CategoryDTO.CategoryResponse createCategory(Long categoryId, String categoryName, String color) {
            return CategoryResponse.builder()
                    .categoryId(categoryId)
                    .categoryName(categoryName)
                    .color(color)
                    .build();

        }

    }

}