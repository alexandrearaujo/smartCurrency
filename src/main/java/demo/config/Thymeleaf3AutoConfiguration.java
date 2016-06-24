package demo.config;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import javax.annotation.PostConstruct;
import javax.servlet.Servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.template.TemplateLocation;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.MimeType;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableConfigurationProperties(ThymeleafProperties.class)
@ConditionalOnClass(SpringTemplateEngine.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@Slf4j
public class Thymeleaf3AutoConfiguration {
	
	@Configuration
	@ConditionalOnMissingBean(name = "defaultTemplateResolver")
	public static class DefaultTemplateResolverConfiguration {

		@Autowired
		private ThymeleafProperties properties;

		@Autowired
		private ApplicationContext applicationContext;
		

		@PostConstruct
		public void checkTemplateLocationExists() {
			boolean checkTemplateLocation = this.properties.isCheckTemplateLocation();
			if (checkTemplateLocation) {
				TemplateLocation location = new TemplateLocation(
						this.properties.getPrefix());
				if (!location.exists(this.applicationContext)) {
					log.warn("Cannot find template location: " + location
							+ " (please add some templates or check "
							+ "your Thymeleaf configuration)");
				}
			}
		}

		@Bean
	    public ITemplateResolver defaultTemplateResolver() {
	        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
	        resolver.setApplicationContext(applicationContext);
	        resolver.setPrefix("/WEB-INF/templates/");
	        resolver.setTemplateMode(TemplateMode.HTML);
//	        resolver.setResourceResolver(thymeleafResourceResolver());
//	        return resolver;
	        
	        
			resolver.setPrefix(this.properties.getPrefix());
			resolver.setSuffix(this.properties.getSuffix());
//			resolver.setTemplateMode(this.properties.getMode());
			if (this.properties.getEncoding() != null) {
				resolver.setCharacterEncoding(this.properties.getEncoding().name());
			}
			resolver.setCacheable(this.properties.isCache());
			Integer order = this.properties.getTemplateResolverOrder();
			if (order != null) {
				resolver.setOrder(order);
			}
			return resolver;
	    }
		
//		@Bean
//		public ITemplateResolver cssTemplateResolver() {
//	        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
//	        resolver.setApplicationContext(applicationContext);
//	        resolver.setPrefix("/WEB-INF/css/");
//	        resolver.setTemplateMode(TemplateMode.CSS);
//	        return resolver;
//	    }
//
//		@Bean
//		public ITemplateResolver javascriptTemplateResolver() {
//	        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
//	        resolver.setApplicationContext(applicationContext);
//	        resolver.setPrefix("/WEB-INF/js/");
//	        resolver.setTemplateMode(TemplateMode.JAVASCRIPT);
//	        return resolver;
//	    }

//		@Bean
//		public SpringResourceTemplateResolver thymeleafResourceResolver() {
//			return new SpringResourceTemplateResolver();
//		}
	}

	@Configuration
	@ConditionalOnMissingBean(SpringTemplateEngine.class)
	protected static class ThymeleafDefaultConfiguration {

		@Autowired
		private final Collection<ITemplateResolver> templateResolvers = Collections
				.emptySet();

		@Autowired(required = false)
		private final Collection<IDialect> dialects = Collections.emptySet();

		@Bean
		public SpringTemplateEngine templateEngine() {
			SpringTemplateEngine engine = new SpringTemplateEngine();
			for (ITemplateResolver templateResolver : this.templateResolvers) {
				engine.addTemplateResolver(templateResolver);
			}
			for (IDialect dialect : this.dialects) {
				engine.addDialect(dialect);
			}
			return engine;
		}
		
	}

//	@Configuration
//	@ConditionalOnClass(name = "nz.net.ultraq.thymeleaf.LayoutDialect")
//	protected static class ThymeleafWebLayoutConfiguration {
//
//		@Bean
//		@ConditionalOnMissingBean
//		public LayoutDialect layoutDialect() {
//			return new LayoutDialect();
//		}
//
//	}

//	@Configuration
//	@ConditionalOnClass(DataAttributeDialect.class)
//	protected static class DataAttributeDialectConfiguration {
//
//		@Bean
//		@ConditionalOnMissingBean
//		public DataAttributeDialect dialect() {
//			return new DataAttributeDialect();
//		}
//
//	}

//	@Configuration
//	@ConditionalOnClass({ SpringSecurityDialect.class })
//	protected static class ThymeleafSecurityDialectConfiguration {
//
//		@Bean
//		@ConditionalOnMissingBean
//		public SpringSecurityDialect securityDialect() {
//			return new SpringSecurityDialect();
//		}
//	}

//	@Configuration
//	@ConditionalOnClass(ConditionalCommentsDialect.class)
//	protected static class ThymeleafConditionalCommentsDialectConfiguration {
//
//		@Bean
//		@ConditionalOnMissingBean
//		public ConditionalCommentsDialect conditionalCommentsDialect() {
//			return new ConditionalCommentsDialect();
//		}
//	}
	
	@Configuration
	@ConditionalOnClass(Java8TimeDialect.class)
	protected static class Java8TimeDialectConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public Java8TimeDialect dialect() {
			return new Java8TimeDialect();
		}

	}

	@Configuration
	@ConditionalOnClass({ Servlet.class })
	@ConditionalOnWebApplication
	protected static class ThymeleafViewResolverConfiguration {

		@Autowired
		private ThymeleafProperties properties;

		@Autowired
		private SpringTemplateEngine templateEngine;

		@Bean
		@ConditionalOnMissingBean(name = "thymeleafViewResolver")
		@ConditionalOnProperty(name = "spring.thymeleaf.enabled", matchIfMissing = true)
		public ThymeleafViewResolver thymeleafViewResolver() {
			ThymeleafViewResolver resolver = new ThymeleafViewResolver();
			resolver.setTemplateEngine(this.templateEngine);
			resolver.setCharacterEncoding(this.properties.getEncoding().name());
			resolver.setContentType(appendCharset(this.properties.getContentType(),
					resolver.getCharacterEncoding()));
			resolver.setExcludedViewNames(this.properties.getExcludedViewNames());
			resolver.setViewNames(this.properties.getViewNames());
			// This resolver acts as a fallback resolver (e.g. like a
			// InternalResourceViewResolver) so it needs to have low precedence
			resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 5);
			resolver.setCache(this.properties.isCache());
			return resolver;
		}

		private String appendCharset(MimeType type, String charset) {
			if (type.getCharSet() != null) {
				return type.toString();
			}
			LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
			parameters.put("charset", charset);
			parameters.putAll(type.getParameters());
			return new MimeType(type, parameters).toString();
		}
		
//		@Bean
//	    public ThymeleafViewResolver cssViewResolver() {
//	        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
//	        resolver.setTemplateEngine(templateEngine(cssTemplateResolver()));
//	        resolver.setContentType("text/css");
//	        resolver.setCharacterEncoding(this.properties.getEncoding().name());
//	        String[] viewName = {"*.css"};
//	        
//	        resolver.setViewNames(viewName);
//	        return resolver;
//	    }
//
//	    @Bean
//	    public ThymeleafViewResolver javascriptViewResolver() {
//	        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
//	        resolver.setTemplateEngine(templateEngine(javascriptTemplateResolver()));
//	        resolver.setContentType("application/javascript");
//	        resolver.setCharacterEncoding(this.properties.getEncoding().name());
//	        String[] viewName = {"*.js"};
//	        
//	        resolver.setViewNames(viewName);
//	        return resolver;
//	    }
//	    
//	    private SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
//	        SpringTemplateEngine engine = new SpringTemplateEngine();
//	        engine.setTemplateResolver(templateResolver);
//	        return engine;
//	    }

	}

	@Configuration
	@ConditionalOnWebApplication
	protected static class ThymeleafResourceHandlingConfig {

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnEnabledResourceChain
		public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
			return new ResourceUrlEncodingFilter();
		}

	}

}
