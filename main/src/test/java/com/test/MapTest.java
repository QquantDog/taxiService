package com.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.n52.jackson.datatype.jts.JtsModule;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


public class MapTest {
    @Test
    void testMap() throws IOException {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JtsModule());

        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(22.45, 34.23));

        String geoJSON = objectMapper.writeValueAsString(point);

        System.out.println(geoJSON);

        InputStream inputStream = new ByteArrayInputStream(geoJSON.getBytes());
        Point point2 = objectMapper.readValue(inputStream, Point.class);
        Assertions.assertEquals(point, point2);

    }
    @Test
    void deserializePoint() throws JsonProcessingException {

    }
}
