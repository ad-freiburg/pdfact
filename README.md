# Icecite

## How to use

### 0. Checkout

Checkout the project via

    git clone https://github.com/ckorzen/icecite.git --recursive

The --recursive flag is needed to update all submodules. 
If your version of git do not support this flag, you can do:

    git clone https://github.com/ckorzen/icecite.git
    cd icecite
    git submodule init
    git submodule update
    
### 1. Build    

Build the project:
    
    cd icecite/pdf-cli
    mvn install

### 2. Extracting text from PDF files

Extract text from PDF files:
    
    java -jar target/pdf-cli-*-jar-with-dependencies.jar [options] <input> [<output>]

This will parse the given input (which may be a pdf file or a directory of pdf 
files) and outputs the extraction results into the given output dir. If the 
output isn't given, the result will be printed to stdout.

The options are:

    --feature <feature>   The features to extract. Available: [characters,
                          figures, shapes, words, paragraphs, lines]
    --format <format>     The output format [tsv, xml, json].
    --prefix <prefix>     The prefix of files to consider on parsing the
                          input.
    --suffix <suffix>     The suffix of files to consider on parsing the
                          input.
    --recursive           Parse the input recursively.
    --visualize           Create visualization of the extracted features.
 
... to be continued ...
