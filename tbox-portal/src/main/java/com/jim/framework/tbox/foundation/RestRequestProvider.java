package com.jim.framework.tbox.foundation;

import com.google.common.collect.Maps;
import com.jim.framework.tbox.common.api.RestRequest;
import com.jim.framework.tbox.core.enums.Env;
import com.jim.framework.tbox.foundation.internals.XmlRestReqeustRepository;
import com.jim.framework.tbox.foundation.spi.RestRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.Map;


/**
 * Created by celiang.hu on 2018-12-19.
 */
public interface RestRequestProvider {
    /**
     * can get rest request by id or name
     *
     * @param uniqueId
     * @return
     */
    RestRequest getRequest(String uniqueId);

    /**
     * @param uniqueId     唯一标识
     * @param urlValiables url参数
     * @param env          环境
     * @return
     */
    RestRequest resolveRequest(String uniqueId, String env, String[] urlValiables);

    /**
     * @param uniqueId     唯一标识
     * @param env          环境
     * @param urlValiables url参数
     * @param metaObject   参数对象
     * @return
     */
    RestRequest resolveRequest(String uniqueId, String env,String[] urlValiables, Object metaObject);

    class Default implements RestRequestProvider {
        private static final Logger logger = LoggerFactory.getLogger(Default.class);
        private RestRequestRepository restRequestRespository;
        private Map<String, RestRequest> requestNameCache = Maps.newHashMap();
        private Map<String, RestRequest> requestIdCache = Maps.newHashMap();

        public Default() {
            restRequestRespository = new XmlRestReqeustRepository();
            initialize(restRequestRespository);
        }

        private void initialize(RestRequestRepository restRequestRespository) {
            try {
                List<RestRequest> requestList = restRequestRespository.getAllRestRequest();
                requestList.forEach(request -> {
                    if (requestNameCache.containsKey(request.getName())) {
                        logger.warn("exist the same rest request name:" + request.getName());
                    } else {
                        requestNameCache.put(request.getName(), request);
                    }

                    if (requestIdCache.containsKey(request.getId())) {
                        logger.warn("exist the same rest request id:" + request.getId());
                    } else {
                        requestIdCache.put(request.getId(), request);
                    }
                });
            } catch (JAXBException e) {
                throw new RuntimeException("initial service fialed", e);
            }
        }

        @Override
        public RestRequest getRequest(String uniqueId) {
            RestRequest result = null;
            if (requestNameCache.containsKey(uniqueId)) {
                result = requestNameCache.get(uniqueId);
            }
            if (requestIdCache.containsKey(uniqueId)) {
                result = requestIdCache.get(uniqueId);
            }
            try {
                return (RestRequest) result.clone();
            } catch (CloneNotSupportedException e) {
                logger.warn("copy request failed", e);
            } finally {
                return result;
            }
        }

        @Override
        public RestRequest resolveRequest(String uniqueId, String env, String[] urlValiables) {
            return resolveRequest(uniqueId, env,urlValiables, null);
        }

        @Override
        public RestRequest resolveRequest(String uniqueId, String env,String[] urlValiables, Object metaObject) {
            RestRequest request = getRequest(uniqueId);
            if (request != null) {
                request.setEnv(Env.fromString(env));
                request.setUrlValiables(urlValiables);
                request.setMetaObject(metaObject);
            }
            return request;
        }

    }

}
