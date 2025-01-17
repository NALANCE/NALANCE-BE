package nalance.backend.domain.terms.service;

import static nalance.backend.domain.terms.dto.TermsDTO.TermsResponse.*;

import java.util.List;

public interface TermsQueryService {
    List<TermsDetailResponse> getAllTerms();
    TermsDetailResponse getTermsById(Long termsId);
}
