# PdfAct

*PdfAct* is a tool for selectively extracting text from PDF files, optionally together with layout information like positional information, font information, or color information. For example, *PdfAct* allows you to extract only the important pieces of text from a PDF file (e.g., the title, the section headings and the body text paragraphs) and to choose the granularity in which the text should be extracted (e.g., characters, words, text lines or paragraphs). 
Further features of *PdfAct* are: 
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

## 2. Run PdfAct using Docker  

The basic syntax of the command to run *PdfAct* is as follows:

```bash
docker run --rm --name pdfact pdfact [--help] [<options>] <pdf-file> [<output-file>]
```

In the following, you will find some example commands that will give you an intuition about how to run *PdfAct* in general.

### 2.1 Extract all text blocks from a PDF file as plain text and print them to the console. 

To extract the text from a PDF file using the standard settings, type:

```bash
docker run --rm --name pdfact -v /home/user/foo.pdf:/input.pdf pdfact input.pdf
```

This will extract **all** text blocks from the PDF file `/home/user/foo.pdf` as plain text and print them to the console (each text block separated by a blank line).
 
### 2.2 Write the extraction output to a file.

Instead of printing the extraction ouptut to the console, you can also write it to a file:

```bash
docker run --rm --name pdfact -v /home/user/foo.pdf:/input.pdf -v /home/user/output.txt:/output.txt pdfact input.pdf output.txt
```

**NOTE**: `/home/user/output.txt` must be an existing file on the host before running the command, because otherwise Docker creates and mount an empty directory (instead of an empty file).  

### 2.3 Create a visualization of the extracted text blocks.

To create a visualization of the extracted text blocks, type:

```bash
docker run --rm --name pdfact -v /home/user/foo.pdf:/input.pdf -v /home/user/visualization.pdf:/visualization.pdf pdfact input.pdf --visualize visualization.pdf
```

This will create the PDF file `/home/user/visualization.pdf`, which is a copy of the input PDF file in which each extracted text block is surrounded by its bounding box and each extracted paragraph is labelled with its semantic role.

**NOTE**: Similar to the note above, `/home/user/visualization.pdf` must be an existing file on the host before running the command, because otherwise Docker creates and mount an empty directory (instead of an empty file).

### 2.4 Extract selected text blocks together with layout information.

If you want to extract only the text blocks with selected semantic roles, say "heading" and "body", together with layout information, type:

```bash
docker run --rm --name pdfact -v /home/user/foo.pdf:/input.pdf pdfact input.pdf --format json --roles heading,body
```

The output will be now in JSON format and will provide layout information for the extracted text blocks. Instead of the format "json" you can also choose "xml"; it will provide the exact same layout information in XML format.

### 2.5 Print usage info.

The output and the behavior of *PdfAct* is highly customizable. To get a complete usage info with an overview of all available options, type
```bash
docker run --rm --name pdfact pdfact --help
```
