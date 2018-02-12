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
 * Query an item by partition key and index key along with where condition (another index key).
 * 
 */
public class CatalogVariantsGetItemByLocalIndex2 {

	public static void main(String[] args) throws Exception {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2")).build();

		DynamoDB dynamoDB = new DynamoDB(client);
		String tableName = "CatalogVariants";

		String itemGroupIdIsProcessedIndexName = "store_id-itemGroupId-isProcessed-index";

		Table table = dynamoDB.getTable(tableName);

		System.out.println("\n***********************************************************\n");
		System.out.println("Querying table " + tableName + "...");

		QuerySpec querySpec = new QuerySpec();

		Index index = table.getIndex(itemGroupIdIsProcessedIndexName);

		querySpec
				.withKeyConditionExpression(
						"store_id = :v_store_id and itemGroupId_isProcessed = :v_key")
				.withValueMap(new ValueMap().withString(":v_store_id", "store_id1")
						.withString(":v_key", "item-false")).withMaxResultSize(1);

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