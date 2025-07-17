package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeEntity;
import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodeGroupPort;
import cl.tenpo.ports.CodePort;
import cl.tenpo.usecases.DeleteCodeGroupUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteCodeGroupUseCaseImplTest {

    private CodePort codePort;
    private CodeGroupPort codeGroupPort;
    private DeleteCodeGroupUseCase deleteCodeGroupUseCase;

    private final UUID groupId = UUID.randomUUID();
    private final String email = "test@test.com";

    @BeforeEach
    void setUp() {
        codePort = mock(CodePort.class);
        codeGroupPort = mock(CodeGroupPort.class);
        deleteCodeGroupUseCase = new DeleteCodeGroupUseCaseImpl(codeGroupPort, codePort);
    }

    @Test
    void shouldDeleteGroupWhenNoAssociatedCodes() {
        when(codePort.findbyGroupId(groupId)).thenReturn(Collections.emptyList());

        deleteCodeGroupUseCase.delete(groupId, email);

        verify(codeGroupPort).finById(groupId); // aunque no lo uses directamente
        verify(codeGroupPort).delete(groupId, email);
    }

    @Test
    void shouldThrowExceptionWhenGroupHasAssociatedCodes() {
        List<CodeEntity> associatedCodes = List.of(CodeEntity.builder().build());

        when(codePort.findbyGroupId(groupId)).thenReturn(associatedCodes);

        assertThatThrownBy(() -> deleteCodeGroupUseCase.delete(groupId, email))
                .isInstanceOf(TenpoBusinessException.class)
                .hasMessageContaining("No se puede eliminar el grupo porque contiene códigos asociados.");

        verify(codeGroupPort, never()).delete(any(), any());
    }
}
