package com.synechrone.users.controller;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synechrone.users.dto.DepartmentDto;
import com.synechrone.users.dto.ResponseDto;
import com.synechrone.users.dto.UserDto;
import com.synechrone.users.entity.User;
import com.synechrone.users.service.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserController {
	@Autowired
	private UserService userService;
	 public static final String USER_SERVICE="userService1";
    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user){
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    //@Retry(name = USER_SERVICE,fallbackMethod = "getAllAvailableUsers")
    @CircuitBreaker(name =USER_SERVICE,fallbackMethod = "getAllAvailableUsers")
    public ResponseEntity<ResponseDto> getUser(@PathVariable("id") String userId){
        ResponseDto responseDto = userService.getUser(userId);
        return ResponseEntity.ok(responseDto);
    }
    
    public ResponseEntity<ResponseDto> getAllAvailableUsers(String id, Exception e){
    	System.out.println("---------Department service is unavailable-------------");
    	ResponseDto dto = new ResponseDto();
    	DepartmentDto department = new DepartmentDto();
        UserDto user = new UserDto();
        dto.setDepartment(department);
        dto.setUser(user);
       
        return  ResponseEntity.ok(dto);
    }
}
