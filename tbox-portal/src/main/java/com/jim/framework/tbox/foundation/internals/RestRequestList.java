package com.jim.framework.tbox.foundation.internals;

import com.jim.framework.tbox.common.api.RestRequest;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by celiang.hu on 2018-12-19.
 */
@XmlRootElement(name = "requests")
@XmlAccessorType(XmlAccessType.FIELD)
public class RestRequestList {
    @XmlElement(name = "request")
    private List<RestRequest> restRequests;

    public List<RestRequest> getRestRequests() {
        return restRequests;
    }

    public void setRestRequests(List<RestRequest> restRequests) {
        this.restRequests = restRequests;
    }
}
