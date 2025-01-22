package nalance.backend.domain.category.service;

import nalance.backend.domain.category.entity.Category;

import java.util.List;

public interface CategoryQueryService {
    public List<Category> getCategoriesByMember();
}
