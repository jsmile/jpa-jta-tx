package com.jsmile.springboot.jpajtatx.dao.master;

import java.util.List;

import com.jsmile.springboot.jpajtatx.entity.identity.Employee;

public interface EmployeeMariadbDAO {

	public List<Employee> findAll();
	
	public Employee findById(int theId);
	
	public void save(Employee theEmployee);
	
	public void deleteById(int theId);
	
}
