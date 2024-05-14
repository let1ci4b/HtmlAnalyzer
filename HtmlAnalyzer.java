import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlAnalyzer {

    public static void main(String[] args) {
        checkArgsValidity(args);
    }
    enum ContentType { OPENING_TAG, CLOSING_TAG, TEXT } // HTML content type

    // verify arguments validity
    private static void checkArgsValidity(String[] args) {
        String url = args.length == 1 ? args[0] : null;
        String htmlContent;

        if (url == null) return; // if we received no correct format args

        try {
            htmlContent = getHtmlContent(url);
        } catch (Exception e) { // handling with getHtmlContent() exceptions
            System.out.println("URL connection error");
            return;
        }

        if(isHtmlMalformed(htmlContent)) {
            System.out.println("malformed HTML");
        } else {
            try {
                System.out.println(extractDeeperText(htmlContent));
            } catch (Exception e) { // handling with extractDesiredSection() exceptions
                e.printStackTrace(); // no expected error output
            }
        }
    }

    // extract HTML content from argued URL
    private static String getHtmlContent(String urlString) throws Exception {
        HttpURLConnection connection = null;
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
            }

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return content.toString();
    }

    // extract and analyze sections from HTML content to find deeper section text
    // considering the HTML format premises
    private static String extractDeeperText(String htmlContent) throws Exception {
        String deeperText = null;
        String pattern = "(?<=<[^>]*>)[^<]+|<[^>]*>|[^<]+"; // recognizing opening tags, closing tags and texts
        Matcher matcher = Pattern.compile(pattern).matcher(htmlContent);
        int maxDepth = 0;
        int currentDepth = 0;

        while (matcher.find()) { // looping through every HTML structure line containing pattern content
            String lineContent = matcher.group().trim();

            if (!lineContent.isEmpty()) { // ignoring blank lines
                switch (getContentType(lineContent)) {
                    case OPENING_TAG:
                        currentDepth++;
                        break;
                    case CLOSING_TAG:
                        currentDepth--;
                        break;
                    case TEXT:
                        if (deeperText == null) {
                            deeperText = lineContent;
                            maxDepth = currentDepth;
                        } else if (currentDepth > maxDepth) {
                            deeperText = lineContent;
                            maxDepth = currentDepth;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return deeperText;
    }

    // verifying if HTML structure is malformed
    private static Boolean isHtmlMalformed(String htmlContent) {
        Stack<String> tagStack = new Stack<>();
        String pattern = "<(/?\\w+)[^>]*>"; // recognizing opening tags and closing tags
        Matcher matcher = Pattern.compile(pattern).matcher(htmlContent);

        while (matcher.find()) { // looping through html tags
            String tag = matcher.group(1).trim();

            if (tag.startsWith("/")) {
                if (tagStack.isEmpty()) {
                    return true; // closing tag without opening tag - HTML structure is malformed
                }
                String openingTag = tagStack.pop();
                if (!openingTag.equals(tag.substring(1))) {
                    return true; // closing tag doesn't correspond to last opening tag - HTML structure is malformed
                }
            } else {
                tagStack.push(tag);
            }
        }

        return !tagStack.isEmpty(); // opening tags without closing tags - HTML structure is malformed
        // analyzing common HTML malformed causes that could appear in the input code considering the HTML format premises
    }

    // populating ContentType ENUM
    private static ContentType getContentType(String line) {
        if (line.startsWith("<")) {
            if (line.startsWith("</")) {
                return ContentType.CLOSING_TAG;
            } else {
                return ContentType.OPENING_TAG;
            }
        } else {
            return ContentType.TEXT;
        }
    }
}