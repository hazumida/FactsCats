import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Post> posts = null;
        CloseableHttpResponse response = null;

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        try {
            HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");
            response = httpClient.execute(request);

            ObjectMapper mapper = new ObjectMapper();
            posts = mapper.readValue(response.getEntity().getContent(), new TypeReference<List<Post>>() {});
        } catch (IOException err) {
            System.out.println(err.getMessage());
        } finally {
            try {
                httpClient.close();
                response.close();
            } catch (IOException err) {
                System.out.println(err.getMessage());
            }
        }

        posts.stream()
                .filter(value -> value.getUpvotes() != null)
                .filter(value -> value.getUpvotes() > 0)
                .forEach(System.out::println);
    }
}
