package com.jim.framework.tbox.common.controller;

import com.jim.framework.tbox.core.Tbox;
import com.jim.framework.tbox.foundation.Foundation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by celiang.hu on 2018-12-18.
 */
@RestController
@RequestMapping("/tbox")
public class TboxInfoController {
    @RequestMapping(value = "/app", method = RequestMethod.HEAD)
    public String getApp() {
        return Foundation.app().toString();
    }

    @RequestMapping(value = "/net", method = RequestMethod.HEAD)
    public String getNet() {
        return Foundation.net().toString();
    }

    @RequestMapping(value = "/server", method = RequestMethod.HEAD)
    public String getServer() {
        return Foundation.server().toString();
    }

    @RequestMapping(value = "/version", method = RequestMethod.HEAD)
    public String getVersion() {
        return Tbox.VERSION;
    }
}
