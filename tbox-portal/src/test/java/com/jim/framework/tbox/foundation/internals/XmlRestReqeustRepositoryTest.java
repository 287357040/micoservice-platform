package com.jim.framework.tbox.foundation.internals;

import com.jim.framework.tbox.common.api.RestRequest;
import com.jim.framework.tbox.core.enums.Tool;
import com.jim.framework.tbox.foundation.spi.RestRequestRepository;
import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
import org.junit.Test;
import org.springframework.http.HttpMethod;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by celiang.hu on 2018-12-19.
 */
public class XmlRestReqeustRepositoryTest {

    @Test
    public void marshall() throws JAXBException {
        RestRequestRepository restRequestRepository = new XmlRestReqeustRepository();
        List<RestRequest> restRequests = restRequestRepository.getAllRestRequest();
        System.out.println(restRequests.toString());
    }
}