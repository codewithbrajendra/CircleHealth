package com.circlesHealth.admin.dto.mappers;

import com.circlesHealth.admin.dto.UserDto;
import com.circlesHealth.admin.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    public static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    public abstract UserDto getEntityToDto(User user);
    
    public abstract UserDto getEntityToPatientDto(User user);
}
