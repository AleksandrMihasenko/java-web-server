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
            request = httpParser.parseHttpRequest(generateValidGETTestCase());
        } catch (HttpParsingException error) {
        }

        assertNotNull(request);
        assertEquals(request.getRequestTarget(), "/");
        assertEquals(request.getMethod(), HttpMethod.GET);
    }

    @Test
    void parseBadHttpRequest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadGETTestCase());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
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

    @Test
    void parseBadHttpVersion() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadHttpVersionTestCase());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseUnsupportedHttpVersion() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateUnsupportedHttpVersionTestCase());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseHigherHttpVersion() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateHigherHttpVersionTestCase());

            assertNotNull(request);
            assertEquals(request.getBestCompatibleVersion(), HttpVersion.HTTP_1_1);
            assertEquals(request.getOriginalHttpVersion(), "HTTP/1.2");
        } catch (HttpParsingException e) {
        }
    }

    private InputStream generateValidGETTestCase() {
        String rawData = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8082\r\n" +
                "Connection: keep-alive\r\n" +
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
        String rawData = "GeT / HTTP/1.1\r\n" +
            "Host: localhost:8080\r\n" +
            "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
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

    private InputStream generateBadHttpVersionTestCase() {
        String rawData = "GET / HP/1.1\r\n" +
                "Host: localhost:8082\r\n" +
                "Connection: keep-alive\r\n" +
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

    private InputStream generateUnsupportedHttpVersionTestCase() {
        String rawData = "GET / HTTP/2.1\r\n" +
                "Host: localhost:8082\r\n" +
                "Connection: keep-alive\r\n" +
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

    private InputStream generateHigherHttpVersionTestCase() {
        String rawData = "GET / HTTP/1.2\r\n" +
                "Host: localhost:8082\r\n" +
                "Connection: keep-alive\r\n" +
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
}