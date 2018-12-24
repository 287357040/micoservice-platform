package com.jim.framework.tbox.common.api;


import com.jim.framework.tbox.core.enums.Env;
import com.jim.framework.tbox.core.enums.Tool;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;

/**
 * Created by celiang.hu on 2018-12-07.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RestRequest implements Cloneable{

    @XmlAttribute
    private String id;
    @XmlAttribute
    private String name;
    private HttpEntity<Object> data;
    @XmlAttribute
    private HttpMethod httpMethod;
    @XmlAttribute
    private String url;
    private Object[] urlValiables;
    private Object metaObject;

    /**
     * env,serviceGroupName,toolType as service select condition
     */

    private Env env;
    private String serviceGroupName;
    @XmlAttribute
    private Tool toolType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tool getToolType() {
        return toolType;
    }

    public void setToolType(Tool toolType) {
        this.toolType = toolType;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object[] getUrlValiables() {
        return urlValiables;
    }

    public void setUrlValiables(Object[] urlValiables) {
        this.urlValiables = urlValiables;
    }

    public Env getEnv() {
        return env;
    }

    public void setEnv(Env env) {
        this.env = env;
    }

    public String getServiceGroupName() {
        return serviceGroupName;
    }

    public void setServiceGroupName(String serviceGroupName) {
        this.serviceGroupName = serviceGroupName;
    }

    public Object getMetaObject() {
        return metaObject;
    }

    public void setMetaObject(Object metaObject) {
        this.metaObject = metaObject;
    }

    public HttpEntity<Object> getData() {
        return data;
    }

    public void setData(HttpEntity<Object> data) {
        this.data = data;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "RestRequest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", data=" + data +
                ", httpMethod=" + httpMethod +
                ", url='" + url + '\'' +
                ", urlValiables=" + Arrays.toString(urlValiables) +
                ", env=" + env +
                ", metaObject=" + metaObject +
                ", toolType=" + toolType +
                '}';
    }
}
