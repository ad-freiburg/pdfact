# PdfAct

**Disclaimer**: This project is in an early alpha state and is still under development. So expect bugs and use it on your own risk :-)

## How to use

### Checkout

Checkout the project via

    git clone https://github.com/ad-freiburg/pdfact.git

### Build    

Build the project:

    mvn install

### Extracting the structure from PDF files

Extract structure from PDF files:

    ./pdfact [options] <pdf> [<output>]

This will parse the given PDF file and outputs the extraction results to
*output*, if *output* is given. Otherwise the result will be printed to stdout.

Positional Arguments:

    <pdf-path>           Defines the path to the PDF file to process.
    <output-file>        Defines the path to the file where pdfact should write  the  text output. If not specified, the output will be
                         written to stdout.

Optional Arguments:

    -h, --help             show this help message and exit
    --format <format>      Defines the format in which the text output should be written. Choose from: [txt, xml, json].
    --unit <unit>          Defines the text unit to extract. Choose from:[txt, xml, json].
    --role [<role> [<role> ...]]
                         Defines one or more semantic role(s)  in  order  to  filter  the  chosen  text  units  in the text output (and
                         visualization if the --visualize option is given) by  those  roles.  If  not specified, all text units will be
                         included, regardless of their  semantic  roles.  Choose  from:  [figure,  appendix, keywords, heading, footer,
                         acknowledgments, caption, toc, abstract, footnote, body, itemize-item, title, reference, affiliation, general-
                         terms, formula, header, categories, table, authors]
    --visualize <path>     Defines a path to a file where pdfact should  write  a  visualization  of  the text output (that is a PDF file
                         where the chosen elements are  surrounded  by  bounding  boxes).  If  not  specified, no visualization will be
                         created.
    --debug [<level>]      Defines the verbosity of debug messages.  The  level  defines  the  minimum  level  of severity required for a
                         message to be logged. Choose from:
                          4 ERROR
                          3 WARN
                          2 INFO
                          1 DEBUG
                          0 OFF


#### Example

If you wish to extract the words and text lines from a PDF file "foo.pdf", to
have the result in an XML file and to create a visualization of the extracted
features (a pdf where the extracted words and text lines are
higlighted in different colors), use this command:

    java -jar target/pdf-cli-*-jar-with-dependencies.jar --feature words --feature lines --format xml --visualize foo.pdf


... to be continued ...
