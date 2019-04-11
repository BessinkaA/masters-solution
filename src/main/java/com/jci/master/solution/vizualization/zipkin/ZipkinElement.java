package com.jci.master.solution.vizualization.zipkin;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import lombok.*;
import org.springframework.format.annotation.*;

import java.io.*;
import java.util.*;

@Data
public class ZipkinElement {

    private String traceId;
    private String parentId;
    private String id;
    private String kind;
    private String name;
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private Date timestamp;
    private int duration;
    private Boolean shared;
    private ZipkinLocalEndpoint localEndpoint;
    private ZipkinRemoteEndpoint remoteEndpoint;
    private Map<String, String> tags;

    public Boolean getShared() {
        if (shared == null){
            return false;
        }
        return shared;
    }

    public static class UnixTimestampDeserializer extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String timestamp = jp.getText().trim();

            try {
                return new Date(Long.valueOf(timestamp)/1000);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
