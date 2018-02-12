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

public class StoreSettingsTableAddData {

 public static void main(String[] args) throws Exception {

     AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
         .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
         .build();

     DynamoDB dynamoDB = new DynamoDB(client);

     Table table = dynamoDB.getTable("StoreSettings");


     final Map<String, Object> infoMap = new HashMap<String, Object>();
     infoMap.put("store_id", "10");

     try {
         System.out.println("Adding a new item...");
         PutItemOutcome outcome = table
             .putItem(new Item().withPrimaryKey("id", "1").withString("store_id", "20"));

         System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());

     }
     catch (Exception e) {
         
         System.err.println(e.getMessage());
     }

 }
}
