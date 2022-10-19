package com.spree;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Retrieve_An_Account {
	String outh_token;

	@SuppressWarnings("unchecked")
	@BeforeTest
	public void oAuth_Token() {

		RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("grant_type", "password");
		requestParams.put("username", "farid@spree.com");
		requestParams.put("password", "selenium");

		request.header("Content-Type", "application/json");
		request.body(requestParams.toJSONString());

		Response response = request.post("/spree_oauth/token");

		response.prettyPrint();
		int statusCode = response.getStatusCode();

		Assert.assertEquals(statusCode, 200);

		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		outh_token = jsonPathEvaluator.get("access_token").toString();
		System.out.println("oAuth Token is =>  " + outh_token);

	}

	@Test
	public void retrieve_An_Account() {
		RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification request = RestAssured.given();

		request.header("Authorization", "Bearer " + outh_token);

		Response response = request.get("/api/v2/storefront/account");
		String responsebody = response.getBody().prettyPrint();
		System.out.println("body -------> " + responsebody);
	}
}
