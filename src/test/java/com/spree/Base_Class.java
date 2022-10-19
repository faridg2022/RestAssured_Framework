package com.spree;

import org.json.simple.JSONObject;
import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Base_Class {

	@SuppressWarnings("unchecked")
	public static String oAuth_Token() {

		RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("grant_type", "password");
		requestParams.put("username", "farid17@spree.com.com");
		requestParams.put("password", "spree123");

		request.header("Content-Type", "application/json");
		request.body(requestParams.toJSONString());
		Response response = request.post("/spree_oauth/token");

		int statusCode = response.getStatusCode();

		Assert.assertEquals(statusCode, 200);

		@SuppressWarnings("unused")
		String responseBody = response.getBody().asString();

		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		String outh_token = jsonPathEvaluator.get("access_token").toString();
		System.out.println("oAuth Token is =>  " + outh_token);

		return outh_token;
	}
}
