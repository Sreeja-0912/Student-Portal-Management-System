package com.portal.studentportal.mapper;

import com.portal.studentportal.dto.student.StudentRequest;
import com.portal.studentportal.dto.student.StudentResponse;
import com.portal.studentportal.entity.Student;
import org.mapstruct.*;

@Mapper(componentModel = "spring",builder = @Builder(disableBuilder = true))
public interface StudentMapper {
    @Mapping(source = "user.id", target = "userId")
    StudentResponse toResponse(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Student toEntity(StudentRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void update(StudentRequest request, @MappingTarget Student student);
}
