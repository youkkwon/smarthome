package edu.cmu.team2.iotms.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import edu.cmu.team2.iotms.uicontroller.EventHistoryController;
import edu.cmu.team2.iotms.uicontroller.MessageController;
import edu.cmu.team2.iotms.uicontroller.NodeController;
import edu.cmu.team2.iotms.uicontroller.RuleController;
import edu.cmu.team2.iotms.uicontroller.SettingController;
import edu.cmu.team2.iotms.uicontroller.UserController;

@Configuration
@EnableWebMvc
@ImportResource("classpath:ws-config.xml")
public class SpringMvcConfig extends WebMvcConfigurerAdapter {


	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver result = new InternalResourceViewResolver();
		result.setPrefix("/WEB-INF/view/");
		result.setSuffix(".jsp");
		return result;
	}

	@Bean
	public EventHistoryController eventHistoryController() {
		EventHistoryController controller = new EventHistoryController();
		return controller;
	}
	
	@Bean
	public MessageController messageController() {
		MessageController controller = new MessageController();
		return controller;
	}
	
	@Bean
	public SettingController settingController() {
		SettingController controller = new SettingController();
		return controller;
	}
	
	@Bean
	public UserController userController() {
		UserController controller = new UserController();
		return controller;
	}
	
	@Bean
	public RuleController ruleController() {
		RuleController controller = new RuleController();
		return controller;
	}
	
	@Bean
	public NodeController nodeController() {
		NodeController controller = new NodeController();
		return controller;
	}
}
