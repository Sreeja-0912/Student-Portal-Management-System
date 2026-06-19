package com.portal.studentportal.mapper;

import com.portal.studentportal.dto.announcement.AnnouncementRequest;
import com.portal.studentportal.dto.announcement.AnnouncementResponse;
import com.portal.studentportal.entity.Announcement;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Builder;

@Mapper(componentModel = "spring",builder = @Builder(disableBuilder = true))
public interface AnnouncementMapper {
    AnnouncementResponse toResponse(Announcement announcement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdByName", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Announcement toEntity(AnnouncementRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdByName", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void update(AnnouncementRequest request, @MappingTarget Announcement announcement);
}
