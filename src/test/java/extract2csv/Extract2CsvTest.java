package extract2csv;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import extract2csv.Extract2Csv;

/**
 * Tests for: Extract2Csv.
 */
public class Extract2CsvTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Very basic test.
	 */
	@Test
	public void test() {
		try {
			var datetimePattern = "Tidspunkt for indberetningen*Referencenummer";
			var authorityPattern = "virksomhedens navn*Afdeling";
			var eventPattern = "Beskriv hændelsen*Hvor fandt hændelsen fysisk sted";
			var patternStrings = new String[] { datetimePattern, authorityPattern, eventPattern };
			var here = Paths.get("").toAbsolutePath();
			System.out.println("You are here: " + here.toString());
			var dir = Paths.get("./src/test/resources/testfiles").toAbsolutePath().toFile();
			var filesInDir = dir.listFiles();
			var testfiles = new ArrayList<Path>();
			for (var file : filesInDir) {
				var path = Paths.get(here.toString(), file.getName());
				Files.copy(file.toPath(), path);
				testfiles.add(path);
			}
			Extract2Csv.main(patternStrings);
			var outfile = Paths.get(Extract2Csv.DEFAULT_OUT_FILENAME);
			testfiles.add(outfile);
			assertTrue("Out file is missing", Files.exists(outfile));
			var fileContent = Files.readString(outfile);
			assertTrue("Missing field", fileContent.contains("2019-442-2007.PDF.txt"));
			assertTrue("Missing field", fileContent.contains("onsdag november 6, 2019 14:14:01"));
			assertTrue("Missing field", fileContent.contains("Kalundborg kommune"));
			assertTrue("Missing field", fileContent.contains("hvis denne kode er 3"));
			for (var file : testfiles) {
				Files.delete(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException: " + e.getMessage());
		}
	}

}
