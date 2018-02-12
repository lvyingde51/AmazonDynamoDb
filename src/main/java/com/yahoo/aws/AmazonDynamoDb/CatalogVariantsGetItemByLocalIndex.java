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
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnConsumedCapacity;

/**
 * Query an item by partition key and index key. Along with that pass pagination details. 
 *
 */
public class CatalogVariantsGetItemByLocalIndex {

	public static void main(String[] args) throws Exception {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2")).build();

		DynamoDB dynamoDB = new DynamoDB(client);
		String tableName = "CatalogVariants";

		String createTimeIndexName = "store_id-create_time-index";

		Table table = dynamoDB.getTable(tableName);

		System.out.println("\n***********************************************************\n");
		System.out.println("Querying table " + tableName + "...");

		QuerySpec querySpec = new QuerySpec();

		Index index = table.getIndex(createTimeIndexName);

		querySpec.withKeyConditionExpression("store_id = :v_store_id and create_time <= :v_create_time")
		.withValueMap(new ValueMap().withString(":v_store_id", "store_id2").withNumber(":v_create_time",
				System.currentTimeMillis())).withMaxResultSize(1);

		//querySpec.withProjectionExpression("OrderCreationDate, ProductCategory, ProductName, OrderStatus");
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