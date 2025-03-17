package com.example.testmicro.configuration;

import com.example.testmicro.utils.Configurazione;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
//@ComponentScan(basePackageClasses = {WebConfig.class})
public class WebConfig implements WebMvcConfigurer {
	private static final Logger logger = LogManager.getLogger(WebConfig.class);

	public WebMvcConfigurer corsConfigurer() {
		return new WebConfig() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				logger.info("Inizializzo configurazione CORS");
				String allowedDomainString = Configurazione.INSTANCE.getString("service.allowedDomain", "").replace("[", "").replace("]", "").replace(" ", "");
				if (!"".equals(allowedDomainString)) {
					String[] allowedDomain = allowedDomainString.split(",");
					registry.addMapping("/**")
							.allowedOrigins(allowedDomain)
							.allowedMethods("GET", "POST")
							.allowCredentials(false).maxAge(3600);
					logger.info("CORS configurato con successo per questi domini [" + allowedDomainString + "]");
				} else {
					logger.info("Non è stato configurato nessun dominio CORS da abilitare");
				}
			}
		};
	}
}
