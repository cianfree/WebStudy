package edu.zhku.cn;

import org.junit.Test;

/**
 * Created by Arvin on 2016/3/23.
 */
public class CommonTest {

    @Test
    public void testYiwei() {
        int i = 1;
        System.out.println(i++);
        i = 1;
        System.out.println(++i);
        i = 1;
        System.out.println(i++ + ++i);
        i = 1;
        System.out.println(i++ + ++i + ++i + ++i);
        i = 1;
        System.out.println(i++ + ++i + ++i + (9 + i));
        i = 1;
        System.out.println(i++ + ++i + ++i + (9 + i) + 1);

    }
}
