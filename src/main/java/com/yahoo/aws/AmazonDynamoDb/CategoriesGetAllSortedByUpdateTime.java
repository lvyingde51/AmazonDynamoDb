package com.yahoo.aws.AmazonDynamoDb;

import java.util.Iterator;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

public class CategoriesGetAllSortedByUpdateTime {

	public static void main(String[] args) throws Exception {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2")).build();

		DynamoDB dynamoDB = new DynamoDB(client);
		String tableName = "Categories";

		String createTimeIndexName = "store_id-update_time-index";

		Table table = dynamoDB.getTable(tableName);

		System.out.println("\n***********************************************************\n");
		System.out.println("Querying table " + tableName + "...");

		QuerySpec querySpec = new QuerySpec();

		Index index = table.getIndex(createTimeIndexName);

		// To Sort in Descending
//		querySpec.withKeyConditionExpression("store_id = :v_store_id").withScanIndexForward(false)
//		.withValueMap(new ValueMap().withString(":v_store_id", "store_id")).withMaxResultSize(5);

		// To Sort in Ascending
		querySpec.withKeyConditionExpression("store_id = :v_store_id").withScanIndexForward(true)
		.withValueMap(new ValueMap().withString(":v_store_id", "store_id")).withMaxResultSize(5);

		System.out.println(querySpec.getKeyConditionExpression());
		System.out.println(querySpec.getValueMap());
		System.out.println(querySpec.getMaxResultSize());
		
		ItemCollection<QueryOutcome> items = index.query(querySpec);
		Iterator<Item> iterator = items.iterator();

		System.out.println("Query: printing results...");

		while (iterator.hasNext()) {
			System.out.println(iterator.next().toJSONPretty());
		}

	}
}