FROM ubuntu:20.04

WORKDIR /

RUN apt-get update -y && apt-get upgrade -y && apt-get install -y maven 

COPY pdfact-cli ./pdfact-cli
COPY pdfact-core ./pdfact-core
COPY resources ./resources
COPY pom.xml .

RUN mvn install -DskipTests

# Define the entrypoint.
ENTRYPOINT ["java", "-jar", "/bin/pdfact.jar"]

# Build image.
# docker build -t pdfact .

# Get usage info.
# docker run --rm --name pdfact pdfact --help

# Extract all paragraphs from a single PDF and print the output to stdout.
# docker run --rm --name pdfact -v <path-to-pdf>:/input.pdf pdfact input.pdf

# Extract the body paragraphs from a single PDF, print the output to output.xml (in XML format) 
# and create a visualization file.
# NOTE: In the command below, <path-to-xml-file> and <path-to-visualization-pdf-file> must be paths to existent files on the host because otherwise Docker creates and mounts directories (instead of files).
# docker run --rm --name pdfact -v <path-to-pdf>:/input.pdf -v <path-to-xml-file>:/output.xml -v <path-to-visualization-pdf-file>:/visualization.pdf pdfact input.pdf output.xml --include-roles body --visualize visualization.pdf --format xml