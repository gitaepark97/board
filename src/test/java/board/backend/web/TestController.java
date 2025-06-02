package board.backend.web;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@WebMvcTest
@ExtendWith(RestDocumentationExtension.class)
class TestController {

    protected final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    protected WebApplicationContext context;
    protected MockMvc mockMvc;

    @BeforeEach
    void init(RestDocumentationContextProvider provider) {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(documentationConfiguration(provider))
            .build();
    }

}
