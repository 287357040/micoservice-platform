package com.jim.framework.tbox.portal;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jim.framework.tbox.core.enums.Env;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by celiang.hu on 2018-12-15.
 */
public class PortalConfig {
    private List<Env> supportEnvs = Lists.newLinkedList();
    private List<String> admin;
    private Set<Env> emailSupportedEnvs = Sets.newHashSet();
    private String[] configViewMemberOnlyEnvs;
    private int connectTimeout;
    private int readTimeout;
    private String protalAddress;
    private String[] emergencyPublishSupportedEnvs;

    public PortalConfig(String[] supportEnvs, String[] emailSupportedEnvs, String[] emergencyPublishSupportedEnvs, String[] strings) {
        if (supportEnvs != null && supportEnvs.length > 0) {
            for (String env : supportEnvs) {
                this.supportEnvs.add(Env.fromString(env));
            }
        }

        if (emailSupportedEnvs != null && emailSupportedEnvs.length > 0) {
            for (String env : emailSupportedEnvs) {
                this.emailSupportedEnvs.add(Env.fromString(env));
            }
        }
        this.emergencyPublishSupportedEnvs = emergencyPublishSupportedEnvs;
    }

    public boolean isConfigViewMemberOnly(String env) {

        for (String memberOnlyEnv : this.configViewMemberOnlyEnvs) {
            if (memberOnlyEnv.equalsIgnoreCase(env)) {
                return true;
            }
        }

        return false;
    }

    public boolean isEmergencyPublishAllowed(Env env) {
        String targetEnv = env.name();

        for (String supportedEnv : this.emergencyPublishSupportedEnvs) {
            if (Objects.equals(targetEnv, supportedEnv.toUpperCase().trim())) {
                return true;
            }
        }
        return false;
    }

    public List<String> getAdmin() {
        return admin;
    }

    public void setAdmin(List<String> admin) {
        this.admin = admin;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getProtalAddress() {
        return protalAddress;
    }

    public void setProtalAddress(String protalAddress) {
        this.protalAddress = protalAddress;
    }

    public List<Env> getSupportEnvs() {
        return supportEnvs;
    }

    public void setSupportEnvs(List<Env> supportEnvs) {
        this.supportEnvs = supportEnvs;
    }

    public Set<Env> getEmailSupportedEnvs() {
        return emailSupportedEnvs;
    }

    public void setEmailSupportedEnvs(Set<Env> emailSupportedEnvs) {
        this.emailSupportedEnvs = emailSupportedEnvs;
    }
}
