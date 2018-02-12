package com.yahoo.aws.AmazonDynamoDb;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

public class CatalogVariantsGetItem {

	public static void main(String[] args) throws Exception {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2")).build();

		DynamoDB dynamoDB = new DynamoDB(client);
		String tableName = "CatalogVariants";
		String partitionKey = "store_id";
		String sortKey = "itemGroupId-VariantId";
		String itemGroupIdIndexKey = "itemGroupId";
		String isProcessedIndexKey = "isProcessed";

		Table table = dynamoDB.getTable(tableName);
		//Table table = dynamoDB.getTable("qa-StoreSettings");

		GetItemSpec spec = new GetItemSpec().withPrimaryKey(partitionKey, partitionKey, sortKey, sortKey);

		try {
			System.out.println("Attempting to read the item...");
			Item outcome = table.getItem(spec);
			System.out.println("GetItem succeeded: " + outcome.toJSONPretty());

		} catch (Exception e) {
			System.err.println("Unable to read item: " );
			System.err.println(e.getMessage());
		}

	}
}