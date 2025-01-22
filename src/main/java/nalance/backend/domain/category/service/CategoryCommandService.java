package nalance.backend.domain.category.service;

import nalance.backend.domain.category.dto.CategoryDTO;
import nalance.backend.domain.category.entity.Category;

import java.util.List;

public interface CategoryCommandService {

    public void createOneCateory(CategoryDTO.CategoryRequest category);
    public void createManyCateory(List<CategoryDTO.CategoryRequest> categories);
    public Category updateCategory(CategoryDTO.CategoryUpdateRequest categoryRequest);
    public void deleteCategory(Long categoryId);
}
