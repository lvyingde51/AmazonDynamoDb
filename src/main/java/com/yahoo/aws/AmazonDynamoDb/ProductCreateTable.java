package com.yahoo.aws.AmazonDynamoDb;

import java.util.Arrays;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

public class ProductCreateTable {
	 public static void main(String[] args) throws Exception {

	     AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
	         .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
	         .build();

	     DynamoDB dynamoDB = new DynamoDB(client);

	     String tableName = "PRODUCT";

	     try {
	         System.out.println("Attempting to create table; please wait...");
	         Table table = dynamoDB.createTable(tableName,
	                 Arrays.asList(new KeySchemaElement("store_id", KeyType.HASH), // Partition
	                                                                           // key
	                     new KeySchemaElement("id", KeyType.RANGE)), // Sort key
	                 Arrays.asList(new AttributeDefinition("store_id", ScalarAttributeType.S),
	                     new AttributeDefinition("id", ScalarAttributeType.S)),
	                 new ProvisionedThroughput(10L, 10L));
	         table.waitForActive();
	         System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

	     }
	     catch (Exception e) {
	         System.err.println("Unable to create table: ");
	         System.err.println(e.getMessage());
	     }

	 }
}
