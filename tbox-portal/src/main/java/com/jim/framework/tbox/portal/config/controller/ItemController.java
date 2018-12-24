package com.jim.framework.tbox.portal.config.controller;

import com.jim.framework.tbox.common.api.RestApiHandler;
import com.jim.framework.tbox.common.dto.config.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apps/{appId}/envs/{env}/clusters/{clusterName}/namespaces/{namespaceName}/items")
public class ItemController {

    @Autowired
    private RestApiHandler restApiHandler;


    @RequestMapping(method = RequestMethod.POST)
    public ItemDTO create(@PathVariable("appId") String appId,
                          @PathVariable("clusterName") String clusterName,
                          @PathVariable("namespaceName") String namespaceName, @RequestBody ItemDTO dto) {
        return null;
    }


    @RequestMapping(path = "/{itemId}", method = RequestMethod.PUT)
    public ItemDTO update(@PathVariable("appId") String appId,
                          @PathVariable("clusterName") String clusterName,
                          @PathVariable("namespaceName") String namespaceName,
                          @PathVariable("itemId") long itemId,
                          @RequestBody ItemDTO itemDTO) {

        return null;
    }


    @RequestMapping(path = "/{itemId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("itemId") long itemId, @RequestParam String operator) {

    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ItemDTO> findItems(@PathVariable("appId") String appId,
                                   @PathVariable("clusterName") String clusterName,
                                   @PathVariable("namespaceName") String namespaceName) {
        return null;

    }

    @RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
    public ItemDTO get(@PathVariable("itemId") long itemId) {
        return null;
    }

    @RequestMapping(value = "/{key:.+}", method = RequestMethod.GET)
    public ItemDTO get(@PathVariable("appId") String appId,
                       @PathVariable("clusterName") String clusterName,
                       @PathVariable("namespaceName") String namespaceName, @PathVariable("key") String key) {
        return null;
    }


}
