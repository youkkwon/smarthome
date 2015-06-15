package edu.cmu.team2.iotms.conf;

import java.beans.PropertyVetoException;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import edu.cmu.team2.iotms.application.EventHistoryService;
import edu.cmu.team2.iotms.application.EventHistoryServiceImpl;
import edu.cmu.team2.iotms.application.MessageService;
import edu.cmu.team2.iotms.application.MessageServiceImpl;
import edu.cmu.team2.iotms.application.RuleService;
import edu.cmu.team2.iotms.application.RuleServiceImpl;
import edu.cmu.team2.iotms.application.SettingService;
import edu.cmu.team2.iotms.application.SettingServiceImpl;
import edu.cmu.team2.iotms.application.UserService;
import edu.cmu.team2.iotms.application.UserServiceImpl;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "edu.cmu.team2.iotms.domain")
public class SpringAppConfig {

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		ComboPooledDataSource ds = new ComboPooledDataSource();
		try {
			ds.setDriverClass("com.mysql.jdbc.Driver");
		} catch (PropertyVetoException e) {
			throw new RuntimeException(e);
		}
		ds.setJdbcUrl("jdbc:mysql://localhost/iotmsdb?characterEncoding=utf8");
		ds.setUser("iotms");
		ds.setPassword("iotms");
		return ds;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager tm = new JpaTransactionManager();
		tm.setEntityManagerFactory(emf);
		return tm;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource());
		emf.setPackagesToScan("edu.cmu.team2.iotms.domain");
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.MYSQL);
		adapter.setShowSql(true);
		emf.setJpaVendorAdapter(adapter);
		return emf;
	}

	@Bean
	public EventHistoryService eventHistoryService() {
		EventHistoryServiceImpl service = new EventHistoryServiceImpl(dataSource());
		return service;
	}
	
	@Bean
	public MessageService messageService() {
		MessageServiceImpl service = new MessageServiceImpl(dataSource());
		return service;
	}

	@Bean
	public SettingService settingService() {
		SettingServiceImpl service = new SettingServiceImpl(dataSource());
		return service;
	}
	
	@Bean
	public UserService userService() {
		UserServiceImpl service = new UserServiceImpl(dataSource());
		return service;
	}
	
	@Bean
	public RuleService ruleService() {
		RuleServiceImpl service = new RuleServiceImpl(dataSource());
		return service;
	}
}
