package com.yahoo.aws.AmazonDynamoDb;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.s3.model.Region;

public class CategoriesAddData {

	public static void main(String[] args) throws Exception {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(
						new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", Region.US_West.name()))
				.build();

		DynamoDB dynamoDB = new DynamoDB(client);
		String tableName = "Categories";
		String partitionKey = "store_id";
		String sortKey = "id";
		String updateTimeIndexKey = "update_time";

		Table table = dynamoDB.getTable(tableName);

		final Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put("plot", "Nothing happens at all.");
		infoMap.put("rating", 0);

		try {
			System.out.println("Adding a new item...");
			PutItemOutcome outcome = table
					.putItem(new Item().withPrimaryKey(partitionKey, partitionKey, sortKey, "Shoes")
							.withNumber("description", 3)
							.withNumber(updateTimeIndexKey, System.currentTimeMillis()));

			System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
			

		} catch (Exception e) {
			System.err.println("Unable to add item: ");
			System.err.println(e.getMessage());
		}

	}
}
