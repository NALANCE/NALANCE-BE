package nalance.backend.domain.category.service;

import nalance.backend.domain.category.dto.CategoryDTO;
import nalance.backend.domain.category.entity.Category;

import java.util.List;

public interface CategoryCommandService {

    public void createOneCateory(Long memberId, CategoryDTO.CategoryRequest category);
    public void createManyCateory(Long memberId, List<CategoryDTO.CategoryRequest> categories);
    public Category updateCategory(Long memberId, CategoryDTO.CategoryUpdateRequest categoryRequest);
    public void deleteCategory(Long memberId, Long categoryId);
}
