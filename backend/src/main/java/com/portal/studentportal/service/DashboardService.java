package com.portal.studentportal.service;

import com.portal.studentportal.dto.dashboard.AtRiskStudentResponse;
import com.portal.studentportal.dto.dashboard.BandStatisticResponse;
import com.portal.studentportal.dto.dashboard.SummaryResponse;
import com.portal.studentportal.dto.marks.RankingResponse;

import java.util.List;

public interface DashboardService {
    SummaryResponse summary();
    List<RankingResponse> topStudents(int limit);
    List<BandStatisticResponse> attendanceStatistics();
    List<AtRiskStudentResponse> atRiskStudents();
}
