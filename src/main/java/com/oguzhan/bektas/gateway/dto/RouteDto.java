package com.oguzhan.bektas.gateway.dto;

public class RouteDto {

    private String id;
    private int order = 100;
    private String uri;
    private String path = "";
    private String rewriteRegex;
    private String rewriteReplace;
    private String host1 = "";
    private String host2 = "";
    private String host3 = "";

    private String remoteAddr = "";
    private String remoteAddr2 = "";

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRemoteAddr2() {
        return remoteAddr2;
    }

    public void setRemoteAddr2(String remoteAddr2) {
        this.remoteAddr2 = remoteAddr2;
    }

    public String getHost3() {
        return host3;
    }

    public void setHost3(String host3) {
        this.host3 = host3;
    }

    public String getHost1() {
        return host1;
    }

    public void setHost1(String host1) {
        this.host1 = host1;
    }

    public String getHost2() {
        return host2;
    }

    public void setHost2(String host2) {
        this.host2 = host2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRewriteRegex() {
        return rewriteRegex;
    }

    public void setRewriteRegex(String rewriteRegex) {
        this.rewriteRegex = rewriteRegex;
    }

    public String getRewriteReplace() {
        return rewriteReplace;
    }

    public void setRewriteReplace(String rewriteReplace) {
        this.rewriteReplace = rewriteReplace;
    }
}