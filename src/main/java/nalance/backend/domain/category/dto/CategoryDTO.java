package nalance.backend.domain.category.dto;

import lombok.*;

@Data
@Builder
public class CategoryDTO {
    @Data
    @Builder
    public static class CategoryRequest {
        String categoryName;
        String color;
    }
    @Data
    @Builder
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
