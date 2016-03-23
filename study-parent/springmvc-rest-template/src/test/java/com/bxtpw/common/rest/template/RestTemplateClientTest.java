package com.bxtpw.common.rest.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author 夏集球
 * @time 2015年6月15日 下午3:48:03
 * @version 0.1
 * @since 0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext-rest.xml" })
public class RestTemplateClientTest {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 请求基路径
     */
    private String restBaseUrl = "http://localhost/rest/";

    @Test
    public void testGetInteger() {

        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            System.out.println(converter.getClass());
        }

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new ApikeyHeaderHttpRequestInterceptor("2015090451513"));
        restTemplate.setInterceptors(interceptors);

        Integer argId = 10;
        Integer id = restTemplate.getForObject(restBaseUrl + "getInteger/{id}", Integer.class, argId);
        System.out.println(id);
    }

}

class ApikeyHeaderHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    private final String headerValue;

    public ApikeyHeaderHttpRequestInterceptor(String headerValue) {
        this.headerValue = headerValue;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
        requestWrapper.getHeaders().set("apikey", headerValue);
        return execution.execute(requestWrapper, body);
    }
}
