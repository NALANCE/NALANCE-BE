package nalance.backend.domain.terms.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.terms.dto.TermsDTO;
import nalance.backend.domain.terms.service.TermsQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsQueryServiceImpl implements TermsQueryService {
    @Override
    public List<TermsDTO.TermsResponse.TermsDetailResponse> getAllTerms() {
        // 작성 x
        // 약관 리스트 형태로 전체 출력
        return null;
    }

    @Override
    public TermsDTO.TermsResponse.TermsDetailResponse getTermsById(Long termsId) {
        // 작성 x
        // 약관 하나만 출력
        return null;
    }
}
