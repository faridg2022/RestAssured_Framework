package com.spree;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Create__Update_An_Address {

	String id;

	@Test(priority = 1)
	public void POST_Create_Address() throws IOException, ParseException {

		RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification request = RestAssured.given();

		JSONParser jsonparser = new JSONParser();

		FileReader reader = new FileReader(".\\JSON_File\\create_address.json");

		Object obj = jsonparser.parse(reader);

		JSONObject prodjsonobj = (JSONObject) obj;

		request.header("Content-Type", "application/json");
		request.header("Authorization", "Bearer " + Base_Class.oAuth_Token());
		request.body(prodjsonobj);

		Response response = request.post("/api/v2/storefront/account/addresses");
		response.prettyPrint();
		int statusCode = response.getStatusCode();

		Assert.assertEquals(statusCode, 200);

		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		String fname = jsonPathEvaluator.get("data.attributes.firstname").toString();
		System.out.println("First Name  =>  " + fname);
		Assert.assertEquals(fname, "Rest");
		id = jsonPathEvaluator.get("data.id").toString();
		System.out.println(id);

	}

	@Test(priority = 2)
	public void updateAddress() throws Exception {
		RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification request = RestAssured.given();

		JSONParser jsonparser = new JSONParser();

		FileReader reader = new FileReader(".\\JSON_File\\update_address.json");

		Object obj = jsonparser.parse(reader);

		JSONObject prodjsonobj = (JSONObject) obj;

		request.header("Content-Type", "application/json");
		request.header("Authorization", "Bearer " + Base_Class.oAuth_Token());
		request.body(prodjsonobj);

		Response response = request.patch("/api/v2/storefront/account/addresses/" + id);

		response.prettyPrint();
		int statusCode = response.getStatusCode();

		Assert.assertEquals(statusCode, 200);
	}

}
