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
		, entityManagerFactoryRef = "xaMysqlEntityManagerFactory" 
		, transactionManagerRef = "multiTxManager" 
)
@EntityScan( "com.jsmile.springboot.jpajtatx.entity" )
//@EntityScan 시에
//모든 DBMS가 동일한 DB 스키마를 가졌다고 가정
//만일 DBMS 마다 DB 스키마가 다르다면 별도의 package 로 구성해야 함.
public class XaForMysqlDataSourceConfig
{
	private Logger logger = Logger.getLogger(getClass().getName());
	
	
	@Value( "${mysql.jta.unique.resource.name}" )
	private String uniqueResourceName;
	@Value( "${mysql.jta.xa.data.source.class.name}" )
	private String dataSourceClassName;
	@Value( "${mysql.jta.xa.user}" )
	private String user;
	@Value( "${mysql.jta.xa.password}" )
	private String password;
	@Value( "${mysql.jdbc.url}" )
	private String url;
	
	@Autowired
	private JpaVendorAdapter jpaVendorAdapter;
	
	@Bean( name="xaForMysqlDataSource" )
	public DataSource xaForMysqlDataSource() 
	{
		Properties props = new Properties();
		props.setProperty( "url", url );
		props.setProperty( "user", user );
		props.setProperty( "password", password );
		
		AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
		dataSource.setUniqueResourceName( uniqueResourceName );
		dataSource.setXaDataSourceClassName( dataSourceClassName );
		dataSource.setXaProperties( props );
		
		return dataSource;		
	}
	
	@Bean( name="xaMysqlEntityManagerFactory" )
	@DependsOn( "multiTxManager" )
	public LocalContainerEntityManagerFactoryBean xaMysqlEntityManagerFactory()
	{
		Properties props = new Properties();
		props.put( "hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName() );
		props.put( "javax.persistence.transactionType", "JTA" );

		LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
		entityManager.setDataSource( xaForMysqlDataSource() );
		entityManager.setJpaVendorAdapter( jpaVendorAdapter );
//		entityManager.setPackagesToScan( "kr.co.within.goodchoice.user.jta.domain.legacy" );
		entityManager.setPackagesToScan( "com.jsmile.springboot.jpajtatx.entity" );
		entityManager.setPersistenceUnitName( "mysqlEntityManager" );
		entityManager.setJpaProperties( props );

		return entityManager;
	}	



}
