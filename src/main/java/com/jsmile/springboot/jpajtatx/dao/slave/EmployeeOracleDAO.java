package com.jsmile.springboot.jpajtatx.dao.slave;

import java.util.List;

import com.jsmile.springboot.jpajtatx.entity.sequence.Employee;

public interface EmployeeOracleDAO {

	public List<Employee> findAll();
	
	public Employee findById(int theId);
	
	public void save(Employee theEmployee);
	
	public void deleteById(int theId);
	
}
