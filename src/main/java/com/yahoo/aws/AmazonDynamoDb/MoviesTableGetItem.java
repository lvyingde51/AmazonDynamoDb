package com.yahoo.aws.AmazonDynamoDb;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.s3.model.Region;

public class MoviesTableGetItem {

	public static void main(String[] args) throws Exception {

	     AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
	    		 .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", Region.US_West.name()))
	         .build();

		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("Movies");
		//Table table = dynamoDB.getTable("qa-StoreSettings");

		int year = 2015;
		String title = "The Big New Movie";

		GetItemSpec spec = new GetItemSpec().withPrimaryKey("year", year, "title", title);

		try {
			System.out.println("Attempting to read the item...");
			Item outcome = table.getItem(spec);
			System.out.println("GetItem succeeded: " + outcome.toJSONPretty());

		} catch (Exception e) {
			System.err.println("Unable to read item: " + year + " " + title);
			System.err.println(e.getMessage());
		}

	}
}