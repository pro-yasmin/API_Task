package tests;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class Capital {
    //valid case  
	@Test
	public void getCapitalInfo_positive() {

		String capitalName = given().get("https://restcountries.eu/rest/v2/all?fields=name;capital;currencies;latlng")
				.then().extract().path("[1].capital");

		String capitalCurrency = given()
				.get("https://restcountries.eu/rest/v2/all?fields=name;capital;currencies;latlng").then().extract()
				.path("[1].currencies[0].code");

		Response response = given().pathParam("extractedCapitalName", capitalName).when()
				.get("https://restcountries.eu/rest/v2/capital/{extractedCapitalName}");
		int statuscode = response.getStatusCode();
		Assert.assertEquals(statuscode, 200);

		String jsonString = response.asString();
		Assert.assertEquals(jsonString.contains("Åland Islands"), true);

		String countryCurrency = response.then().extract().path("[0].currencies[0].code");

		Assert.assertEquals(countryCurrency, capitalCurrency);

	}

	//send capital name with empty value 
	@Test
	public void getCapitalInfo_negative_empty() {

		Response response = given().pathParam("extractedCapitalName", "").when()
				.get("https://restcountries.eu/rest/v2/capital/{extractedCapitalName}");
		int statuscode = response.getStatusCode();
		Assert.assertEquals(statuscode, 404);

		/*
		 * given().contentType("application/json").pathParam("extractedCapitalName",
		 * "").when()
		 * .get("https://restcountries.eu/rest/v2/capital/{extractedCapitalName}").then(
		 * ).assertThat() .body("status", Is.is(404)).log().all();
		 * 
		 * given().contentType("application/json").pathParam("extractedCapitalName",
		 * "").when()
		 * .get("https://restcountries.eu/rest/v2/capital/{extractedCapitalName}").then(
		 * ).assertThat() .body("message", Is.is("Not Found")).log().all();
		 */

	}
    // send request with country name not capital name
	@Test
	public void getCapitalInfo_negative_withcountryname() {

		/*
		 * Response response = given().pathParam("extractedCapitalName",
		 * "Algeria").when()
		 * .get("https://restcountries.eu/rest/v2/capital/{extractedCapitalName}"); int
		 * statuscode = response.getStatusCode(); Assert.assertEquals(statuscode, 404);
		 */

		given().pathParam("extractedCapitalName", "Algeria").when()
				.get("https://restcountries.eu/rest/v2/capital/{extractedCapitalName}").then().assertThat()
				.body("status", Is.is(404)).log().all();
		given().pathParam("extractedCapitalName", "Algeria").when()
				.get("https://restcountries.eu/rest/v2/capital/{extractedCapitalName}").then().assertThat()
				.body("message", Is.is("Not Found")).log().all();

	}
}
