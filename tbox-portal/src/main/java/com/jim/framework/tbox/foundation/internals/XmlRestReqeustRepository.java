package com.jim.framework.tbox.foundation.internals;

import com.jim.framework.tbox.common.api.RestRequest;
import com.jim.framework.tbox.foundation.internals.provider.DefaultApplicationProvider;
import com.jim.framework.tbox.foundation.spi.RestRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by celiang.hu on 2018-12-19.
 */
public class XmlRestReqeustRepository implements RestRequestRepository {
    public static final String TBOX_MODULE_XML = "classpath*:/META-INF/tbox-module-*.xml";
    private static final Logger logger = LoggerFactory.getLogger(XmlRestReqeustRepository.class);

    @Override
    public List<RestRequest> getAllRestRequest() throws JAXBException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<InputStream> inputStreams = new ArrayList<>();
        List<RestRequest> allRequests = new ArrayList<>();

        try {
            Resource[] resources = resolver.getResources(TBOX_MODULE_XML);
            for (Resource resource : resources) {
                InputStream in = new FileInputStream(resource.getFile());
                inputStreams.add(in);
            }
        } catch (IOException e) {
            logger.error("init module api failed!", e);
            throw new RuntimeException("init module api failed!");
        }
        inputStreams.forEach(stream -> {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(RestRequestList.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                RestRequestList requestList = (RestRequestList) unmarshaller.unmarshal(stream);
                if (!CollectionUtils.isEmpty(requestList.getRestRequests())) {
                    allRequests.addAll(requestList.getRestRequests());
                }
            } catch (JAXBException e) {
                e.printStackTrace();
            }finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    logger.warn("module api file stream cloe failed!",e);
                }
            }
        });
        return allRequests;
    }


}
