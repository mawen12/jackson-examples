package com.mawen.jackson.basic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
 *
 * @see <a href="http://www.cowtowncoder.com/blog/archives/2011/02/entry_443.html"/>
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2023/3/23
 */
@DisplayName("@JsonIgnore 示例")
public class JsonIgnoreTest {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    @DisplayName("基于字段的序列化示例")
    public void testSerializeInField() throws JsonProcessingException, JSONException {
        User user = new User(1L, "mawen");
        String result = OBJECT_MAPPER.writeValueAsString(user);
        // language=JSON
        String excepted = "{\"id\":  1}";

        JSONAssert.assertEquals(result, excepted, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("基于字段的反序列化示例")
    public void testDeserializeInField() throws JsonProcessingException {
        // language=JSON
        String json = "{\"id\":  3, \"name\": \"jack\"}";

        User user = OBJECT_MAPPER.readValue(json, User.class);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getId(), 3L);
        Assertions.assertNull(user.getName());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class User {
        private Long id;
        @JsonIgnore
        private String name;
    }

    @Test
    @DisplayName("基于 Getter 的序列化示例")
    public void testSerializeInGetter() throws JsonProcessingException, JSONException {
        Dept dept = new Dept(2L,"开发部");
        String result = OBJECT_MAPPER.writeValueAsString(dept);
        // language=JSON
        String excepted = "{\"name\": \"开发部\"}";

        JSONAssert.assertEquals(result, excepted, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("基于 Setter 的反序列化示例")
    public void testDeserializeInSetter() throws JsonProcessingException {
        String json = "{\"id\":  3, \"name\":  \"测试部\"}";
        Dept dept = OBJECT_MAPPER.readValue(json, Dept.class);

        Assertions.assertNotNull(dept);
        Assertions.assertNull(dept.getId());
        Assertions.assertEquals(dept.getName(), "测试部");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Dept {
        private Long id;
        private String name;

        @JsonIgnore
        public Long getId() {
            return id;
        }
    }

    @Test
    @DisplayName("基于构造器的示例")
    public void testDeserializeInConstructor() throws JsonProcessingException {
        String json = "{\"id\": 1, \"name\":  \"测试组长\"}";
        Role role = OBJECT_MAPPER.readValue(json, Role.class);

        Assertions.assertNotNull(role);

    }

    @Data
    static class Role {
        private Long id;

        private String name;

        public Role() {
        }

        @JsonIgnore
        @JsonCreator
        public Role(@JsonProperty("id") Long id, @JsonProperty("name") String name) {
            this.id = id;
            this.name = name;
        }
    }
}
