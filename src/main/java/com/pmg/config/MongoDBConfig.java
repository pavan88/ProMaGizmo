package com.pmg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
public class MongoDBConfig extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return "pmgdb";
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		Mongo mongo = new MongoClient("localhost", 27017);
		return mongo;
	}

	@Override
	protected String getMappingBasePackage() {

		return "com.pmg.model";
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(this.mongo(), this.getDatabaseName());
		System.out.println("****************************");
		System.out.println("Returning the Mongo Bean" + mongoTemplate.getDb());
		System.out.println("****************************");
		return mongoTemplate;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
		
		return new PersistenceExceptionTranslationPostProcessor();
	}

}
