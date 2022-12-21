package cash.machine.cashmachine.endpoint;

import cash.machine.cashmachine.services.OperationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class OperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OperationService operationService;

    @Test
    void logIntoSystem_badCredentials() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/cash-machine-api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "cardID": 1,
                            "cardPIN": "1234"
                        }
                        """))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    @Test
    void makeAPayment() {
    }

    @Test
    void makeAWithdraw() {
    }

    @Test
    void showAccountMoney() {
    }
}