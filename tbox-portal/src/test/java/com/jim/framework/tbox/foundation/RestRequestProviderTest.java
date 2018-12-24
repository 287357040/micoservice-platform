package com.jim.framework.tbox.foundation;

import com.jim.framework.tbox.common.api.RestRequest;
import com.jim.framework.tbox.foundation.internals.XmlRestReqeustRepository;
import com.jim.framework.tbox.foundation.spi.RestRequestRepository;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by celiang.hu on 2018-12-19.
 */
public class RestRequestProviderTest {
    @Test
    public void testRestRequestProvider(){
        RestRequestProvider restRequestProvider = new RestRequestProvider.Default();
        RestRequest restRequest = restRequestProvider.getRequest("admin.searchService");
        System.out.println(restRequest.toString());
    }

}