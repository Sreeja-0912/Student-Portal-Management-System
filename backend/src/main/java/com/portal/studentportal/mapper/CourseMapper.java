package com.portal.studentportal.mapper;

import com.portal.studentportal.dto.course.CourseRequest;
import com.portal.studentportal.dto.course.CourseResponse;
import com.portal.studentportal.entity.Course;
import org.mapstruct.*;


@Mapper(componentModel = "spring",builder = @Builder(disableBuilder = true))
public interface CourseMapper {
    CourseResponse toResponse(Course course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Course toEntity(CourseRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void update(CourseRequest request, @MappingTarget Course course);
}
