package com.spree;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Retrieve_Country_Using_DataProvider {

	@Test(dataProvider = "country", dataProviderClass = TestData.class)
	public void retrieveCountry(String iso3, String iso_name) {
		RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification httpRequest = RestAssured.given();

		Response response = httpRequest.request(Method.GET, "/api/v2/storefront/countries/" + iso3);

		String responseBody = response.getBody().prettyPrint();
		System.out.println("Response Body is =>  " + responseBody);

		int statusCode = response.getStatusCode();
		System.out.println("Status code is =>  " + statusCode);
		Assert.assertEquals(200, statusCode);

		JsonPath js = new JsonPath(response.asString());
		String expected = js.get("data.attributes.iso_name");
		System.out.println(expected);
		Assert.assertEquals(expected, iso_name);
	}
}
