package nalance.backend.domain.category.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.category.dto.CategoryDTO;
import nalance.backend.domain.category.entity.Category;
import nalance.backend.domain.category.repository.CategoryRepository;
import nalance.backend.domain.category.service.CategoryCommandService;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.error.handler.CategoryException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryCommandServiceImpl implements CategoryCommandService {
    private final CategoryRepository categoryRepository;
    // Todo : memberId 추후 수정 -> createOneCategory, createManyCateory
    Long memberId = 1L;
    @Override
    public void createOneCateory(CategoryDTO.CategoryRequest categoryRequest) {
        // Valid : 멤버의 기존 카테고리 이름 중복여부 확인
        validateCategoryNames(List.of(categoryRequest.getCategoryName()), memberId);
        // Valid : 멤버의 기존 카테고리 색상 중복여부 확인
        validateCategoryColors(List.of(categoryRequest.getCategoryName()), memberId);
        Category category = Category.builder()
                        .categoryName(categoryRequest.getCategoryName())
                        .color(categoryRequest.getColor())
                        .build();
        // Todo : set member & exception
        categoryRepository.save(category);

    }

    @Override
    public void createManyCateory(List<CategoryDTO.CategoryRequest> categoryRequests) {
        // Valid : 멤버의 기존 카테고리 이름 중복여부 확인
        List<String> newCategoryNames = categoryRequests.stream()
                .map(CategoryDTO.CategoryRequest::getCategoryName)
                .toList();

        if (!categoryRequests.isEmpty()) {
            validateCategoryNames(newCategoryNames, memberId);
        }
        // Valid : 멤버의 기존 카테고리 색상 중복여부 확인
        List<String> newCategoryColors = categoryRequests.stream()
                .map(CategoryDTO.CategoryRequest::getColor)
                .toList();

        if (!categoryRequests.isEmpty()) {
            validateCategoryColors(newCategoryColors, memberId);
        }
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
                .orElseThrow(() -> new CategoryException(ErrorStatus.CATEGORY_NOT_FOUND));
        // Valid : 멤버의 기존 카테고리 이름 중복여부 확인
        validateCategoryNames(List.of(categoryRequest.getCategoryName()), memberId);
        // Valid : 멤버의 기존 카테고리 색상 중복여부 확인
        validateCategoryColors(List.of(categoryRequest.getCategoryName()), memberId);

        category.updateCategoryDetails(categoryRequest.getCategoryName(), categoryRequest.getColor());
        return categoryRepository.save(category);
    }

    // 특정 멤버의 카테고리 삭제
    @Override
    public void deleteCategory(Long memberId, Long categoryId) {
        if (!categoryRepository.existsByCategoryIdAndMember_MemberId(categoryId, memberId)) {
            throw new CategoryException(ErrorStatus.CATEGORY_NOT_FOUND);
        }
        categoryRepository.deleteById(categoryId);
    }

    // 멤버별 중복된 카테고리명 검증 메소드
    private void validateCategoryNames(List<String> categoryNames, Long memberId) {
        List<String> existingCategoryNames = categoryRepository.findCategoriesByMember_MemberId(memberId)
                .stream()
                .map(Category::getCategoryName)
                .toList();

        // 중복인지 확인
        List<String> duplicateNames = categoryNames.stream()
                .filter(existingCategoryNames::contains)
                .toList();

        if (!duplicateNames.isEmpty()) {
            throw new CategoryException(ErrorStatus.CATEGORY_NAME_ALREADY_EXISTS);
        }
    }

    // 멤버별 중복된 카테고리 색상 검증 메소드
    private void validateCategoryColors(List<String> categoryColors, Long memberId) {
        List<String> existingCategoryColors = categoryRepository.findCategoriesByMember_MemberId(memberId)
                .stream()
                .map(Category::getColor)
                .toList();
        // 중복인지 확인
        List<String> duplicateColors = categoryColors.stream()
                .filter(existingCategoryColors::contains)
                .toList();
        if(!duplicateColors.isEmpty()) {
            throw new CategoryException(ErrorStatus.CATEGORY_COLOR_ALREADY_EXISTS);
        }

    }
}
