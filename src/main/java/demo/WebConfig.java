package demo;

import java.util.Locale;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@EnableSpringDataWebSupport
@Configuration
public class WebConfig extends WebMvcAutoConfigurationAdapter {
	
	@Bean
	@Override
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(new Locale("pt-BR"));
		return slr;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
		super.addInterceptors(registry);
	}
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource srbms = new ReloadableResourceBundleMessageSource();
		srbms.setDefaultEncoding("UTF-8");
		srbms.setBasenames("classpath:org/springframework/security/messages",
						   "classpath:org/hibernate/validator/ValidationMessages",
						   "classpath:/messagesApp/messages",
						   "classpath:/messagesSecurity/messages",
						   "classpath:/messages/messages");
		return srbms;
	}

	
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(mappingJackson2HttpMessageConverter());
//        super.configureMessageConverters(converters);
//    }

//    @Bean
//    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.setObjectMapper(jacksonBuilder().build());
//        return converter;
//    }
    
//	private Jackson2ObjectMapperBuilder jacksonBuilder() {
//        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
//        builder
//        	.indentOutput(true)
//        	.dateFormat(new SimpleDateFormat("dd/MM/yyyy"))
//        	.modulesToInstall(
//        			new Hibernate4Module().disable(Hibernate4Module.Feature.USE_TRANSIENT_ANNOTATION),
//                    new AfterburnerModule(),
//                    new JavaTimeModule(),
//                    new Jdk8Module(),
//                    new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
//        return builder;
//    }
//	
//	@Bean
//	public MVDialect mvDialect() {
//		return new MVDialect();
//	}

}