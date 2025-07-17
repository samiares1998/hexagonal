package cl.tenpo.controller;

import cl.tenpo.controller.dto.BankTransactionResponseDTO;
import cl.tenpo.controller.dto.CodeDTO;
import cl.tenpo.entities.CodeEntity;
import cl.tenpo.shared.pagination.PageResult;
import cl.tenpo.usecases.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CodeControllerTest {
    @InjectMocks
    private CodeController codeController;

    @Mock
    private  CodeCreateUseCase codeCreateUseCase;
    @Mock
    private  FindByMambuIdUseCase findByMambuIdUseCase;
    @Mock
    private  UpdateCodeByMambuIdUseCase updateCodeByMambuIdUseCase;
    @Mock
    private  DeleteCodeUseCase deleteCodeUseCase;
    @Mock
    private  AllCodesUseCase allCodesUseCase;

    private static final String email = "test@test.com";
    @Test
    void createCode() {
        UUID id = UUID.randomUUID();
        CodeDTO codeDto = CodeDTO.builder()
                .groupId(id)
                .mambuCodeName("")
                .groupName("")
                .build();
        CodeEntity codeEntity= CodeEntity.builder()
                .groupId(id)
                .mambuCodeName("")
                .groupName("")
                .build();
        when(codeCreateUseCase.create(codeEntity,email)).thenReturn(codeEntity);
        ResponseEntity<CodeDTO> response = codeController.createCode(codeDto,email);
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
    }
    @Test
    void getCodeByMambuId() {
        int mambuId = 123;
        CodeEntity codeEntity = CodeEntity.builder()
                .groupId(UUID.randomUUID())
                .groupName("")
                .mambuCodeName("")
                .build();

        when(findByMambuIdUseCase.find(mambuId)).thenReturn(codeEntity);

        ResponseEntity<CodeDTO> response = codeController.getCodeByMambuId(mambuId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        verify(findByMambuIdUseCase).find(mambuId);
    }

    @Test
    void getAll() {
        int page = 0;
        int size = 15;
        String name = "someName";
        String groupName = "group";

        CodeEntity entity = CodeEntity.builder()
                .groupId(UUID.randomUUID())
                .groupName("group")
                .mambuCodeName("")
                .build();
        PageResult<CodeEntity> pageResult = new PageResult<>(List.of(entity), 1, 1, 1);

        when(allCodesUseCase.getAll(page, size, name, groupName)).thenReturn(pageResult);

        ResponseEntity<PageResult<CodeDTO>> response = codeController.getAll(page, size, name, groupName);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        verify(allCodesUseCase).getAll(page, size, name, groupName);
    }

    @Test
    void patchCode() {
        int mambuId = 123;
        CodeDTO codeDto = CodeDTO.builder()
                .groupId(UUID.randomUUID())
                .groupName("")
                .mambuCodeName("")
                .build();

        doNothing().when(updateCodeByMambuIdUseCase)
                .update(eq(mambuId), any(CodeEntity.class), eq(email));

        ResponseEntity<?> response = codeController.patchCode(mambuId, codeDto, email);

        assertThat(response.getStatusCodeValue()).isEqualTo(202);
        verify(updateCodeByMambuIdUseCase).update(eq(mambuId), any(CodeEntity.class), eq(email));
    }

    @Test
    void deleteCode() {
        UUID codeId = UUID.randomUUID();

        doNothing().when(deleteCodeUseCase).delete(codeId, email);

        ResponseEntity<Void> response = codeController.deleteCode(codeId, email);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(deleteCodeUseCase).delete(codeId, email);
    }
}