package nalance.backend.domain.terms.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.terms.dto.TermsDTO;
import nalance.backend.domain.terms.entity.Terms;
import nalance.backend.domain.terms.repository.TermsRepository;
import nalance.backend.domain.terms.service.TermsQueryService;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.error.handler.TermsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsQueryServiceImpl implements TermsQueryService {
    private final TermsRepository termsRepository;

    @Override
    public List<TermsDTO.TermsResponse.TermsDetailResponse> getAllTerms() {
        return termsRepository.findAll().stream()
                .map(terms -> TermsDTO.TermsResponse.TermsDetailResponse.builder()
                        .termsId(terms.getTermsId())
                        .content(terms.getContent())
                        .type(terms.getType())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public TermsDTO.TermsResponse.TermsDetailResponse getTermsById(Long termsId) {
        Terms terms = termsRepository.findById(termsId)
                .orElseThrow(() -> new TermsException(ErrorStatus.TERMS_NOT_FOUND));
        return TermsDTO.TermsResponse.TermsDetailResponse.builder()
                .termsId(terms.getTermsId())
                .content(terms.getContent())
                .type(terms.getType())
                .build();
    }
}
