package com.bxtpw.zk.config.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Arvin on 2016/3/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-config.xml"})
public class ZKConfigTest {

    @Test
    public void testVoid() {
        System.out.printf("Hello");
        while(true) ;
    }

}