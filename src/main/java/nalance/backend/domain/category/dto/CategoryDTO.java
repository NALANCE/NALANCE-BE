package nalance.backend.domain.category.dto;

import lombok.*;

public class CategoryDTO {
    @Getter
    public static class CategoryRequest {
        String categoryName;
        String color;
    }
    @Getter
    public static class CategoryUpdateRequest {
        Long categoryId;
        String categoryName;
        String color;
    }
    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CategoryResponse {
        String categoryName;
        String color;
        public CategoryDTO.CategoryResponse createCategory(String categoryName, String color) {
            return CategoryResponse.builder()
                    .categoryName(categoryName)
                    .color(color)
                    .build();

        }

    }

}