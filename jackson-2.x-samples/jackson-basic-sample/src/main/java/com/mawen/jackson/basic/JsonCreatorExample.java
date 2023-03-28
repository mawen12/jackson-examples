package com.mawen.jackson.basic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.*;

/**
 * {@link com.fasterxml.jackson.annotation.JsonCreator} 示例
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2023/3/20
 */
public class JsonCreatorExample {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws JsonProcessingException {
        deserialize();
    }


    private static void deserialize() throws JsonProcessingException {
        String json = "{\"id\": 1, \"name\": \"mawen\"}";
        User user = OBJECT_MAPPER.readValue(json, User.class);
        System.out.println(user);

        Type integer = OBJECT_MAPPER.readValue("{\"value\": \"INTEGER\"}", Type.class);
        System.out.println(integer);
    }

    static class User {

        private Long id;

        private String name;

        private Type type;

//        @JsonCreator
//        public User(@JsonProperty("id") Long id, @JsonProperty("name") String name) {
//            this.id = id;
//            this.name = name;
//        }

        @JsonCreator(mode = DELEGATING)
        public User(Map<String, Object> map) {
            this.id = (Long) map.get("id");
            this.name = (String) map.get("name");
        }



        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", type=" + type +
                    '}';
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    static enum Type {

        INTEGER("数字"),
        STRING("数字"),
        ;

        private final String type;

        Type(String type) {
            this.type = type;
        }

        @JsonCreator(mode = PROPERTIES)
        public static Type value(@JsonProperty("value") String value) {
            if (value != null) {
                return Enum.valueOf(Type.class, value);
            }
            return null;
        }
    }
}
