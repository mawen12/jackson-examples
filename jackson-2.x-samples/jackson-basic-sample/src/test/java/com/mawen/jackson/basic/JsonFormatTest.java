package com.mawen.jackson.basic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2023/3/21
 */
@DisplayName("JsonFormat 示例")
public class JsonFormatTest {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    @DisplayName("JsonFormat 反序列化示例")
    public void testJsonFormatForList() throws JsonProcessingException {
        // language=JSON
        String json = "{\"id\": 123, \"values\":  [\"abc\", \"xyz\"]}";

        StringList listString = OBJECT_MAPPER.readValue(json, StringList.class);
        System.out.println(listString);
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    static class StringList extends ArrayList<String> {
        private int id;

        private Iterator<String> getValues() {
            return iterator();
        }

        public void setValues(Collection<String> v) {
            addAll(v);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    @Test
    @DisplayName("JsonFormat 反序列化示例")
    public void testJsonFormatForEnum() throws JsonProcessingException, JSONException {
        Draw draw1 = new Draw();
        draw1.color = Color.BLUE;
        // 不使用 JsonValue，使用 JsonFormat: {"color":{"value":"BLUE","label":"蓝色","desc":"蓝色"}}
        // 使用 JsonValue: {"color":"BLUE"}
        String result = OBJECT_MAPPER.writeValueAsString(draw1);
        //language=JSON
        String expect = "{\"color\":  {\"value\":  \"BLUE\", \"label\":  \"蓝色\", \"desc\":  \"蓝色\"}}";
        JSONAssert.assertEquals(result, expect, JSONCompareMode.STRICT);

        //language=JSON
        String json = "{\"color\": {\"value\": \"RED\" }}";

        Draw draw = OBJECT_MAPPER.readValue(json, Draw.class);
        Assertions.assertEquals(draw.getColor(), Color.RED);
        System.out.println(draw);
    }

    static class Draw {

        private Color color;

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT) // 以对象解析JSON
    enum Color {

        BLUE("蓝色", "蓝色"),
        RED("红色", "红色"),
        ;

        private String label;

        private String desc;

        Color(String label, String dec) {
            this.label = label;
            this.desc = dec;
        }

        public String getValue() {
            return name();
        }

        public String getLabel() {
            return label;
        }

        public String getDesc() {
            return desc;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        @JsonCreator
        public static Color value(@JsonProperty("value") String value) {
            if (value != null) {
                return Enum.valueOf(Color.class, value);
            }
            return null;
        }
    }
}
