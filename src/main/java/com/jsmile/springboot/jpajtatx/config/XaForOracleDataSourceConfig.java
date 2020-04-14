package com.jsmile.springboot.jpajtatx.config;

import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.atomikos.jdbc.AtomikosDataSourceBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration 
@DependsOn("multiTxManager") 
@EnableTransactionManagement 
@EnableJpaAuditing 
@EnableJpaRepositories( 
		basePackages = {"com.jsmile.springboot.jpajtatx.dao.slave"} 
		, entityManagerFactoryRef = "xaOracleEntityManagerFactory" 
		, transactionManagerRef = "multiTxManager" 
)
@EntityScan( "com.jsmile.springboot.jpajtatx.entity" )
//@EntityScan 시에
//모든 DBMS가 동일한 DB 스키마를 가졌다고 가정
//만일 DBMS 마다 DB 스키마가 다르다면 별도의 package 로 구성해야 함.
public class XaForOracleDataSourceConfig
{
	private Logger logger = Logger.getLogger(getClass().getName());
	
	
	@Value( "${oracle.jta.unique.resource.name}" )
	private String uniqueResourceName;
	@Value( "${oracle.jta.xa.data.source.class.name}" )
	private String dataSourceClassName;
	@Value( "${oracle.jta.xa.user}" )
	private String user;
	@Value( "${oracle.jta.xa.password}" )
	private String password;
	@Value( "${oracle.jdbc.url}" )
	private String url;
	
	@Autowired
	private JpaVendorAdapter jpaVendorAdapter;
	
	@Bean( name="xaForOracleDataSource" )
	public DataSource xaForOracleDataSource() 
	{
		Properties props = new Properties();
		props.setProperty( "URL", url );
		props.setProperty( "user", user );
		props.setProperty( "password", password );
		
		AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
		dataSource.setUniqueResourceName( uniqueResourceName );
		dataSource.setXaDataSourceClassName( dataSourceClassName );
		dataSource.setXaProperties( props );
		
		return dataSource;		
	}
	
	@Bean( name="xaOracleEntityManagerFactory" )
	@DependsOn( "multiTxManager" )
	public LocalContainerEntityManagerFactoryBean xaOracleEntityManagerFactory()
	{
		Properties props = new Properties();
		props.put( "hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName() );
		props.put( "javax.persistence.transactionType", "JTA" );

		LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
		entityManager.setDataSource( xaForOracleDataSource() );
		entityManager.setJpaVendorAdapter( jpaVendorAdapter );
//		entityManager.setPackagesToScan( "kr.co.within.goodchoice.user.jta.domain.legacy" );
		entityManager.setPackagesToScan( "com.jsmile.springboot.jpajtatx.entity" );
		entityManager.setPersistenceUnitName( "oracleEntityManager" );
		entityManager.setJpaProperties( props );

		return entityManager;
	}	



}
