package com.jsmile.springboot.jpajtatx.config;

import java.util.logging.Logger;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration 
@ComponentScan 
@EnableTransactionManagement
public class XaDataManagerConfig
{
	private Logger log = Logger.getLogger(getClass().getName());
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() 
	{
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setShowSql( true );
		hibernateJpaVendorAdapter.setGenerateDdl( false );
		hibernateJpaVendorAdapter.setDatabase( Database.MYSQL);
		
		return hibernateJpaVendorAdapter;
	}
	
	@Bean( name="userTransaction" )
	public UserTransaction userTransaction() throws Throwable 
	{
		log.info( "\n############### userTransaction ####################" );
		
		UserTransactionImp userTransactionImp = new UserTransactionImp();
		userTransactionImp.setTransactionTimeout( 10000 );
		
		return userTransactionImp; 
	}
	
	@Bean(name = "atomikosTransactionManager", initMethod = "init", destroyMethod = "close")
	public TransactionManager atomikosTransactionManager() throws Throwable 
	{ 
		log.info( "\n############### atomikosTransactionManager ####################" );
		
		UserTransactionManager userTransactionManager = new UserTransactionManager(); 
		userTransactionManager.setForceShutdown(false); 
		
		AtomikosJtaPlatform.transactionManager = userTransactionManager; 
		
		return userTransactionManager; 
	} 

	@Bean(name = "multiTxManager") 
	@DependsOn({"userTransaction", "atomikosTransactionManager"}) 
	public PlatformTransactionManager transactionManager() throws Throwable 
	{ 
		log.info( "\n############### multiTxManager ####################" );
		
		UserTransaction userTransaction = userTransaction(); 
		AtomikosJtaPlatform.transaction = userTransaction; 
		JtaTransactionManager jtaTxManager = 
				new JtaTransactionManager(userTransaction, atomikosTransactionManager()); 
		
		return jtaTxManager; 
	}		
	
}
