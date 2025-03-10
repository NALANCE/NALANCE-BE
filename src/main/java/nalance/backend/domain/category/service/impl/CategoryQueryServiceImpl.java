package nalance.backend.domain.category.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.category.entity.Category;
import nalance.backend.domain.category.repository.CategoryRepository;
import nalance.backend.domain.category.service.CategoryQueryService;
import nalance.backend.global.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {
    private final CategoryRepository categoryRepository;
    // 조회: 특정 멤버의 모든 카테고리 조회
    @Override
    public List<Category> getCategoriesByMember() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return categoryRepository.findCategoriesByMember_MemberId(memberId);
    }
}
