package uol.compass.cspcapi.application.api.generalPurposeDTO;

import org.junit.jupiter.api.Test;
import uol.compass.cspcapi.application.api.generalPourposeDTO.ResponseDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResponseDTOTest {
    @Test
    public void testGetData() {
        String testData = "Exemplo de dados";
        ResponseDTO<String> responseDTO = new ResponseDTO<>(testData);

        assertEquals(testData, responseDTO.getData());
    }

    @Test
    public void testGetTimestamp() {
        ResponseDTO<String> responseDTO = ResponseDTO.ok("Teste de data");

        assertNotNull(responseDTO.getTimestamp());
    }

    @Test
    public void testOk() {
        String testData = "Dados de teste";

        ResponseDTO<String> responseDTO = ResponseDTO.ok(testData);

        assertEquals(testData, responseDTO.getData());
        assertNotNull(responseDTO.getTimestamp());
    }
}
