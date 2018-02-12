package com.yahoo.aws.AmazonDynamoDb;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;

public class StoreSettingsTableGetItem {

	public static void main(String[] args) throws Exception {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2")).build();

		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("StoreSettings");
		//Table table = dynamoDB.getTable("qa-StoreSettings");


		PrimaryKey key = new PrimaryKey("id", 1);

		try {
			System.out.println("Attempting to read the item...");
			Item outcome = table.getItem(key);
			System.out.println("GetItem succeeded: " + outcome.toJSONPretty());

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
}