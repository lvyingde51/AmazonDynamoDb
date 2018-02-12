package com.yahoo.aws.AmazonDynamoDb;
//Copyright 2012-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

import java.util.ArrayList;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

public class CatalogVariantsCreateTable {

	public static void main(String[] args) throws Exception {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2")).build();

		String tableName = "CatalogVariants";
		String partitionKey = "store_id";
		String sortKey = "itemGroupId-VariantId";
		String itemGroupIdIndexKey = "itemGroupId";
		String isProcessedIndexKey = "isProcessed";
		String createTimeIndexKey = "create_time";
		String itemGroupIdIsProcessedIndexKey = "itemGroupId_isProcessed";

		String itemGroupIdIndexName = "store_id-itemGroupId-index";
		String isProcessedIndexName = "store_id-isProcessed-index";
		String createTimeIndexName = "store_id-create_time-index";
		String itemGroupIdIsProcessedIndexName = "store_id-itemGroupId-isProcessed-index";
		
		DynamoDB dynamoDB = new DynamoDB(client);

		// Index
		LocalSecondaryIndex itemGroupIdIsProcessedIndex = new LocalSecondaryIndex().withIndexName(itemGroupIdIsProcessedIndexName)
				.withProjection(new Projection().withProjectionType(ProjectionType.ALL));
		ArrayList<KeySchemaElement> itemGroupIdIsProcessedIndexKeySchema = new ArrayList<KeySchemaElement>();
		itemGroupIdIsProcessedIndexKeySchema.add(new KeySchemaElement().withAttributeName(partitionKey).withKeyType(KeyType.HASH));  //Partition key
		itemGroupIdIsProcessedIndexKeySchema
				.add(new KeySchemaElement().withAttributeName(itemGroupIdIsProcessedIndexKey).withKeyType(KeyType.RANGE));  //Sort key
		itemGroupIdIsProcessedIndex.setKeySchema(itemGroupIdIsProcessedIndexKeySchema);

		// Index
		LocalSecondaryIndex createTimeIdIndex = new LocalSecondaryIndex().withIndexName(createTimeIndexName)
				.withProjection(new Projection().withProjectionType(ProjectionType.ALL));
		ArrayList<KeySchemaElement> createTimeIndexKeySchema = new ArrayList<KeySchemaElement>();
		createTimeIndexKeySchema.add(new KeySchemaElement().withAttributeName(partitionKey).withKeyType(KeyType.HASH));  //Partition key
		createTimeIndexKeySchema
				.add(new KeySchemaElement().withAttributeName(createTimeIndexKey).withKeyType(KeyType.RANGE));  //Sort key
		createTimeIdIndex.setKeySchema(createTimeIndexKeySchema);

		// Index
		LocalSecondaryIndex itemGroupIdIndex = new LocalSecondaryIndex().withIndexName(itemGroupIdIndexName)
				.withProjection(new Projection().withProjectionType(ProjectionType.ALL));
		ArrayList<KeySchemaElement> itemGroupIdIndexKeySchema = new ArrayList<KeySchemaElement>();
		itemGroupIdIndexKeySchema.add(new KeySchemaElement().withAttributeName(partitionKey).withKeyType(KeyType.HASH));  //Partition key
		itemGroupIdIndexKeySchema
				.add(new KeySchemaElement().withAttributeName(itemGroupIdIndexKey).withKeyType(KeyType.RANGE));  //Sort key
		itemGroupIdIndex.setKeySchema(itemGroupIdIndexKeySchema);

		// Index
		LocalSecondaryIndex isProcessedIndex = new LocalSecondaryIndex().withIndexName(isProcessedIndexName)
				.withProjection(new Projection().withProjectionType(ProjectionType.ALL));
		ArrayList<KeySchemaElement> isProcessedIndexKeySchema = new ArrayList<KeySchemaElement>();
		isProcessedIndexKeySchema.add(new KeySchemaElement().withAttributeName(partitionKey).withKeyType(KeyType.HASH));  //Partition key
		isProcessedIndexKeySchema
				.add(new KeySchemaElement().withAttributeName(isProcessedIndexKey).withKeyType(KeyType.RANGE));  //Sort key
		isProcessedIndex.setKeySchema(isProcessedIndexKeySchema);

		// Table key schema
		ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
		tableKeySchema.add(new KeySchemaElement().withAttributeName(partitionKey).withKeyType(KeyType.HASH));  //Partition key
		tableKeySchema.add(new KeySchemaElement().withAttributeName(sortKey).withKeyType(KeyType.RANGE));  //Sort key

		// Attribute definitions
		ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
		attributeDefinitions.add(
				new AttributeDefinition().withAttributeName(partitionKey).withAttributeType(ScalarAttributeType.S));
		attributeDefinitions
				.add(new AttributeDefinition().withAttributeName(sortKey).withAttributeType(ScalarAttributeType.S));
		attributeDefinitions.add(new AttributeDefinition().withAttributeName(itemGroupIdIndexKey)
				.withAttributeType(ScalarAttributeType.S));
		attributeDefinitions.add(new AttributeDefinition().withAttributeName(isProcessedIndexKey)
				.withAttributeType(ScalarAttributeType.S));
		attributeDefinitions.add(new AttributeDefinition().withAttributeName(createTimeIndexKey)
				.withAttributeType(ScalarAttributeType.N));
		attributeDefinitions.add(new AttributeDefinition().withAttributeName(itemGroupIdIsProcessedIndexKey)
				.withAttributeType(ScalarAttributeType.S));


		CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
				.withProvisionedThroughput(
						new ProvisionedThroughput().withReadCapacityUnits((long) 5).withWriteCapacityUnits((long) 1))
				.withAttributeDefinitions(attributeDefinitions).withKeySchema(tableKeySchema)
				.withLocalSecondaryIndexes(itemGroupIdIndex).withLocalSecondaryIndexes(isProcessedIndex)
				.withLocalSecondaryIndexes(createTimeIdIndex).withLocalSecondaryIndexes(itemGroupIdIsProcessedIndex);

		try {
			System.out.println("Attempting to create table; please wait...");
			Table table = dynamoDB.createTable(createTableRequest);
			System.out.println(table.getDescription());
			//table.waitForActive();
			System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

		} catch (Exception e) {
			System.err.println("Unable to create table: ");
			System.err.println(e.getMessage());
		}

	}
}