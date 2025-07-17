package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeEntity;
import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateCodeByMambuIdUseCaseImplTest {

    private CodePort codePort;
    private UpdateCodeByMambuIdUseCaseImpl updateUseCase;

    @BeforeEach
    void setUp() {
        codePort = mock(CodePort.class);
        updateUseCase = new UpdateCodeByMambuIdUseCaseImpl(codePort);
    }

    @Test
    void shouldUpdateCodeWhenMambuIdExists() {
        // Given
        int mambuId = 123;
        String email = "test@tenpo.cl";
        CodeEntity entity = CodeEntity.builder().mambuIdCode(mambuId).build();

        when(codePort.findbyMambuId(mambuId)).thenReturn(Optional.of(entity));

        // When
        updateUseCase.update(mambuId, entity, email);

        // Then
        verify(codePort).update(mambuId, entity, email);
    }

    @Test
    void shouldThrowExceptionWhenMambuIdDoesNotExist() {
        // Given
        int mambuId = 123;
        String email = "test@tenpo.cl";
        CodeEntity entity = CodeEntity.builder().mambuIdCode(mambuId).build();

        when(codePort.findbyMambuId(mambuId)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> updateUseCase.update(mambuId, entity, email))
                .isInstanceOf(TenpoBusinessException.class)
                .hasMessageContaining("codigo mambu no existe");

        verify(codePort, never()).update(anyInt(), any(), any());
    }
}
