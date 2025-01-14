package nalance.backend.domain.category.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.category.dto.CategoryDTO;
import nalance.backend.domain.category.entity.Category;
import nalance.backend.domain.category.repository.CategoryRepository;
import nalance.backend.domain.category.service.CategoryCommandService;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.error.exception.handler.CategoryHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryCommandServiceImpl implements CategoryCommandService {
    private final CategoryRepository categoryRepository;
    @Override
    public void createOneCateory(CategoryDTO.CategoryRequest categoryRequest) {
        Category category = Category.builder()
                        .categoryName(categoryRequest.getCategoryName())
                        .color(categoryRequest.getColor())
                        .build();
        // Todo : set member & exception
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
        // Todo : set member & exception
        categoryRepository.saveAll(categories);

    }

    // 특정 멤버의 카테고리 수정
    @Override
    public Category updateCategory(Long memberId, CategoryDTO.CategoryUpdateRequest categoryRequest) {
        Category category = categoryRepository.findByCategoryIdAndMember_MemberId(categoryRequest.getCategoryId(), memberId)
                .orElseThrow(() -> new CategoryHandler(ErrorStatus.CATEGORY_NOT_FOUND));
        category.updateCategoryDetails(category.getCategoryName(), category.getColor());
        return categoryRepository.save(category);
    }

    // 특정 멤버의 카테고리 삭제
    @Override
    public void deleteCategory(Long memberId, Long categoryId) {
        if (!categoryRepository.existsByCategoryIdAndMember_MemberId(categoryId, memberId)) {
            throw new CategoryHandler(ErrorStatus.CATEGORY_NOT_FOUND);
        }
        categoryRepository.deleteById(categoryId);
    }
}
