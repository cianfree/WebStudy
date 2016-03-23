package com.bxtpw.common.springmvc.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bxtpw.common.domain.User;

/**
 * @author 夏集球
 * @time 2015年5月25日 上午10:35:03
 * @version 0.1
 * @since 0.1
 */
@RestController
@RequestMapping("rest")
public class RestTemplateController {

    @RequestMapping("getInteger/{id}")
    public Integer getInteger(@PathVariable("id") Integer id, @RequestHeader("apikey") String apikey) {
        System.out.println("apikey: " + apikey);
        return id;
    }

    @RequestMapping("getInt/{id}")
    public int getInt(@PathVariable("id") int id) {
        return id;
    }

    @RequestMapping("getName/{name}")
    public String getName(@PathVariable("name") String name) {
        return name;
    }

    @RequestMapping("getCleanUser")
    public User getCleanUser() {
        return DataProvider.getNoneRefUser();
    }

    @RequestMapping("getHasChildrenUser")
    public User getHasChildrenUser() {
        return DataProvider.getHasChildrenUser();
    }

    @RequestMapping("getChildrenForNoneFather")
    public List<User> getChildrenForNoneFather() {
        return DataProvider.getListUsersForNoneFather();
    }

    @RequestMapping("getChildrenWithFather")
    public List<User> getChildrenWithFather() {
        return DataProvider.getListUsersWithFather();
    }

    @RequestMapping("getDate")
    public Date getDate() {
        return new Date();
    }

    @RequestMapping("getDates")
    public Date[] getDates() {
        return new Date[] { new Date(), new Date() };
    }

    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    public boolean uploadFile(String fileName, MultipartFile file) {
        String folder = "C:\\Users\\Arvin\\Desktop\\upload";
        // 下面是测试代码
        System.out.println(fileName);
        String originalFilename = file.getOriginalFilename();
        System.out.println(originalFilename);
        try {
            FileUtils.writeByteArrayToFile(new File(folder, fileName), file.getBytes());
            System.out.println(file.getBytes().length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @RequestMapping("downloadFile")
    public ResponseEntity<byte[]> downloadFile(String filename) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        File downloadFile = getDictionaryFile(filename);
        headers.setContentDispositionFormData("attachment", downloadFile.getName());
        headers.set("filename", downloadFile.getName());
        return new ResponseEntity<byte[]>(//
                FileUtils.readFileToByteArray(downloadFile), //
                headers, //
                HttpStatus.CREATED);
    }

    /**
     * 获取要下载的文件
     * 
     * @author 夏集球
     * @time 2015年6月16日 下午2:04:56
     * @version 0.1
     * @since 0.1
     * @return
     */
    private File getDictionaryFile(String filename) {
        return new File("C:\\Users\\Arvin\\Desktop", StringUtils.isBlank(filename) ? "catalina.out" : filename);
    }
}
