package nalance.backend.domain.category.service;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.category.dto.CategoryDTO;
import nalance.backend.domain.category.entity.Category;
import nalance.backend.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryCommandServiceImpl implements CategoryCommandService{
    private final CategoryRepository categoryRepository;
    @Override
    public void createOneCateory(CategoryDTO.CategoryRequest categoryRequest) {
        Category category = Category.builder()
                        .categoryName(categoryRequest.getCategoryName())
                        .color(categoryRequest.getColor())
                        .build();
        categoryRepository.save(category);

    }

    @Override
    public void createManyCateory(List<CategoryDTO.CategoryRequest> categoryRequests) {
        // 카테고리 리스트 생성 및 변환
        List<Category> categories = categoryRequests.stream().map(
                categoryRequest ->  Category.builder()
                        .categoryName(categoryRequest.getCategoryName())
                        .color(categoryRequest.getColor())
                        .build()
        ).collect(Collectors.toList());
        // 카테고리 리스트 전체 저장
        categoryRepository.saveAll(categories);

    }
}
