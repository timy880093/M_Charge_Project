package com.gateweb.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MapMapper {
    protected final Logger logger = LoggerFactory.getLogger(MapMapper.class);
    private Map<String, Object> map;

    public MapMapper(Map<String, Object> map) {
        this.map = map;
    }

    public Optional<Integer> toIntegerOptional(String propertyName) {
        Optional result = Optional.empty();
        try {
            result = Optional.of(Integer.valueOf(String.valueOf(map.get(propertyName))));
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

    public Optional<String> toStringOptional(String propertyName) {
        Optional result = Optional.empty();
        try {
            result = Optional.of(map.get(propertyName));
        } catch (Exception ex) {
            logger.error(propertyName + ":{}", ex.getMessage());
        }
        return result;
    }

    public Optional<String[]> toCharSplitStrArray(String propertyName) {
        List<String> list = new ArrayList<>();
        Optional<String> opt = toStringOptional(propertyName);
        try {
            if (opt.isPresent()) {
                if (opt.get().indexOf(",") == -1) {
                    list.add(opt.get());
                } else {
                    String[] recipientSet = opt.get().split(",");
                    list = Arrays.asList(recipientSet);
                }
            }
            return Optional.of(list.toArray(new String[]{}));
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return Optional.empty();
    }


}
