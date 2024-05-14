# HTML Analyzer in Java

This system is an HTML analyzer MVP that uses a URL to discover the text localized in your deeper HTML structure section.

### Usage instructions:
_This program can be compiled and executed using JDK 17._
1. Open the CMD;
2. Go to the directory containing the source code files; 
3. Inside the directory, execute the command ``` javac HtmlAnalyzer.java ``` to compile the program;
4. Still inside the directory, write the command ``` java HtmlAnalyzer insert-url-here ``` to execute the program **(don't forget to replace "insert-url-here" for a valid URL)**.



#### Notes:
- No libraries and frameworks were used external to the JDK, as well as packages and native JDK classes related to data manipulation HTML, XML or DOM;
- This code logic is constructed based that all input code follows the following premises:
``` 
1. The HTML code is divided into lines;
2. Each line can only be one of the following types:
A. Opening tag (example: <div>)
B. Closing tag (example: </div>)
C. Text excerpt (example: “This is the body.”)
3. The same line cannot contain two types of content;
4. Only HTML elements with opening and tag pairs closure are used (example: <div> and </div>, but no <br/>)
5. Opening tags do not have attributes (counterexample: <a href=”link.html”>).
```
- This system may not give you the correct output if the input code doesn't follow these premises;
- The exception treatment is also developed to lead with intern problems considering to show only the required outputs ```malformed HTML```, ```URL connection error``` or the deeper text identified.

###

#### Developed by Letícia Beatriz Souza.
