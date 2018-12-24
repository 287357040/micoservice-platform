package com.jim.framework.tbox.foundation.spi;

import com.jim.framework.tbox.common.api.RestRequest;

import javax.xml.bind.JAXBException;
import java.util.List;

/**
 * Created by celiang.hu on 2018-12-19.
 */
public interface RestRequestRepository {
    /**
     *
     * @return get all base api request params
     */
    List<RestRequest> getAllRestRequest() throws JAXBException;
}
