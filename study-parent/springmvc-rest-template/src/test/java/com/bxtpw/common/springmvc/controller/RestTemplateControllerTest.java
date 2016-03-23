package com.bxtpw.common.springmvc.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSON;
import com.bxtpw.common.domain.User;
import com.bxtpw.common.rest.template.ExtendRestTemplate;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * @author 夏集球
 * @time 2015年6月15日 下午4:17:31
 * @version 0.1
 * @since 0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext-rest.xml" })
public class RestTemplateControllerTest {

    @Autowired
    private ExtendRestTemplate restTemplate;

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
     */
    @Test
    public void testGetInteger() {
        System.out.println(restTemplate.getClass());
        Integer argId = 10;
        Integer id = restTemplate.getForObject(uri("getInteger/{id}"), Integer.class, argId);
        assertEquals(argId, id);
    }

    /**
     * Test method for
     * {@link com.bxtpw.common.springmvc.controller.RestTemplateController#getInt(int)}
     * .
     */
    @Test
    public void testGetInt() {
        int argId = 10;
        int id = restTemplate.getForObject(uri("getInt/{id}"), int.class, argId);
        assertEquals(argId, id);
    }

    /**
     * Test method for
     * {@link com.bxtpw.common.springmvc.controller.RestTemplateController#getName(java.lang.String)}
     * .
     */
    @Test
    public void testGetName() {
        String argName = "arvin";
        String name = restTemplate.getForObject(uri("getName/{name}"), String.class, argName);
        System.out.println(name);
        assertEquals(argName, name);
    }

    /**
     * Test method for
     * {@link com.bxtpw.common.springmvc.controller.RestTemplateController#getCleanUser()}
     * .
     */
    @Test
    public void testGetCleanUser() {
        User user = restTemplate.getForObject(uri("getCleanUser"), User.class);
        assertNotNull(user);
        System.out.println(user.getSimpleString());
    }

    @Test
    public void testGetCleanResultUser() {
        ResultUser user = restTemplate.getForObject(uri("getCleanUser"), ResultUser.class);
        assertNotNull(user);
        System.out.println(user.getSimpleString());
    }

    /**
     * Test method for
     * {@link com.bxtpw.common.springmvc.controller.RestTemplateController#getHasChildrenUser()}
     * .
     */
    @Test
    public void testGetHasChildrenUser() {
        User user = restTemplate.getForObject(uri("getHasChildrenUser"), User.class);
        assertNotNull(user);
        assertNotNull(user.getChildren());
        System.out.println(user.getSimpleString());
    }

    @Test
    public void testGetHasChildrenResultUser() {
        ResultUser user = restTemplate.getForObject(uri("getHasChildrenUser"), ResultUser.class);
        assertNotNull(user);
        assertNotNull(user.getChildren());
        System.out.println(user.getSimpleString());
    }

    /**
     * Test method for
     * {@link com.bxtpw.common.springmvc.controller.RestTemplateController#getChildrenForNoneFather()}
     * .
     * 
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    @Test
    public void testGetChildrenForNoneFather() throws JsonParseException, JsonMappingException, IOException {
        List<User> children = restTemplate.getList(uri("getChildrenForNoneFather"), User.class);
        assertNotNull(children);
        printListUser(children);
    }

    /**
     * Test method for
     * {@link com.bxtpw.common.springmvc.controller.RestTemplateController#getChildrenWithFather()}
     * .
     */
    @Test
    public void testGetChildrenWithFather() {
        List<User> children = restTemplate.getList(uri("getChildrenWithFather"), User.class);
        assertNotNull(children);
        printListUser(children);
    }

    @Test
    public void testGetDate() {
        Date date = restTemplate.getForObject(uri("getDate"), Date.class);
        System.out.println(dateFormat.format(date));
    }

    @Test
    public void testGetDates() {
        Date[] dates = restTemplate.getArray(uri("getDates"), Date.class);
        if (dates != null && dates.length > 0) {
            for (Date date : dates) {
                System.out.println(dateFormat.format(date));
            }
        }
    }

    @Test
    public void testUploadFile() {
        String filePath = "C:\\Users\\Arvin\\Desktop\\bxtpw-schema.sql";
        FileSystemResource resource = new FileSystemResource(new File(filePath));
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("file", resource);
        param.add("fileName", "bxtpw-schema.sql");

        Boolean status = restTemplate.postForObject(uri("uploadFile"), param, Boolean.class);
        System.out.println(status);
    }

    @Test
    public void testDownloadFile() throws IOException {
        String folder = "C:\\Users\\Arvin\\Desktop\\upload";
        
        ResponseEntity<byte[]> entity = restTemplate.getForEntity(//
                uri("downloadFile?filename={filename}"), //
                byte[].class, //
                "bxtpw-schema.sql");
        HttpHeaders headers = entity.getHeaders();
        Set<String> keys = headers.keySet();
        for (String key : keys ) {
            List<String> values = headers.get(key);
            System.out.println(key + ": " + JSON.toJSONString(values));
        }
        
        String filename = headers.getFirst("filename");
        System.out.println(filename);
        FileUtils.writeByteArrayToFile(new File(folder, filename), entity.getBody());
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
