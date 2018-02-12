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

public class CatalogVariantsAddData {

	public static void main(String[] args) throws Exception {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2")).build();

		DynamoDB dynamoDB = new DynamoDB(client);
		String tableName = "CatalogVariants";
		String partitionKey = "store_id";
		String sortKey = "itemGroupId-VariantId";
		String itemGroupIdIndexKey = "itemGroupId";
		String isProcessedIndexKey = "isProcessed";
		String create_timeIndexKey = "create_time";
		String itemGroupIdIsProcessedIndexKey = "itemGroupId_isProcessed";

		Table table = dynamoDB.getTable(tableName);

		final Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put("plot", "Nothing happens at all.");
		infoMap.put("rating", 0);

		try {
			System.out.println("Adding a new item...");
			PutItemOutcome outcome = table.putItem(new Item()
					.withPrimaryKey(partitionKey, partitionKey + 1, sortKey, sortKey + 1 + System.currentTimeMillis())
					.withString(itemGroupIdIndexKey, itemGroupIdIndexKey + 1)//.withString(isProcessedIndexKey, "true")
					.withString(itemGroupIdIsProcessedIndexKey, "item-true"));
			//.withNumber(create_timeIndexKey, System.currentTimeMillis()));

			System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());

		} catch (Exception e) {
			System.err.println("Unable to add item: ");
			System.err.println(e.getMessage());
		}

	}
}
