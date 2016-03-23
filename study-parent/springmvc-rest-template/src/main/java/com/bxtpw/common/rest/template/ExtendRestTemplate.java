package com.bxtpw.common.rest.template;

import java.lang.reflect.Array;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @author 夏集球
 * @time 2015年6月15日 下午4:59:25
 * @version 0.1
 * @since 0.1
 */
@SuppressWarnings("unchecked")
public class ExtendRestTemplate extends RestTemplate {

    /**
     * 类型--对应的数组类型
     */
    private final Map<Class<?>, Class<?>> typeArrays = new ConcurrentHashMap<Class<?>, Class<?>>();

    public ExtendRestTemplate() {
        super();
    }

    public ExtendRestTemplate(ClientHttpRequestFactory requestFactory) {
        super(requestFactory);
    }

    public ExtendRestTemplate(List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
    }

    /**
     * 设置MessageConverter，会替换到原来的
     * 
     * @author 夏集球
     * @time 2015年6月16日 上午10:27:24
     * @version 0.1
     * @since 0.1
     * @param messageConverter
     */
    public void setHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        for (HttpMessageConverter<?> converter : messageConverters) {
            replaceHttpMessageConverter(converter);
        }
    }

    /**
     * 替换
     * 
     * @author 夏集球
     * @time 2015年6月16日 上午10:32:02
     * @version 0.1
     * @since 0.1
     * @param messageConverter
     */
    private void replaceHttpMessageConverter(HttpMessageConverter<?> messageConverter) {
        HttpMessageConverter<?> existsConverter = null;
        for (HttpMessageConverter<?> converter : getMessageConverters()) {
            if (converter.getClass() == messageConverter.getClass()) {
                existsConverter = converter;
                break;
            }
        }
        if (existsConverter != null) {
            getMessageConverters().remove(existsConverter);
        }
        getMessageConverters().add(messageConverter);
    }

    /**
     * 获取指定类型的数组类型
     * 
     * @author 夏集球
     * @time 2015年6月16日 上午8:18:54
     * @version 0.1
     * @since 0.1
     * @param classType
     * @return
     */
    protected <T> Class<T[]> getArrayType(Class<T> classType) {
        Class<?> resultType = typeArrays.get(classType);
        if (null == resultType) {
            Object obj = Array.newInstance(classType, 0);
            resultType = (Class<T[]>) obj.getClass();
            typeArrays.put(classType, resultType);
            return (Class<T[]>) resultType;
        } else {
            return (Class<T[]>) resultType;
        }
    }

    public <T> List<T> getList(String url, Class<T> responseType, Object... urlVariables) throws RestClientException {
        T[] result = (T[]) super.getForObject(url, getArrayType(responseType), urlVariables);
        return Arrays.asList(result);
    }

    public <T> List<T> getList(String url, Class<T> responseType, Map<String, ?> urlVariables) throws RestClientException {
        T[] result = (T[]) super.getForObject(url, getArrayType(responseType), urlVariables);
        return Arrays.asList(result);
    }

    public <T> List<T> getList(URI url, Class<T> responseType) throws RestClientException {
        T[] result = (T[]) super.getForObject(url, getArrayType(responseType));
        return Arrays.asList(result);
    }

    public <T> T[] getArray(String url, Class<T> responseType, Object... urlVariables) throws RestClientException {
        return (T[]) super.getForObject(url, getArrayType(responseType), urlVariables);
    }

    public <T> T[] getArray(String url, Class<T> responseType, Map<String, ?> urlVariables) throws RestClientException {
        return (T[]) super.getForObject(url, getArrayType(responseType), urlVariables);
    }

    public <T> T[] getArray(URI url, Class<T> responseType) throws RestClientException {
        return (T[]) super.getForObject(url, getArrayType(responseType));
    }

    /**
     * 将字符串的前后双引号删除
     * 
     * @author 夏集球
     * @time 2015年6月16日 上午8:40:33
     * @version 0.1
     * @since 0.1
     * @param value
     * @return
     */
    protected String getTrimQuotationString(String value) {
        return value != null ? value.replaceAll("^\"", "").replaceAll("\"$", "") : value;
    }

    public String getString(String url, Object... urlVariables) throws RestClientException {
        String result = super.getForObject(url, String.class, urlVariables);
        return null != result ? getTrimQuotationString(result) : result;
    }

    public String getString(String url, Map<String, ?> urlVariables) throws RestClientException {
        String result = super.getForObject(url, String.class, urlVariables);
        return null != result ? getTrimQuotationString(result) : result;
    }

    public String getString(URI url) throws RestClientException {
        String result = super.getForObject(url, String.class);
        return null != result ? getTrimQuotationString(result) : result;
    }

}
