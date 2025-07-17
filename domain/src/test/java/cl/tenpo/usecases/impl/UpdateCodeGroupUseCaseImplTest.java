package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeGroupEntity;
import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodeGroupPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateCodeGroupUseCaseImplTest {

    private CodeGroupPort codeGroupPort;
    private UpdateCodeGroupUseCaseImpl updateUseCase;

    @BeforeEach
    void setUp() {
        codeGroupPort = mock(CodeGroupPort.class);
        updateUseCase = new UpdateCodeGroupUseCaseImpl(codeGroupPort);
    }

    @Test
    void shouldUpdateGroupWhenIdExists() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "NuevoNombre";
        String email = "user@tenpo.cl";
        CodeGroupEntity group = CodeGroupEntity.builder().id(id).build();

        when(codeGroupPort.finById(id)).thenReturn(group);

        // When
        updateUseCase.update(id, name, email);

        // Then
        verify(codeGroupPort).update(id, name, email);
    }

    @Test
    void shouldThrowExceptionWhenGroupIdDoesNotExist() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "GrupoInexistente";
        String email = "user@tenpo.cl";

        when(codeGroupPort.finById(id)).thenReturn(null);

        // When / Then
        assertThatThrownBy(() -> updateUseCase.update(id, name, email))
                .isInstanceOf(TenpoBusinessException.class)
                .hasMessageContaining("codigo del grupo no existe");

        verify(codeGroupPort, never()).update(any(), any(), any());
    }
}
