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
        String categoryName;
        String color;
        public static CategoryDTO.CategoryResponse createCategory(String categoryName, String color) {
            return CategoryResponse.builder()
                    .categoryName(categoryName)
                    .color(color)
                    .build();

        }

    }

}