package com.demo.system.mapper;

import com.demo.system.model.User;
import com.demo.system.to.UserTo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserTo> {

    @Mapping(target = "email", expression = "java(to.getEmail().toLowerCase())")
    @Override
    User toEntity(UserTo to);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", expression = "java(to.getEmail().toLowerCase())")
    @Override
    User updateFromTo(@MappingTarget User entity, UserTo to);
}
