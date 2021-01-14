# PdfAct

*PdfAct* is a tool for selectively extracting text blocks from PDF files, optionally together with layout information like positional information, font information, or color information. For example, *PdfAct* allows you to extract only the important pieces of text from a PDF file (e.g., the title, the section headings and the body text paragraphs) and to choose the granularity in which the text should be extracted (e.g., characters, words, text lines or paragraphs). 
Further features are: 
(1) the detection of correct paragraph boundaries (which is important because paragraphs could be interrupted by for example, column- or page breaks, or figures, or tables, etc.)
(2) the detection of the natural reading order of text blocks (which is of particular importance in case of multi-column layouts); 
(3) the translation of ligatures and characters with diacritical marks;
(3) the detection of the semantic roles played by the text blocks in a PDF file; and
(4) merging hyphenated words.

**Disclaimer**: Extracting text from PDF files is a suprisingly difficult task, read our [publication "A Benchmark and Evaluation for Text Extraction from PDF"](https://ad-publications.informatik.uni-freiburg.de/benchmark.pdf) to learn why. Further, PdfAct is still in an early alpha state and is still under development. So please excuse bugs and use *PdfAct* on your own risk :-)

## 1. Checkout

Checkout the repository

```bash
git clone https://github.com/ad-freiburg/pdfact.git
cd pdfact
```

## 2. Run PdfAct 

You have several options to run *PdfAct*. 

### 2.1 Without Docker

Build the project with

```bash
mvn install
```

This will produce the two executables `./bin/pdfact.jar` and `./bin/pdfact`. 
Both files are logically equivalent, as they accept the exact same command line options, will run the exact same code in the background and will also produce the exact same extraction output for the same PDF file. 
Both files simply provide two different options to run *PdfAct* from the command line:

*Option 1*:
  ```bash
  java -jar ./bin/pdfact.jar [--help] [<options>] <pdf-file> [<output-file>]
  ```

*Option 2*:
  ```bash
  ./bin/pdfact [--help] [<options>] <pdf-file> [<output-file>]
  ```


Note that the syntax of both commands is principally the same and that they differ only in their prefixes ("`java -jar ./bin/pdfact.jar`" and "`./bin/pdfact`").
Obviously, *Option 2* is much shorter and thus a bit more convenient to write.

#### **Example commands**

In the following, you will find some example commands that will give you an intuition on how to use *PdfAct* in general.
Note that, for the sake of convenience, the commands are written in the shorter notation of Option 2.
If you prefer to use the notation of Option 1 instead, just replace the "`./bin/pdfact`" prefixes in the commands below by "`java -jar ./bin/pdfact.jar`".

*(1) Extract all text blocks from a PDF file as plain text and print them to the console.*
```bash
./pdfact foo.pdf
```
This extracts **all** text blocks from the PDF file `foo.pdf` as plain text and prints them to the console (each text block separated by a blank line).

*(2) Extract all text blocks from a PDF file as plain text and write them to a file.*
```bash
./pdfact foo.pdf output.txt
```
Instead of printing the extraction output to the console, you can also write it to a file by specifying a file path as the second argument. 
In the command above, the extraction output is written to *output.txt*. 

*(3) Create a visualization of the extracted text blocks.*

```bash
./pdfact foo.pdf --visualize visualization.pdf
```
This creates the PDF file `/home/user/visualization.pdf`, which is in fact a copy *foo.pdf* with each extracted text block surrounded by its bounding box and each extracted paragraph labelled with its semantic role.

*(4) Extract only selected text blocks, together with layout information.*

```bash
./pdfact foo.pdf --format json --roles heading,body
```
This extracts the text blocks with the semantic roles "heading" and "body", together with their layout information (the positions in the PDF, the font information and the color information). The output is encoded in JSON format and provides the layout information in form of key-value pairs (TODO: Explain the exact format in detail). Instead of *json* you can also choose the format *xml* which will encode the exact same layout information in XML format (TODO: Explain this format as well).

*(5) Print usage info.*
```bash
./pdfact --help
```
The output and the behavior of *PdfAct* is highly customizable. To get a complete usage info with an overview of all available options, type the command above.

### 2.2 With Docker  

If you want to use Docker instead, you first have to build the Docker image:

```bash
docker build -t pdfact .
```

The basic syntax of the Docker command to run *PdfAct* is as follows:

```bash
docker run --rm pdfact [--help] [<options>] <pdf-file> [<output-file>]
```

#### **Example commands**

In the following, we give the Docker commands equivalent to the example commands in Section 2.1:
*(1) Extract all text blocks from a PDF file as plain text and print them to the console.*
```bash
docker run --rm -v /home/user/foo.pdf:/input.pdf pdfact input.pdf
```
 
*(2) Write the extraction output to a file.*
```bash
docker run --rm -v /home/user/foo.pdf:/input.pdf -v /home/user/output.txt:/output.txt pdfact input.pdf output.txt
```
*NOTE*: `/home/user/output.txt` must exist on the host before running the command, because otherwise Docker creates and mount the empty *directory* `/home/user/output.txt`.  

*(3) Create a visualization of the extracted text blocks.*
```bash
docker run --rm -v /home/user/foo.pdf:/input.pdf -v /home/user/visualization.pdf:/visualization.pdf pdfact input.pdf --visualize visualization.pdf
```
*NOTE*: Similar to the note above, `/home/user/visualization.pdf` must exist on the host before running the command, because otherwise Docker creates and mount the empty *directory* `/home/user/visualization.pdf`.

*(4) Extract selected text blocks together with layout information.*
```bash
docker run --rm -v /home/user/foo.pdf:/input.pdf pdfact input.pdf --format json --roles heading,body
```

*(5) Print usage info.*
```bash
docker run --rm pdfact --help
```

