package com.apple.slack.status;

import com.apple.slack.status.properties.SlackAppProperties;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.time.Duration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableConfigurationProperties(value = {SlackAppProperties.class})
public class SlackStatusApplication {

	@Autowired
	private SlackAppProperties slackAppProperties;

	public static void main(String[] args) {
		SpringApplication.run(SlackStatusApplication.class, args);
	}

	@Bean
	public App getSlackApp() {
		AppConfig appConfig = new AppConfig();
		appConfig.setClientId(slackAppProperties.getClientId());
		appConfig.setClientSecret(slackAppProperties.getClientSecret());
		appConfig.setSingleTeamBotToken(slackAppProperties.getBotToken());
		appConfig.setSigningSecret(slackAppProperties.getSigningSecret());
		return new App(appConfig);
	}

	@Bean
	public MongoDatabase mongoDatabase(final Environment environment) {
		final String mongoDbUri = environment.getProperty("spring.data.mongodb.url", "");
		final String databaseName = environment.getProperty("spring.data.mongodb.db", "");
		final MongoClient mongoClient = MongoClients.create(mongoDbUri);
		return mongoClient.getDatabase(databaseName);
	}
}
