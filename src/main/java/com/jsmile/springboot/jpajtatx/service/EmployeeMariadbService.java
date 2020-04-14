package com.jsmile.springboot.jpajtatx.service;

import java.util.List;

import com.jsmile.springboot.jpajtatx.entity.identity.Employee;

public interface EmployeeMariadbService {

	public List<Employee> findAll();
	
	public Employee findById(int theId);
	
	public void save(Employee theEmployee);
	
	public void deleteById(int theId);
	
}
