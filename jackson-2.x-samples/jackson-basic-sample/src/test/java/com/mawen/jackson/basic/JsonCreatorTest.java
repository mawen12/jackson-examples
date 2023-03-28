package com.mawen.jackson.basic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2023/3/20
 */
@DisplayName("JsonCreator 示例")
public class JsonCreatorTest {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    @DisplayName("基于 PROPERTIES 的反序列化对象示例")
    public void testJsonCreatorObject() throws JsonProcessingException {
        //language=JSON
        String json = "{\"userId\":  1, \"username\":  \"mawen\"}";

        User user = OBJECT_MAPPER.readValue(json, User.class);
        User expected = new User(1L, "mawen");

        Assertions.assertEquals(user.id, expected.id);
        Assertions.assertEquals(user.name, expected.name);
    }

    static class User {

        private Long id;

        private String name;

        @JsonCreator
        public User(@JsonProperty Long id, @JsonProperty String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Test
    @DisplayName("基于 PROPERTIES 的反序列化枚举示例")
    public void testJsonCreatorEnum() throws JsonProcessingException {
        //language=JSON
        String json = "{\"value\": \"INTEGER\"}";

        Type type = OBJECT_MAPPER.readValue(json, Type.class);
        Assertions.assertEquals(type, Type.INTEGER);
    }

    static enum Type {

        INTEGER("数字"),
        STRING("字符串")
        ;


        private final String type;

        Type(String type) {
            this.type = type;
        }

        @JsonCreator
        public static Type value(@JsonProperty("value") String value) {
            if (value != null) {
                return Enum.valueOf(Type.class, value);
            }
            return null;
        }
    }

    @Test
    @DisplayName("基于 DELETIING 的反序列化示例")
    public void testJsonCreatorByDelegating() throws JsonProcessingException {
        // 用于不确定具体类型的场景下，比如反序列化是一个Object
        OBJECT_MAPPER.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);

        //language=JSON
        String json = "{\"id\": 1, \"name\": \"开发部\"}";

        Dept dept = OBJECT_MAPPER.readValue(json, Dept.class);

        Dept expected = new Dept(1L, "开发部");

        Assertions.assertNotNull(dept);
        Assertions.assertEquals(dept.id, expected.id);
        Assertions.assertEquals(dept.name, expected.name);
    }

    static class Dept {
        private Long id;

        private String name;

        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public Dept(Map<String, Object> map) {
            this.id = (Long) map.get("id");
            this.name = String.valueOf(map.get("name"));
        }

        public Dept(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Dept{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
