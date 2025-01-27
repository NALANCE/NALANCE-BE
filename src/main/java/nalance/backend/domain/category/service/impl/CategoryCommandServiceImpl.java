package nalance.backend.domain.category.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.category.dto.CategoryDTO;
import nalance.backend.domain.category.entity.Category;
import nalance.backend.domain.category.repository.CategoryRepository;
import nalance.backend.domain.category.service.CategoryCommandService;
import nalance.backend.domain.member.entity.Member;
import nalance.backend.domain.member.repository.MemberRepository;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.error.handler.CategoryException;
import nalance.backend.global.error.handler.MemberException;
import nalance.backend.global.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryCommandServiceImpl implements CategoryCommandService {
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    // Todo refactor: move create method


    @Override
    public void createOneCateory(CategoryDTO.CategoryRequest categoryRequest) {
        // 현재 로그인된 회원의 ID 가져오기
        Long memberId = SecurityUtil.getCurrentMemberId();
        // Valid : 멤버의 기존 카테고리 이름 중복여부 확인
        validateCategoryNames(List.of(categoryRequest.getCategoryName()));
        // Valid : 멤버의 기존 카테고리 색상 중복여부 확인
        validateCategoryColors(List.of(categoryRequest.getCategoryName()));
        Category category = Category.builder()
                        .categoryName(categoryRequest.getCategoryName())
                        .color(categoryRequest.getColor())
                        .member(memberRepository.findById(memberId).orElseThrow(() -> new CategoryException(ErrorStatus.MEMBER_NOT_FOUND)))
                        .build();

        categoryRepository.save(category);

    }

    @Override
    public void createManyCateory(List<CategoryDTO.CategoryRequest> categoryRequests) {
        // 현재 로그인된 회원의 ID 가져오기
        Long memberId = SecurityUtil.getCurrentMemberId();
        // 각 카테고리에 대한 유효성 검증
        for (CategoryDTO.CategoryRequest request : categoryRequests) {
            validateCategoryNames(List.of(request.getCategoryName())); // 이름 검증
            validateCategoryColors(List.of(request.getColor()));       // 색상 검증
        }
//        // Valid : 멤버의 기존 카테고리 이름 중복여부 확인
//        List<String> newCategoryNames = categoryRequests.stream()
//                .map(CategoryDTO.CategoryRequest::getCategoryName)
//                .toList();
//
//        if (!categoryRequests.isEmpty()) {
//            validateCategoryNames(newCategoryNames);
//        }
//        // Valid : 멤버의 기존 카테고리 색상 중복여부 확인
//        List<String> newCategoryColors = categoryRequests.stream()
//                .map(CategoryDTO.CategoryRequest::getColor)
//                .toList();
//
//        if (!categoryRequests.isEmpty()) {
//            validateCategoryColors(newCategoryColors);
//        }
        // 카테고리 리스트 생성 및 변환
        List<Category> categories = categoryRequests.stream().map(
                categoryRequest ->  Category.builder()
                        .categoryName(categoryRequest.getCategoryName())
                        .color(categoryRequest.getColor())
                        .member(memberRepository.findById(memberId).orElseThrow(() -> new CategoryException(ErrorStatus.MEMBER_NOT_FOUND)))
                        .build()
        ).collect(Collectors.toList());

        categoryRepository.saveAll(categories);

    }

    // 특정 멤버의 카테고리 수정
    @Override
    public Category updateCategory(CategoryDTO.CategoryUpdateRequest categoryRequest) {
        // 현재 로그인된 회원의 ID 가져오기
        Long memberId = SecurityUtil.getCurrentMemberId();
        Category category = categoryRepository.findByCategoryIdAndMember_MemberId(categoryRequest.getCategoryId(), memberId)
                .orElseThrow(() -> new CategoryException(ErrorStatus.CATEGORY_NOT_FOUND));

        // 변경하려는 이름이 현재 이름과 다를 경우만 검증
        if (!category.getCategoryName().equals(categoryRequest.getCategoryName())) {
            validateCategoryNames(List.of(categoryRequest.getCategoryName()));
        }

        // 변경하려는 색상이 현재 색상과 다를 경우만 검증
        if (!category.getColor().equals(categoryRequest.getColor())) {
            validateCategoryColors(List.of(categoryRequest.getColor()));
        }

        category.updateCategoryDetails(categoryRequest.getCategoryName(), categoryRequest.getColor());
        return categoryRepository.save(category);
    }

    // 특정 멤버의 카테고리 삭제
    @Override
    public void deleteCategory(Long categoryId) {
        // 현재 로그인된 회원의 ID 가져오기
        Long memberId = SecurityUtil.getCurrentMemberId();
        if (!categoryRepository.existsByCategoryIdAndMember_MemberId(categoryId, memberId)) {
            throw new CategoryException(ErrorStatus.CATEGORY_NOT_FOUND);
        }
        categoryRepository.deleteById(categoryId);
    }

    // 멤버별 중복된 카테고리명 검증 메소드
    private void validateCategoryNames(List<String> categoryNames) {
        // 현재 로그인된 회원의 ID 가져오기
        Long memberId = SecurityUtil.getCurrentMemberId();
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
    private void validateCategoryColors(List<String> categoryColors) {
        // 현재 로그인된 회원의 ID 가져오기
        Long memberId = SecurityUtil.getCurrentMemberId();
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
