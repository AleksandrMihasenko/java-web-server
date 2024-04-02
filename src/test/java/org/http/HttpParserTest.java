package org.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {
    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass() {
        httpParser =new HttpParser();
    }

    @Test
    void parseHttpRequest() {
        HttpRequest request = null;

        try {
            request = httpParser.parseHttpRequest(generateBadGETTestCase());
        } catch (HttpParsingException error) {
            fail(error);
        }

        assertEquals(request.getMethod(), HttpMethod.GET);
    }

    @Test
    void parseBadHttpRequest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadGETTestCase());
        } catch (HttpParsingException error) {
            assertEquals(error.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
   }

    @Test
    void parseBadMethodLengthHttpRequest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadMethodLengthTestCase());
        } catch (HttpParsingException error) {
            assertEquals(error.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @Test
    void parseBadVersionLengthHttpRequest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadVersionLengthTestCase());
        } catch (HttpParsingException error) {
            assertEquals(error.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseBadWithEmptyRequestLine() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadVersionLengthTestCase());
        } catch (HttpParsingException error) {
            assertEquals(error.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseBadWithEmptyLineBreak() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadWithEmptyLineBreakTestCase());
        } catch (HttpParsingException error) {
            assertEquals(error.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    private InputStream generateValidGETTestCase() {
        String rawData = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8082\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Microsoft Edge\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "DNT: 1\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36 Edg/123.0.0.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.UTF_8));

        return inputStream;
    }

    private InputStream generateBadGETTestCase() {
        String rawData = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8082\r\n" +
                "Accept-Language: ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.UTF_8));

        return inputStream;
    }

    private InputStream generateBadMethodLengthTestCase() {
        String rawData = "GETMOCK / HTTP/1.1\r\n" +
                "Host: localhost:8082\r\n" +
                "Accept-Language: ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.UTF_8));

        return inputStream;
    }

    private InputStream generateBadVersionLengthTestCase() {
        String rawData = "GET / HTTPMOCK/1.1\r\n" +
                "Host: localhost:8082\r\n" +
                "Accept-Language: ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.UTF_8));

        return inputStream;
    }

    private InputStream generateBadWithEmptyRequestLineTestCase() {
        String rawData = "\r\n" +
                "Host: localhost:8082\r\n" +
                "Accept-Language: ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.UTF_8));

        return inputStream;
    }

    private InputStream generateBadWithEmptyLineBreakTestCase() {
        String rawData = "GET / HTTP/1.1" +
                "Host: localhost:8082\r\n" +
                "Accept-Language: ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.UTF_8));

        return inputStream;
    }
}