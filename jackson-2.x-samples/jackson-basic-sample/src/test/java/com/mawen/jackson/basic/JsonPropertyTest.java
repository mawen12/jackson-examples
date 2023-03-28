package com.mawen.jackson.basic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2023/3/20
 */
@DisplayName("JsonProperty 简单示例")
public class JsonPropertyTest {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    @DisplayName("序列化测试")
    public void testSerialize() throws JsonProcessingException, JSONException {
        User user = new User();
        user.id = 1L;
        user.name = "mawen";

        String json = OBJECT_MAPPER.writeValueAsString(user);
        String excepted = "{\"userId\": 1, \"username\":  \"mawen\"}";
        JSONAssert.assertEquals(json, excepted, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("反序列化测试")
    public void testDeserialize() throws JsonProcessingException {
        String json = "{\"userId\": 1, \"username\":  \"mawen\"}";
        User user = OBJECT_MAPPER.readValue(json, User.class);

        User expected = new User();
        expected.id = 1L;
        expected.name = "mawen";

        Assertions.assertEquals(user.id, expected.id);
        Assertions.assertEquals(user.name, expected.name);
    }

    static class User {

        @JsonProperty("userId")
        private Long id;

        @JsonProperty("username")
        private String name;

    }

    @Test
    @DisplayName("基于 Getter 序列化的示例")
    public void testSerializeByGetter() throws JsonProcessingException, JSONException {
        String result = OBJECT_MAPPER.writeValueAsString(new A());
        //language=JSON
        String expected = "{\"name\":  \"A\"}";

        JSONAssert.assertEquals(result, expected, JSONCompareMode.STRICT);
    }

    // http://www.cowtowncoder.com/blog/archives/2011/02/entry_443.html
    @Test
    @DisplayName("基于 Setter 反序列化的示例")
    public void testDeserializeWithSetter() throws JsonProcessingException {
        // language=JSON
        String json = "{\"name\":  \"B\"}";

        A a = OBJECT_MAPPER.readValue(json, A.class);
    }

    static class A {

        public String getName() {
            return "A";
        }

        public void setName(String name) {
            System.out.println("Call A setName() " + name);
        }
    }
}
