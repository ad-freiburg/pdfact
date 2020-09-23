# PdfAct

**Disclaimer**: This project is in an early alpha state and is still under development. So expect bugs and use it on your own risk :-)

## Getting started

### Checkout

Checkout the project via

    git clone https://github.com/ad-freiburg/pdfact.git

### Build    

Build the project:

    cd pdfact
    mvn install

### Extracting the text and structure from PDF files

Extract the text and structure from PDF files with:

    ./pdfact [options] <pdf-file> [<output-file>]

This will parse the given PDF file and outputs the extraction results to
`output-file`, if `output-file` is specified. Otherwise the result will be printed to `stdout`.

Positional Arguments:

    <pdf-file>           The path to the PDF file to be processed.
    <output-file>        The path to the file to which the extraction output should be written.
                         If not specified, the output will be written to stdout.

Optional Arguments:

    -h, --help           show this help message and exit
    --format <format>    The output format. Available options: [txt, xml, json].
                         If the format is "txt", the output will contain the text matching the
                         specified --unit and --role options in plain text format (in the format:
                         one text element per line). If the format is "xml" or "json", the output
                         will also contain layout information for each text element, e.g., the
                         positions in the PDF file.
    --unit <unit>        The granularity in which the extracted text (and the layout information,
                         if the chosen output format is dedicated to provide such information)
                         should be broken down in the output. Available options: [characters,
                         blocks, words, areas, paragraphs, lines].
                         For example, when the script is called with the option "--unit words",
                         the output will be broken down by words, that is: the text (and layout
                         information) will be provided word-wise.
    --role [<role> [<role> ...]]
                         The semantic role(s) of the text elements to be extracted. Available
                         options: [figure, appendix, keywords, heading, footer, acknowledgments,
                         caption, toc, abstract, footnote, body, itemize-item, title, reference,
                         affiliation, general-terms, formula, header, categories, table, authors].
                         For example, if the script is called with the option "--role headings",
                         the output will only contain the text (and optionally, the layout
                         information) of the text belonging to a heading. If not specified, all
                         text will be extracted, regardless of the semantic roles.
    --visualize <path>   The path to the file to which a visualization of the extracted text (that
                         is: the original PDF file enriched which bounding boxes around the
                         extracted text elements) should be written to. If not specified, no such
                         visualization will be created.
    --debug [<level>]    The verbosity of the log messages. Available options: [4 (= ERROR), 3 (=
                         WARN), 2 (= INFO), 1 (= DEBUG), 0 (= OFF)].
                         The level defines the minimum level of severity required for a message to
                         be logged.


#### Example

If you wish to extract the words of the body text from a PDF file "foo.pdf", to
have the result in XML format and to create a visualization of the extracted
words to foo-visualized.pdf, use this command:

    ./pdfact --unit words --format xml --role body --visualize ./foo-visualized.pdf foo.pdf


... to be continued ...
