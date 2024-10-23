package com.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{
	
	@Query("Select e from Employee e WHERE e.fullName LIKE CONCAT('%', :name, '%')")
	public List<Employee> search(String name);
	
}
