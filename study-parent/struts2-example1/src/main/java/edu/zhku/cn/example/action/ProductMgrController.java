package edu.zhku.cn.example.action;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;
import edu.zhku.cn.example.domain.Product;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.util.ServletContextAware;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/2/22 21:58
 * @since 0.1
 */
public class ProductMgrController implements ServletRequestAware, ServletResponseAware, ServletContextAware {

    private Product product;

    public String save() {
        product.setId(String.valueOf(new Random().nextInt()));
        System.out.println("保存Product： " + product);

        // 通过ActionContext获取Web资源, 只能操作属性，不能操作原生方法
        // Session 的属性操作 ActionContext.getContext().getSession();
        // Request 的属性操作 ActionContext.getContext().getContextMap()
        // Application 的属性操作 ActionContext.getContext().getApplication()

        return "details";
    }

    public String action1() {
        ActionContext ac = ActionContext.getContext();
        System.out.println(JSON.toJSONString(ac.getContextMap().keySet()));
        System.out.println(JSON.toJSONString(ac.getParameters().keySet()));
        ac.getContextMap().put("name", ac.getParameters().get("name"));
        return "success";
    }

    public Product getProduct() {
        return product;
    }

    public ProductMgrController setProduct(Product product) {
        this.product = product;
        return this;
    }

    @Override
    public void setServletContext(ServletContext context) {

    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {

    }
}
