package no.nb.microservices.catalogitem.core.item.service;

public class SecurityInfo {
    private String xHost;
    private String xPort;
    private String xRealIp;
    private String ssoToken;

    public String getxPort() {
        return xPort;
    }

    public void setxPort(String xPort) {
        this.xPort = xPort;
    }

    public String getxRealIp() {
        return xRealIp;
    }

    public void setxRealIp(String xRealIp) {
        this.xRealIp = xRealIp;
    }

    public String getSsoToken() {
        return ssoToken;
    }

    public void setSsoToken(String ssoToken) {
        this.ssoToken = ssoToken;
    }

    public String getxHost() {
        return xHost;
    }

    public void setxHost(String xHost) {
        this.xHost = xHost;
    }

}
