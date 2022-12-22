package com.synechrone.users.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.synechrone.users.dto.DepartmentDto;

@FeignClient(value = "DEPARTMENT-SERVICE", url = "http://localhost:8081")
public interface APIClient {
    @GetMapping(value = "/api/departments/by_department_id/{id}")
    DepartmentDto getDepartmentById(@PathVariable("id") String departmentId);
    
    @PostMapping(value = "/api/departments/")
    DepartmentDto saveDepartment(@RequestBody DepartmentDto department);
}
