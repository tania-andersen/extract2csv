![Java CI with Gradle](https://github.com/tania-andersen/extract2csv/workflows/Java%20CI%20with%20Gradle/badge.svg)

# Extract2csv

Extract2csv is a simple and easy command line tool for extracting text patterns from files to a comma separated file (csv).

## Example

Let’s say you have a folder with a lot of text files, extracted from pdf or Word documents with same text patterns. This command:

```sh
extract2csv "Time of reporting*Reference number" "company name*Dept" "Describe the event*Where did the event physically occur"
```
will extract the text where the asterisk * is into a csv-file named out.csv.

The file name is in column one and the extracted text in the following columns, when out.csv is viewed in a spreadsheet application:

|   |   |   |   |
|---|---|---|---|
|2019-442-2007.txt|march 15, 2019|GREVE MUNICIPALITY|A municipal employee, in the event of a change of job between two municipalities, has inadvertently not been deprived of his rights...|
|2019-442-2721.txt|may 15, 2019|GLADSAXE MUNICIPALITY|A citizen calls and says they can see a paycheck on an employee...|
|2019-442-4934.txt|november 5, 2019|Nordfyns Municipality|Users have viewed profile pictures that have not had the necessary consent to display...|

The example is from Danish FOI documents regarding data breaches.

You can have as many search patterns as you want to.

## Tips

Extract2csv can only read text files, but you can use command line tools such as [pdftotext, doc2txt et al](https://textract.readthedocs.io/en/stable/). to extract text from pdf, Word and other formats.

On Linux and Windows with WSL, you can extract all pdf files in a folder with this command:
```sh
for file in *.pdf; do pdftotext "$file" "$file.txt"; done
```
## Binaries

A Linux program is here: https://github.com/tania-andersen/extract2csv/tree/master/build/native-image 

Run as in the example above.

An executable jar is here: https://github.com/tania-andersen/extract2csv/blob/master/build/libs/Extract2Csv.jar

Run as:

```sh
java -jar Extract2Csv.jar "Time of reporting*Reference number" "company name*Dept" "Describe the event*Where did the event physically occur"
```

## Installation

For Linux program:

```sh
wget https://github.com/tania-andersen/extract2csv/raw/master/build/native-image/extract2csv
sudo cp extract2csv /usr/local/bin
sudo chmod a+rx /usr/local/bin/extract2csv
```

## Source code

The source code is here: https://github.com/tania-andersen/extract2csv

Dependencies: Apache Commons CSV 1.8.

## License
 
The Unlicence http://unlicense.org/
