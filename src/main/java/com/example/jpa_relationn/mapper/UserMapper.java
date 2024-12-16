package com.example.jpa_relationn.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.jpa_relationn.dto.request.UserCreationRequest;
import com.example.jpa_relationn.dto.request.UserUpdateRequest;
import com.example.jpa_relationn.dto.response.UserResponse;
import com.example.jpa_relationn.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // User toUser(UserCreationRequest request);

    // source là cái ta lấy đi mapp, target: là object ta sẽ map về, ignore: là ko
    // mapping thì nó sẽ ignor đi
    // @Mapping(source = "firstName", target = "lastName", ignore = true) // định
    // nghĩa khi field của 2 đối tượng này khác nhau
    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponse1(List<User> user);

    // void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
