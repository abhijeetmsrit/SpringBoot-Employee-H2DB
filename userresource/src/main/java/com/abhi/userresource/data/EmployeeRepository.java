package com.abhi.userresource.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abhi.userresource.model.Employee;






public interface EmployeeRepository extends JpaRepository<Employee
			, Long> {

}