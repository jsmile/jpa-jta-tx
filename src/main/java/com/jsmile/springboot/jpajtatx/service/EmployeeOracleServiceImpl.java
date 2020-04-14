package com.jsmile.springboot.jpajtatx.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jsmile.springboot.jpajtatx.dao.master.EmployeeMariadbDAO;
import com.jsmile.springboot.jpajtatx.dao.slave.EmployeeMysqlDAO;
import com.jsmile.springboot.jpajtatx.dao.slave.EmployeeOracleDAO;
import com.jsmile.springboot.jpajtatx.entity.Employee;

@Service
public class EmployeeOracleServiceImpl implements EmployeeOracleService
{
	private Logger log = Logger.getLogger( getClass().getName() );
	
	private EmployeeOracleDAO employeeOracleDAO;

	@Autowired
	public EmployeeOracleServiceImpl( @Qualifier( "employeeOracleDAOJpaImpl" ) EmployeeOracleDAO _employeeOracleDAO )
	{
		employeeOracleDAO = _employeeOracleDAO;
	}
	
	@Override
//	@Transactional
	public List<Employee> findAll()
	{
		return employeeOracleDAO.findAll();
	}

	@Override
//	@Transactional
	public Employee findById( int theId )
	{
		return employeeOracleDAO.findById( theId );
	}

	@Override
//	@Transactional
	public void save( Employee theEmployee ) throws RuntimeException
	{
		log.info( "/n############### EmployeeOracleServiceImpl.save() ####################" );
		employeeOracleDAO.save( theEmployee );
//		throw new RuntimeException("add UserInfo fail...");
	}

	@Override
//	@Transactional
	public void deleteById( int theId )
	{
		log.info( "/n############### EmployeeOracleServiceImpl.deleteById() ####################" );
		employeeOracleDAO.deleteById( theId );
	}

}
