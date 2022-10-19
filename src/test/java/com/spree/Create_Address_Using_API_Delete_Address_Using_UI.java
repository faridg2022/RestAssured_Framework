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
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Create_Address_Using_API_Delete_Address_Using_UI {

	String fname;

	WebDriver driver;

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
		fname = jsonPathEvaluator.get("data.attributes.firstname").toString();
		System.out.println("First Name  =>  " + fname);
		Assert.assertEquals(fname, "Rest");

	}

	@Test(priority = 2)
	public void login() throws InterruptedException {
		WebDriverManager.chromedriver().setup();

		driver.findElement(By.xpath("//*[@id='account-button']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[text()='LOGIN']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@id='spree_user_email']")).sendKeys("farid@spree.com");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@name='spree_user[password]']")).sendKeys("selenium");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@name='commit']")).click();
		Thread.sleep(2000);

		String ActValue = driver.findElement(By.xpath("//*[text()='My Account']")).getText();
		String ExpValue = "MY ACCOUNT";
		Assert.assertEquals(ActValue, ExpValue);

		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[text()='Logged in successfully']")).isDisplayed();

		driver.findElement(By.xpath("//span[contains(text(),'" + fname
				+ "')]/parent::address/parent::div/following-sibling::div//a[@aria-label='Remove Address']//*[name()='svg']"))
				.click();
		Thread.sleep(5000);
		driver.findElement(By.id("delete-address-popup-confirm")).click();
		driver.findElement(By.xpath("//span[normalize-space()='Address has been successfully removed.']"))
				.isDisplayed();

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
