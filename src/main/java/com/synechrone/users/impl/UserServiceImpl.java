package com.synechrone.users.impl;

import java.util.Collections;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.synechrone.users.dto.DepartmentDto;
import com.synechrone.users.dto.ResponseDto;
import com.synechrone.users.dto.UserDto;
import com.synechrone.users.entity.User;
import com.synechrone.users.repository.UserRepository;
import com.synechrone.users.service.APIClient;
import com.synechrone.users.service.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private WebClient webClient;
	@Autowired
	private APIClient apiClient;

	public User saveUser(User user) {
		user.setId(UUID.randomUUID().toString().split("-")[0]);
		System.out.println("-------Start--save---Feign client------------");
		DepartmentDto departmentDto = new DepartmentDto();
		departmentDto.setDepartmentAddress("Indore");
		departmentDto.setDepartmentCode("102");
		departmentDto.setDepartmentName("Admin");
		departmentDto.setId(user.getId());
		DepartmentDto departmentDtoFeign = apiClient.saveDepartment(departmentDto);
		System.out.println("-------end--save---Feign--client---------");
		return userRepository.save(user);
	}

	
	public ResponseDto getUser(String userId) {

		ResponseDto responseDto = new ResponseDto();
		User user = userRepository.findById(userId).get();
		UserDto userDto = mapToUser(user);
		// Rest client----------------
		/*
		 * ResponseEntity<DepartmentDto> responseEntity = restTemplate .getForEntity(
		 * "http://localhost:8081/api/departments/by_department_id/5f68a42c" ,
		 * DepartmentDto.class); DepartmentDto departmentDto = responseEntity.getBody();
		 * System.out.println(responseEntity.getStatusCode());
		 */

		// Rest client exchange--------------------
		System.out.println("-------Start-----RestTemplate Exchange------------");
		HttpEntity<DepartmentDto> request = new HttpEntity<DepartmentDto>(new DepartmentDto());
		ResponseEntity<DepartmentDto> response = restTemplate.exchange(
				"http://localhost:8081/api/departments/by_department_id/5f68a42c", HttpMethod.GET, request,
				DepartmentDto.class);
		DepartmentDto departmentDto = response.getBody();
		System.out.println("--------END----RestTemplate Exchange------------" + response.getStatusCode());

		System.out.println("---------Start--Web Client-------------");
		DepartmentDto departmentDto1 = webClient.get()
				.uri("http://localhost:8081/api/departments/by_department_id/5f68a42c").retrieve()
				.bodyToMono(DepartmentDto.class).block();
		System.out.println("------------Web Client------------" + departmentDto1.getDepartmentName());

		System.out.println("---------Start--Feign  Client-------------");
		DepartmentDto departmentDtoFeign = apiClient.getDepartmentById("5f68a42c");
		System.out.println("---------end--Feign  Client-------------" + departmentDtoFeign.getDepartmentName());

		responseDto.setUser(userDto);
		responseDto.setDepartment(departmentDto);

		return responseDto;
	}
	
	public ResponseDto getDefault(int productId, Throwable throwable){
		ResponseDto responseDto = new ResponseDto();
		responseDto.setDepartment(new DepartmentDto());
		responseDto.setUser(new UserDto());
		return responseDto;
    }

	private UserDto mapToUser(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		return userDto;
	}

}
