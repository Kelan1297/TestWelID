package com.example.testmicro;

import com.example.testmicro.configuration.WebConfig;
import com.example.testmicro.utils.Configurazione;
import com.example.testmicro.utils.CostantiTest;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static org.springframework.util.ResourceUtils.getFile;

@SpringBootApplication
public class TestMicroApplication {
	private static final Logger logger = LogManager.getLogger(TestMicroApplication.class);
	private static final String APP_NAME = "TestMicro";
	private static final String DEFAULT_CONFIG_PATH = "./etc/"; //windows

	public static void main(String[] args) throws FileNotFoundException {
		logger.info("Avvio applicativo " + APP_NAME);
		String propConfigFile = args != null && args.length > 0 ? args[0] : DEFAULT_CONFIG_PATH + APP_NAME + ".properties";
		logger.info("File di configurazione properties: " + propConfigFile);
		if (!initProperties(propConfigFile)) {
			logger.error("Avvio interrotto per problema di lettura del file di properties");
			return;
		}
		String serverPort = args != null && args.length > 3 ? args[3] : Configurazione.INSTANCE.getString(CostantiTest.SERVER_PORT);
		logger.info("Porta di ascolto del servizio: " + serverPort);
		SpringApplication application = new SpringApplication(TestMicroApplication.class);
		//proprietà che spring prende dall'esterno
		Map<String, Object> startupProperties = new HashMap<String, Object>();
		startupProperties.put("server.port", serverPort);
		// tutte le rotte avranno come base http://ip:porta/APP_NAME/ ... in questo modo nei controller andra' definita solo l'ultima parte (es. hello-world), vedi BaseController
		startupProperties.put("server.servlet.context-path", "/" + APP_NAME + "/");
		String logConfigFile = args != null && args.length > 1 ? args[1] : DEFAULT_CONFIG_PATH + APP_NAME + "_log.xml";
		logger.info("File di configurazione log: " + logConfigFile);
		if (!initLog(logConfigFile)) {
			logger.error("Avvio interrotto per problema di lettura del file di configurazione log");
			return;
		}
		application.run(args);
		logger.info("Applicativo avviato");
	}
	private static boolean initLog(String path) throws FileNotFoundException {
		File file = getFile(path);
		if (file != null) {
			LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
			context.setConfigLocation(file.toURI());
			logger.info("LOG4J INIZIALIZZATO....");
			logger.debug("debug level ok");
		}
		return file != null;
	}
	private static boolean initProperties(String path) throws FileNotFoundException {
		Configuration config = getPropertiesConfiguration(path);
		if (config != null) {
			for (Iterator<?> iter = config.getKeys(); iter.hasNext();) {
				Object key = iter.next();
				Configurazione.INSTANCE.setProperty((String) key, config.getProperty((String) key));
				logger.info("Property: KEY[" + key + "] VALUE[" + config.getProperty((String) key) + "]");
			}
		}
		return config != null;
	}
	private static Configuration getPropertiesConfiguration(String path) throws FileNotFoundException {
		Configuration config = null;
		File file = getFile(path);
		if (file != null) {
			config = new PropertiesConfiguration();
		}
		return config;
	}
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebConfig();
	}

}
