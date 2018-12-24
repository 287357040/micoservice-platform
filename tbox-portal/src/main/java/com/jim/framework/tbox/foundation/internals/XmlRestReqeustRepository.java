package com.jim.framework.tbox.foundation.internals;

import com.jim.framework.tbox.common.api.RestRequest;
import com.jim.framework.tbox.foundation.internals.provider.DefaultApplicationProvider;
import com.jim.framework.tbox.foundation.spi.RestRequestRepository;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by celiang.hu on 2018-12-19.
 */
public class XmlRestReqeustRepository implements RestRequestRepository {
    public static final String TBOX_REQUEST_CLASSPATH = "/META-INF/request/";
    public static final String TBOX_MODULE_XML = "^tbox-module-.*.xml";

    @Override
    public List<RestRequest> getAllRestRequest() throws JAXBException {
        List<InputStream> inputStreams = new ArrayList<>();
        List<RestRequest> allRequests = new ArrayList<>();
        String classpath = DefaultApplicationProvider.class.getResource("/").getPath() + TBOX_REQUEST_CLASSPATH;
        File file = new File(classpath);
        if (file.exists()) {
            File[] files = file.listFiles(new FileFilter() {
                public boolean accept(File pathName) {
                    return Pattern.matches(TBOX_MODULE_XML, pathName.getName());
                }
            });
            for (File item : files) {
                String requestFile = TBOX_REQUEST_CLASSPATH + item.getName();
                InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(requestFile);
                if (in == null) {
                    in = DefaultApplicationProvider.class.getResourceAsStream(requestFile);
                }
                inputStreams.add(in);
            }
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
            }
        });
        return allRequests;
    }


}
