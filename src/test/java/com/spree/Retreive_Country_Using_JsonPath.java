package com.spree;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Retreive_Country_Using_JsonPath {
	@Test

	public void Get_Default_Country() {

		RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification httpRequest = RestAssured.given();

		Response response = httpRequest.request(Method.GET, "/api/v2/storefront/countries/ind");

		String responseBody = response.getBody().prettyPrint();
		System.out.println("Response Body is =>  " + responseBody);

		int statusCode = response.getStatusCode();
		System.out.println("Status code is =>  " + statusCode);
		Assert.assertEquals(200, statusCode);

		JsonPath js = new JsonPath(response.asString());
		String iso_name = js.get("data.attributes.iso_name");
		System.out.println(iso_name);
		Assert.assertEquals(iso_name, "INDIA");

	}
}