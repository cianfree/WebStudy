package com.bxtpw.common.springmvc.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import com.bxtpw.common.domain.User;
import com.bxtpw.common.rest.template.AbstractListenableFutureCallback;

/**
 * @author 夏集球
 * @time 2015年6月15日 下午4:17:31
 * @version 0.1
 * @since 0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext-rest.xml" })
public class AsyncRestTemplateControllerTest {

    @Autowired
    private AsyncRestTemplate restTemplate;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 请求基路径
     */
    private String restBaseUrl = "http://localhost/rest/";

    /**
     * 获取URI
     * 
     * @author 夏集球
     * @time 2015年6月15日 下午4:18:46
     * @version 0.1
     * @since 0.1
     * @param subUri
     * @return
     */
    public String uri(String subUri) {
        return restBaseUrl + subUri;
    }

    /**
     * Test method for
     * {@link com.bxtpw.common.springmvc.controller.RestTemplateController#getInteger(java.lang.Integer)}
     * .
     * 
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testGetInteger() throws InterruptedException, ExecutionException {
        Integer argId = 10;
        Future<ResponseEntity<Integer>> futureEntity = restTemplate.getForEntity(uri("getInteger/{id}"), Integer.class, argId);

        System.out.println("Before......");

        ResponseEntity<Integer> entity = futureEntity.get();
        System.out.println(entity.getBody());
    }

    @Test
    public void testGetDate1() throws InterruptedException, ExecutionException {
        Future<ResponseEntity<Date>> futureEntity = restTemplate.getForEntity(uri("getDate"), Date.class);

        System.out.println("Before......");

        ResponseEntity<Date> entity = futureEntity.get();
        System.out.println(dateFormat.format(entity.getBody()));
    }

    @Test
    public void testGetDates() throws InterruptedException, ExecutionException {
        Future<ResponseEntity<Date[]>> futureEntity = restTemplate.getForEntity(uri("getDates"), Date[].class);
        System.out.println("Before......");
        Date[] dates = futureEntity.get().getBody();
        if (dates != null && dates.length > 0) {
            for (Date date : dates) {
                System.out.println(dateFormat.format(date));
            }

            List<Date> listDates = Arrays.asList(dates);
            System.out.println(listDates.size());
        }
    }

    @Test
    public void testGetDate() throws InterruptedException, ExecutionException {
        ListenableFuture<ResponseEntity<Date>> futureEntity = restTemplate.getForEntity(uri("getDate"), Date.class);

        System.out.println("未获取...");
        // register a callback
        futureEntity.addCallback(new AbstractListenableFutureCallback<Date>() {

            @Override
            public void handleSuccess(Date object) {
                System.out.println("Success......:" + dateFormat.format(object));
            }

            @Override
            public void handleFailure(Throwable ex) {
                System.out.println("Failure......");
                ex.printStackTrace();
            }

        });

        // 执行获取
        futureEntity.get();
    }

    @Test
    public void testUploadFile() throws InterruptedException, ExecutionException, URISyntaxException {
        String filePath = "C:\\Users\\Arvin\\Desktop\\bxtpw-schema.sql";
        FileSystemResource resource = new FileSystemResource(new File(filePath));
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("file", resource);
        param.add("fileName", "bxtpw-schema.sql");

        System.out.println("Before......");
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(param);

        ListenableFuture<ResponseEntity<Boolean>> futureEntity = restTemplate.exchange(//
                uri("uploadFile"), //
                HttpMethod.POST, //
                httpEntity, //
                Boolean.class);

        System.out.println("未获取...");
        // register a callback
        futureEntity.addCallback(new AbstractListenableFutureCallback<Boolean>() {

            @Override
            public void handleSuccess(Boolean status) {
                System.out.println("Success......:" + status);
            }

            @Override
            public void handleFailure(Throwable ex) {
                System.out.println("Failure......");
                ex.printStackTrace();
            }

        });

        // 执行获取
        futureEntity.get();
    }

    @Test
    public void testAsyncDownload() throws InterruptedException, ExecutionException {

        final String folder = "C:\\Users\\Arvin\\Desktop\\upload";

        ListenableFuture<ResponseEntity<byte[]>> futureEntity = restTemplate.getForEntity(//
                uri("downloadFile?filename={filename}"), //
                byte[].class,//
                "catalina.out");
        System.out.println("未获取...");
        // register a callback
        futureEntity.addCallback(new ListenableFutureCallback<ResponseEntity<byte[]>>() {
            @Override
            public void onSuccess(ResponseEntity<byte[]> entity) {
                System.out.println("Success......");
                HttpHeaders headers = entity.getHeaders();
                String filename = headers.getFirst("filename");
                System.out.println(filename);
                try {
                    FileUtils.writeByteArrayToFile(new File(folder, filename), entity.getBody());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Failure......");
                ex.printStackTrace();
            }
        });
        // 执行获取
        futureEntity.get();
    }

    public void printListUser(List<User> users) {
        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                System.out.println(user.getSimpleString());
                if (user.getFather() != null) {
                    System.out.println("father: " + user.getFather().getSimpleString());
                }
            }
        }
    }
}
