package com.spree;

import org.testng.annotations.DataProvider;

public class TestData {

	@DataProvider(name = "country")
	public Object[][] getName() {

		return new Object[][] {

				{ "IND", "INDIA" }, { "USA", "UNITED STATES" }, { "ALB", "ALBANIA" }, { "AFG", "AFGHANISTAN" },
				{ "AIA", "ANGUILLA" } };

	}

}
