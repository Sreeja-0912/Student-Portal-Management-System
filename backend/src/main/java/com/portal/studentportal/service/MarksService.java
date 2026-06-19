package com.portal.studentportal.service;

import com.portal.studentportal.dto.marks.MarksRequest;
import com.portal.studentportal.dto.marks.MarksResponse;
import com.portal.studentportal.dto.marks.RankingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MarksService {
    MarksResponse record(MarksRequest request);
    MarksResponse update(Long id, MarksRequest request);
    void delete(Long id);
    List<MarksResponse> byStudent(Long studentId);
    Page<RankingResponse> rankings(Pageable pageable);
}
