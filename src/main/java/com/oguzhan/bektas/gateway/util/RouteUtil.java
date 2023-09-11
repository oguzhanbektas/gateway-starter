package com.oguzhan.bektas.gateway.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oguzhan.bektas.gateway.dto.RouteDto;
import com.oguzhan.bektas.gateway.dto.RoutePropertiesDto;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class RouteUtil {
    static ObjectMapper objectMapper = new ObjectMapper();

    public static RouteDto getRouteDetail(RoutePropertiesDto application, boolean debug) {
        RouteDto rt = new RouteDto();

        int initialOrder = 0;
        String key = "";
        String key2 = "";
        String value = "";
        String value2 = "";

        if (application.getId() == null || application.getId().isEmpty()) {
            return null;
        }

        if (application.getUri() == null || application.getUri().isEmpty()) {
            return null;
        }
        rt.setId(application.getId());
        rt.setUri(application.getUri());
        if (application.getPredicates() != null && application.getPredicates().size() > 0) {
            for (String predicate : application.getPredicates()) {
                if (predicate == null || predicate.isEmpty()) {
                    continue;
                }
                int inx = predicate.indexOf("=");
                if (inx != 0) {
                    key = predicate.substring(0, inx);
                    value = predicate.substring(inx + 1);
                }
                if (key.equals("Path")) {
                    rt.setPath(value);
                }
                if (key.equals("Host")) {
                    String[] split = value.split(",");
                    if (split.length > 0) {
                        int say = 0;
                        for (String s : split) {
                            say = say + 1;
                            if (say == 1) {
                                rt.setHost1(s);
                            } else if (say == 2) {
                                rt.setHost2(s);
                            } else if (say == 3) {
                                rt.setHost3(s);
                            } else {
                                break;
                            }
                        }
                    }
                }
                if (key.equals("RemoteAddr")) {
                    String[] split = value.split(",");
                    if (split.length > 0) {
                        int say = 0;
                        for (String s : split) {
                            say = say + 1;
                            if (say == 1) {
                                rt.setRemoteAddr(s);
                            } else if (say == 2) {
                                rt.setRemoteAddr2(s);
                            } else {
                                break;
                            }
                        }
                    }
                }
                key = "";
                value = "";
            }
        }

        if (application.getFilters() != null && application.getFilters().size() > 0) {
            for (String filter : application.getFilters()) {
                if (filter == null || filter.isEmpty()) {
                    continue;
                }
                int inf = filter.indexOf("=");
                if (inf != 0) {
                    key = filter.substring(0, inf);
                    value = filter.substring(inf + 1);
                }
                if (key.equals("RewritePath")) {
                    int inf2 = value.indexOf(",");
                    if (inf2 != 0) {
                        key2 = value.substring(0, inf2);
                        value2 = value.substring(inf2 + 1);
                        value2 = value2.trim();
                    }
                    rt.setRewriteRegex(key2);
                    rt.setRewriteReplace(value2);
                }

                key = "";
                value = "";
            }
        }

        if (debug) {
            initialOrder = 1;
        } else {
            initialOrder = 100;
        }
        rt.setOrder(application.getOrder() + initialOrder);


        return rt;
    }

    public static List<RoutePropertiesDto> getRoutesFromEnvironment(String fileB64) throws IOException {
        byte[] decode = Base64.getDecoder().decode(fileB64);

        String file = new String(decode);


        try {
            List<RoutePropertiesDto> routePropertiesDtos = objectMapper.readValue(file, new TypeReference<List<RoutePropertiesDto>>() {
            });
            return routePropertiesDtos;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

}
