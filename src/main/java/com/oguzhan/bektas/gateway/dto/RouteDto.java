package com.oguzhan.bektas.gateway.dto;

public class RouteDto {

    private String id;
    private int order = 100;
    private String uri;
    private String path = "";
    private String rewriteRegex;
    private String rewriteReplace;

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