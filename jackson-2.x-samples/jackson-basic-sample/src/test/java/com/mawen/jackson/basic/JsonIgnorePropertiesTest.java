package com.mawen.jackson.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2023/3/23
 */
@DisplayName("@JsonIgnoreProperties 示例")
public class JsonIgnorePropertiesTest {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    @DisplayName("基于类型的序列化")
    public void testSerializeInType() throws JsonProcessingException, JSONException {
        User user = new User(1L, "mawen");
        String result = OBJECT_MAPPER.writeValueAsString(user);
        String excepted = "{\"id\":  1}";

        JSONAssert.assertEquals(result, excepted, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("基于类型的反序列化")
    public void testDeserializeInType() throws JsonProcessingException {
        String json = "{\"id\": 2, \"name\":  \"lucy\"}";
        User user = OBJECT_MAPPER.readValue(json, User.class);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getId(), 2L);
        Assertions.assertNull(user.getName());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties({"name"})
    static class User {

        private Long id;

        private String name;

    }

    @Test
    @DisplayName("基于字段的序列化")
    public void testSerializeInField() throws JsonProcessingException, JSONException {
        Dept dept = new Dept(1L, "开发部", new Employee(1L, "mawen"));
        String result = OBJECT_MAPPER.writeValueAsString(dept);
        String expected = "{\"id\": 1, \"name\":  \"开发部\", \"employee\":  {\"id\": 1}}";

        JSONAssert.assertEquals(result, expected, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("基于字段的反序列化")
    public void testDeserializeInField() throws JsonProcessingException {
        //language=JSON
        String json = "{\"id\": 10, \"name\":  \"研发部\", \"employee\":  {\"id\":  1, \"name\":  \"mawen\"}}";
        Dept dept = OBJECT_MAPPER.readValue(json, Dept.class);

        Assertions.assertNotNull(dept);
        Assertions.assertNotNull(dept.getId());
        Assertions.assertEquals(dept.getName(), "研发部");
        Assertions.assertEquals(dept.getEmployee().getId(), 1L);
        Assertions.assertNull(dept.getEmployee().getName());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Dept {

        private Long id;

        @JsonIgnoreProperties
        private String name;

        @JsonIgnoreProperties({"name"})
        private Employee employee;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Employee {
        private Long id;
        private String name;
    }

}
