package getresponsevalidation;

import static io.restassured.RestAssured.get;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Getdata {
	Response resp = get("https://apiproxy.paytm.com/v2/movies/upcoming");

	@Test
	public void testStatusCode() {
		int respcode = resp.getStatusCode();
		if (respcode == 200) {
			System.out.println("Response code:" + respcode);
			System.out.println("Status: Pass");

		} else {
			System.out.println("Response code:" + respcode + "Status:Fail");
			System.out.println("Status: Fail");
		}
	}

	@Test
	public void testMovieReleaseDate() throws ParseException {
		ArrayList<String> dates = get("https://apiproxy.paytm.com/v2/movies/upcoming").then().extract()
				.path("upcomingMovieData.releaseDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat finalformat = new SimpleDateFormat("dd-MM-yyyy");
		Date todaysdate = new Date();

		System.out.println("Todays date:" + finalformat.format(todaysdate));

		for (String d : dates) {
			if (d != null) {
				Date releasedate = sdf.parse(d);

				if (releasedate.compareTo(todaysdate) > 0) {
					System.out.println("Release date: " + finalformat.format(releasedate)
							+ " is after todays date. TEST PASSED. ");
				}
			}
		}

	}

	@Test

	public void moviePosterFormat() {
		ArrayList<String> posterurls = get("https://apiproxy.paytm.com/v2/movies/upcoming").then().extract()
				.path("upcomingMovieData.moviePosterUrl");
		ArrayList<String> urlsPassed = new ArrayList<String>();
		ArrayList<String> urlsFailed = new ArrayList<String>();

		for (String url : posterurls) {
			String format = url.substring(url.lastIndexOf("."));
			if (format.equalsIgnoreCase(".jpg"))
				urlsPassed.add(url);
			else
				urlsFailed.add(url);
		}

		System.out.println("****URLS PASSED****");
		for (String urlPass : urlsPassed) {

			System.out.println("URL: " + urlPass);
		}

		System.out.println("****URLS FAILED****");
		for (String urlFail : urlsFailed) {

			System.out.println("URL:" + urlFail);
		}
	}

	@Test
	public void testMovieCode() {
		ArrayList<String> moviecodes = get("https://apiproxy.paytm.com/v2/movies/upcoming").then().extract()
				.path("upcomingMovieData.paytmMovieCode");
		int count = -1;
		boolean unique = false;

		for (int i = 0; i <= moviecodes.size(); i++) {
			for (int j = i + 1; j < moviecodes.size(); j++) {
				if (!(moviecodes.get(i)).equalsIgnoreCase(moviecodes.get(j))) {
					unique = true;
					break;
				}
			}
			if (unique)
				count++;
		}

		if (moviecodes.size() == count)
			System.out.println("All codes are unique. TEST PASSED");

	}

	@Test
	public void testLanguageFormat() {

		ArrayList<String> moviecodes = get("https://apiproxy.paytm.com/v2/movies/upcoming").then().extract()
				.path("upcomingMovieData.paytmMovieCode");
		int count = 0;

		for (int i = 0; i < moviecodes.size(); i++) {

			try {
				ArrayList<String> language = get("https://apiproxy.paytm.com/v2/movies/upcoming").then().extract()
						.path("upcomingMovieData.language[" + i + "]");
			} catch (Exception e) {
				count++;

			}

		}
		if (count == moviecodes.size())
			System.out.println("All movies codes have single language format");

	}

	@Test
	public void testMovieWithContent() throws IOException {

		ArrayList<String> moviecodes = get("https://apiproxy.paytm.com/v2/movies/upcoming").then().extract()
				.path("upcomingMovieData.paytmMovieCode");
		ArrayList<String> moviename = new ArrayList<String>();
		for (int i = 0; i < moviecodes.size(); i++) {
			int contentcode = get("https://apiproxy.paytm.com/v2/movies/upcoming").then().extract()
					.path("upcomingMovieData.isContentAvailable[" + i + "]");
			if (contentcode == 0) {
				moviename.add((String) (get("https://apiproxy.paytm.com/v2/movies/upcoming").then().extract()
						.path("upcomingMovieData.movie_name[" + i + "]")));
			}
		}
		System.out.println("Movie name having content code 0: " + moviename);
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet s1 = wb.createSheet("Movie_Names");
		XSSFCell cell;
		s1.createRow(0).createCell(0).setCellValue("Movie names with content code 0");
		for (int i = 0; i < moviename.size(); i++) {
			cell = s1.createRow(i + 1).createCell(0);
			cell.setCellValue(moviename.get(i));
		}

		Date todaysdate = new Date();
		SimpleDateFormat finalformat = new SimpleDateFormat("ddMMyyyy_hhmmsssss");
		String fileLoc = "..\\..\\eclipseworkspace\\InsiderAssignmentAPI\\Report\\Movielist_"
				+ finalformat.format(todaysdate) + ".xlsx";
		File newFile = new File(fileLoc);
		FileOutputStream or = new FileOutputStream(newFile);
		wb.write(or);
		System.out.println("***Report generated***");

	}

}
