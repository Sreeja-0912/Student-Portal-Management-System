package com.portal.studentportal.mapper;

import com.portal.studentportal.dto.studymaterial.StudyMaterialResponse;
import com.portal.studentportal.entity.StudyMaterial;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",builder = @Builder(disableBuilder = true))
public interface StudyMaterialMapper {
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.courseCode", target = "courseCode")
    @Mapping(source = "course.courseName", target = "courseName")
    StudyMaterialResponse toResponse(StudyMaterial material);
}
