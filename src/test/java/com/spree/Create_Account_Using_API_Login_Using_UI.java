package com.spree;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Create_Account_Using_API_Login_Using_UI {
	WebDriver driver;
	String email;
	String password = "spree123";
	String token;

	@SuppressWarnings("unchecked")
	@Test(priority = 3)
	public void generateToken() {
		RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("grant_type", "password");
		requestParams.put("username", email);
		requestParams.put("password", password);

		request.header("Content-Type", "application/json");
		request.body(requestParams.toJSONString());
		Response response = request.post("/spree_oauth/token");

		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);

		JsonPath jp = response.getBody().jsonPath();
		token = jp.get("access_token").toString();
		System.out.println("oAuth Token is =>  " + token);

	}

	@Test(priority = 1)
	public void createAccount() throws IOException, ParseException {
		RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification request = RestAssured.given();

		JSONParser jsonparser = new JSONParser();
		FileReader reader = new FileReader(".\\JSON_File\\createAccount.json");
		Object obj = jsonparser.parse(reader);
		JSONObject prodjsonobj = (JSONObject) obj;

		request.header("Content-Type", "application/json");
		request.body(prodjsonobj);

		Response response = request.post("/api/v2/storefront/account");
		response.prettyPrint();

		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);

		JsonPath jsonPathEvaluator = response.getBody().jsonPath();

		email = jsonPathEvaluator.get("data.attributes.email").toString();
		System.out.println("email  =>  " + email);

	}

	@Test(priority = 2)
	public void loginWithUI() throws InterruptedException {

		driver.findElement(By.xpath("//*[@id='account-button']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[text()='LOGIN']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@id='spree_user_email']")).sendKeys(email);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@name='spree_user[password]']")).sendKeys(password);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@name='commit']")).click();
		Thread.sleep(2000);

		String ActValue = driver.findElement(By.xpath("//*[text()='My Account']")).getText();
		String ExpValue = "MY ACCOUNT";
		Assert.assertEquals(ActValue, ExpValue);

		String emailVerify = driver.findElement(By.xpath("//dd[text()='" + email + "']")).getText();
		System.out.println(emailVerify);
		Assert.assertEquals(emailVerify, email);

	}

	@Test(priority = 4)
	public void retrieveAccount() {
		RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification request = RestAssured.given();

		request.header("Content-Type", "application/json");
		request.header("Authorization", "Bearer " + token);

		Response response = request.request(Method.GET, "/api/v2/storefront/account");

		String responseBody = response.getBody().prettyPrint();
		System.out.println("Response Body is =>  " + responseBody);

		int statusCode = response.getStatusCode();
		System.out.println("Status code is =>  " + statusCode);
		Assert.assertEquals(statusCode, 200);
	}

	@Test(priority = 5)
	public void updateAccount() throws IOException, ParseException {
		RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification request = RestAssured.given();

		JSONParser jsonparser = new JSONParser();

		FileReader reader = new FileReader(".\\JSON_File/udpateAccount");

		Object obj = jsonparser.parse(reader);

		JSONObject prodjsonobj = (JSONObject) obj;

		request.header("Content-Type", "application/json");
		request.header("Authorization", "Bearer " + token);
		request.body(prodjsonobj);

		Response response = request.patch("/api/v2/storefront/account");

		response.prettyPrint();
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);

		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		String firstName = jsonPathEvaluator.get("data.attributes.email").toString();
		Assert.assertEquals(firstName, "farid135@spree.org");
	}

	@BeforeTest
	public void lauchBrowser() {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://demo.spreecommerce.org/");
		driver.manage().window().maximize();
	}

	@AfterClass
	public void close() {
		driver.quit();
	}

}
