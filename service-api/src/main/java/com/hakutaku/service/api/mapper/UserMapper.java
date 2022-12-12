package com.hakutaku.service.api.mapper;

import com.hakutaku.service.common.model.User;

import java.util.List;

public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);

    User findByUsername(String username);

    User findById(Long id);

    List<User> findAllUser();

    int insertBatch(List<User> list);
}