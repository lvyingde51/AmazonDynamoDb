package com.yahoo.aws.AmazonDynamoDb;

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
import com.amazonaws.services.s3.model.Region;

public class CategoriesCreateTable {

	public static void main(String[] args) throws Exception {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", Region.US_West.name())).build();

		String tableName = "Categories";
		String partitionKey = "store_id";
		String sortKey = "id";
		String updateTimeIndexKey = "update_time";

		String updateTimeIndexName = "store_id-update_time-index";
		
		DynamoDB dynamoDB = new DynamoDB(client);

		// Index
		LocalSecondaryIndex updateTimeIdIndex = new LocalSecondaryIndex().withIndexName(updateTimeIndexName)
				.withProjection(new Projection().withProjectionType(ProjectionType.ALL));
		ArrayList<KeySchemaElement> updateTimeIndexKeySchema = new ArrayList<KeySchemaElement>();
		updateTimeIndexKeySchema.add(new KeySchemaElement().withAttributeName(partitionKey).withKeyType(KeyType.HASH));  //Partition key
		updateTimeIndexKeySchema
				.add(new KeySchemaElement().withAttributeName(updateTimeIndexKey).withKeyType(KeyType.RANGE));  //Sort key
		updateTimeIdIndex.setKeySchema(updateTimeIndexKeySchema);

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
		attributeDefinitions.add(new AttributeDefinition().withAttributeName(updateTimeIndexKey)
				.withAttributeType(ScalarAttributeType.N));

		CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
				.withProvisionedThroughput(
						new ProvisionedThroughput().withReadCapacityUnits((long) 5).withWriteCapacityUnits((long) 1))
				.withAttributeDefinitions(attributeDefinitions).withKeySchema(tableKeySchema)
				.withLocalSecondaryIndexes(updateTimeIdIndex);

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
