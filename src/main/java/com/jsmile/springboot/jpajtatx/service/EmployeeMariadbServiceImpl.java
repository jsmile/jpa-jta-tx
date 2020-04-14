package com.jsmile.springboot.jpajtatx.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jsmile.springboot.jpajtatx.dao.master.EmployeeMariadbDAO;
import com.jsmile.springboot.jpajtatx.entity.identity.Employee;

@Service
public class EmployeeMariadbServiceImpl implements EmployeeMariadbService
{
	private Logger log = Logger.getLogger( getClass().getName() );

	private EmployeeMariadbDAO employeeMariadbDAO;

	@Autowired
	public EmployeeMariadbServiceImpl( @Qualifier( "employeeMariadbDAOJpaImpl" ) EmployeeMariadbDAO _employeeMariadbDAO )
	{
		employeeMariadbDAO = _employeeMariadbDAO;
	}
	
	@Override
//	@Transactional
	public List<Employee> findAll()
	{
		return employeeMariadbDAO.findAll();
	}

	@Override
//	@Transactional
	public Employee findById( int theId )
	{
		return employeeMariadbDAO.findById( theId );
	}

	@Override
//	@Transactional
	public void save( Employee theEmployee )
	{
		log.info( "/n############### EmployeeMariadbServiceImpl.save() ####################" );
		employeeMariadbDAO.save( theEmployee );
	}

	@Override
//	@Transactional
	public void deleteById( int theId )
	{
		log.info( "/n############### EmployeeMariadbServiceImpl.deleteById() ####################" );
		employeeMariadbDAO.deleteById( theId );
	}

}
