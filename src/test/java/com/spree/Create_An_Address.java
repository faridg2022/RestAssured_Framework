package com.spree;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Create_An_Address {

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
	public void POST_Create_Address() throws IOException, ParseException {

		RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification request = RestAssured.given();

		JSONParser jsonparser = new JSONParser();

		FileReader reader = new FileReader(".\\JSON_File\\create_address.json");

		Object obj = jsonparser.parse(reader);

		JSONObject prodjsonobj = (JSONObject) obj;

		request.header("Content-Type", "application/json");
		request.header("Authorization", "Bearer " + outh_token);
		request.body(prodjsonobj);

		Response response = request.post("/api/v2/storefront/account/addresses");
		response.prettyPrint();
		int statusCode = response.getStatusCode();

		Assert.assertEquals(statusCode, 200);

		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		String fname = jsonPathEvaluator.get("data.attributes.firstname").toString();
		System.out.println("First Name  =>  " + fname);
		Assert.assertEquals(fname, "Rest");

	}

}
