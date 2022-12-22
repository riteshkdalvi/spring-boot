package com.synechrone.users.service;

import com.synechrone.users.dto.ResponseDto;
import com.synechrone.users.entity.User;

public interface UserService {
	User saveUser(User user);

    ResponseDto getUser(String userId);
}
