package extract2csv;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class Extract2Csv {
	private static final class TextPattern {
		final Pattern pattern;
		public ArrayList<String> matches = new ArrayList<>();

		public TextPattern(String stringPattern) {
			pattern = Pattern.compile(stringPattern, Pattern.MULTILINE | Pattern.DOTALL);
		}
	}

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.err.println("Usage: java extract2csv.Extract2Csv [patterns].");
			return;
		}
		var outFile = Paths.get("out.csv");
		for (var i = 0; i < args.length; i++) {
			args[i] = args[i].replace("*", "(.*?)");
		}
		var dir = Paths.get("").toAbsolutePath().toFile();
		var filesInDir = dir.listFiles(file -> {
			try {
				var contentType = Files.probeContentType(file.toPath());
				var fileIsText = contentType != null ? contentType.contains("text/plain") : false;
				return !file.equals(outFile.toFile()) && fileIsText;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		});
		var writer = new OutputStreamWriter(new FileOutputStream(outFile.toFile()), StandardCharsets.UTF_8);
		var csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
		var patterns = new ArrayList<TextPattern>();
		for (var patternString : args) {
			var pattern = new TextPattern(patternString);
			patterns.add(pattern);
		}
		for (var file : filesInDir) {
			var filename = file.getName();
			final String text = Files.readString(file.toPath());
			for (var pattern : patterns) {
				pattern.matches.clear();
				var matcher = pattern.pattern.matcher(text);
				while (matcher.find()) {
					for (int i = 1; i <= matcher.groupCount(); i++) {
						var cell = matcher.group(i).strip();
						pattern.matches.add(cell);
					}
				}
			}
			var maxMatches = patterns.stream().map(pattern -> pattern.matches.size()).max(Integer::compare).get();
			for (var row = 0; row < maxMatches; row++) {
				csvPrinter.print(filename);
				for (var column = 0; column < patterns.size(); column++) {
					// patterns.get(0).matches contains matches for pattern 0,
					// patterns.get(1).matches contains matches for pattern 1...
					var matches = patterns.get(column).matches;
					if (matches.size() > row) {
						csvPrinter.print(matches.get(row));
					} else {
						csvPrinter.print("");
					}
				}
				csvPrinter.println();
			}
		}
		csvPrinter.close(true);
	}
}