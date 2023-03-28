package com.mawen.jackson.basic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

/**
 * {@link JsonProperty} 示例
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2023/3/20
 */
public class JsonPropertyExample {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws JsonProcessingException {
        serialize();
        deserialize();
    }

    private static void serialize() throws JsonProcessingException {
        User user = new User();
        user.setId(1L);
        user.setName("mawen");
        user.desc = "desc";

        String json = OBJECT_MAPPER.writeValueAsString(user);
        System.out.println(json);
    }

    private static void deserialize() throws JsonProcessingException {
        String json = "{\"userId\": 1, \"username\":  \"mawen\", \"age\":  null, \"desc\":  \"desc\"}";
        User user = OBJECT_MAPPER.readValue(json, User.class);

        System.out.println(user);
    }


    static class User {
        @JsonProperty(value = "userId", index = 3)
        private Long id;

        @JsonProperty(value = "username", index = 2)
        private String name;

        @JsonProperty(value = "age", index = 1, defaultValue = "1", required = true)
        private Integer age;

        @JsonProperty(value = "desc")
        private String desc;

        public User() {
            System.out.println("无参构造器");
        }

        public User(Long id, String name, Integer age) {
            System.out.println("全参构造器");
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }
}
