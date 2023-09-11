package com.oguzhan.bektas.gateway.util;

import com.oguzhan.bektas.gateway.dto.RouteDto;
import com.oguzhan.bektas.gateway.dto.RoutePropertiesDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;


public class RouteUtil {


    public static RouteDto getRouteDetail(RoutePropertiesDto application, boolean debug) {
        RouteDto rt = new RouteDto();

        String key = "";
        String key2 = "";
        String value = "";
        String value2 = "";

        if (StringUtils.isBlank(application.getId())) {
            return null;
        }

        if (StringUtils.isBlank(application.getUri())) {
            return null;
        }
        rt.setId(application.getId());
        rt.setUri(application.getUri());
        if (!CollectionUtils.isEmpty(application.getPredicates())) {
            for (String predicate : application.getPredicates()) {
                if (StringUtils.isBlank(predicate)) {
                    continue;
                }
                int indexPredicate = predicate.indexOf("=");
                if (indexPredicate != 0) {
                    key = predicate.substring(0, indexPredicate);
                    value = predicate.substring(indexPredicate + 1);
                } else {
                    continue;
                }
                if ("Path".equalsIgnoreCase(key)) {
                    rt.setPath(value);
                }
            }
        }
        if (!CollectionUtils.isEmpty(application.getFilters())) {
            for (String filter : application.getFilters()) {
                if (StringUtils.isBlank(filter)) {
                    continue;
                }
                int indexFilter = filter.indexOf("=");
                if (indexFilter != 0) {
                    key = filter.substring(0, indexFilter);
                    value = filter.substring(indexFilter + 1);
                } else {
                    continue;
                }
                if ("RewritePath".equalsIgnoreCase(key)) {
                    int indexFilter2 = value.indexOf(",");
                    if (indexFilter2 != 0) {
                        key = value.substring(0, indexFilter2);
                        value = value.substring(indexFilter2 + 1);
                        value = value.trim();
                    }
                    rt.setRewriteRegex(key);
                    rt.setRewriteReplace(value);
                }
            }
        }

        if (debug) {
            rt.setOrder(application.getOrder() + 1);
            return rt;
        }

        rt.setOrder(application.getOrder() + 100);
        return rt;
    }

}
