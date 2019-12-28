package xyz.yunzhongyan.www.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;

@Slf4j
public class HttpRequestUtil {
    public static String sendGet(String uri) {
        try {
            return Request.Get(uri)
                    .execute()
                    .returnContent()
                    .asString();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static String sendPost(String uri, String bodyJsonString) {
        try {
            return Request.Post(uri)
                    .addHeader("Content-Type", "application/json; charset=UTF-8")
                    .bodyString(bodyJsonString, ContentType.APPLICATION_JSON)
                    .execute()
                    .returnContent()
                    .asString();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
