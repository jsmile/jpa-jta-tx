package com.jsmile.springboot.jpajtatx.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jsmile.springboot.jpajtatx.entity.Employee;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TxEmployeeServiceImpl implements TxEmployeeService
{
	@Autowired
	private EmployeeMariadbService employeeMariadbService;
	@Autowired
	private EmployeeMysqlService employeeMysqlService;

	@Override
	@Transactional( readOnly = true, propagation = Propagation.REQUIRED, transactionManager = "multiTxManager" )
	public List<Employee> findAll()
	{
		return employeeMariadbService.findAll();
	}

	@Override
	@Transactional( readOnly = true, propagation = Propagation.REQUIRED, transactionManager = "multiTxManager" )
	public Employee findById( int theId )
	{
		Employee result = employeeMariadbService.findById( theId );

		Employee theEmployee = null;

		if ( result != null ) { theEmployee = result; } 
		else 
		{
			// we didn't find the employee
			throw new RuntimeException( "Did not find employee id - " + theId );
		}

		return theEmployee;
	}

	@Override
	@Transactional( readOnly = false, propagation = Propagation.REQUIRED, transactionManager = "multiTxManager" )
	public void save( Employee theEmployee ) throws RuntimeException
	{
		employeeMariadbService.save( theEmployee );
		employeeMysqlService.save( theEmployee );
	}

	@Override
	@Transactional( readOnly = false, propagation = Propagation.REQUIRED, transactionManager = "multiTxManager" )
	public void deleteById( int theId )
	{
		employeeMariadbService.deleteById( theId );
		employeeMysqlService.deleteById( theId );
	}

}
