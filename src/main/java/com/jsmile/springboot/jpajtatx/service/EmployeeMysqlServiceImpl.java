package com.jsmile.springboot.jpajtatx.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jsmile.springboot.jpajtatx.dao.master.EmployeeMariadbDAO;
import com.jsmile.springboot.jpajtatx.dao.slave.EmployeeMysqlDAO;
import com.jsmile.springboot.jpajtatx.entity.Employee;

@Service
public class EmployeeMysqlServiceImpl implements EmployeeMysqlService
{
	private Logger log = Logger.getLogger( getClass().getName() );
	
	private EmployeeMysqlDAO employeeMysqlDAO;

	@Autowired
	public EmployeeMysqlServiceImpl( @Qualifier( "employeeMysqlDAOJpaImpl" ) EmployeeMysqlDAO _employeeMysqlDAO )
	{
		employeeMysqlDAO = _employeeMysqlDAO;
	}
	
	@Override
//	@Transactional
	public List<Employee> findAll()
	{
		return employeeMysqlDAO.findAll();
	}

	@Override
//	@Transactional
	public Employee findById( int theId )
	{
		return employeeMysqlDAO.findById( theId );
	}

	@Override
//	@Transactional
	public void save( Employee theEmployee ) throws RuntimeException
	{
		log.info( "/n############### EmployeeMysqlServiceImpl.save() ####################" );
		employeeMysqlDAO.save( theEmployee );
//		throw new RuntimeException("add UserInfo fail...");
	}

	@Override
//	@Transactional
	public void deleteById( int theId )
	{
		log.info( "/n############### EmployeeMysqlServiceImpl.deleteById() ####################" );
		employeeMysqlDAO.deleteById( theId );
	}

}
