package cl.tenpo.usecases.impl;

import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodePort;
import cl.tenpo.usecases.DeleteCodeUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteCodeUseCaseImplTest {

    private CodePort codePort;
    private DeleteCodeUseCase deleteCodeUseCase;

    @BeforeEach
    void setUp() {
        codePort = mock(CodePort.class);
        deleteCodeUseCase = new DeleteCodeUseCaseImpl(codePort);
    }

    @Test
    void shouldThrowExceptionWhenCodeIdIsNull() {
        String email = "test@test.com";

        assertThatThrownBy(() -> deleteCodeUseCase.delete(null, email))
                .isInstanceOf(TenpoBusinessException.class)
                .hasMessageContaining("El ID del código no puede ser nulo");

        verifyNoInteractions(codePort);
    }

    @Test
    void shouldCallDeleteWhenCodeIdIsValid() {
        UUID codeId = UUID.randomUUID();
        String email = "test@test.com";

        deleteCodeUseCase.delete(codeId, email);

        verify(codePort).findCodeById(codeId);
        verify(codePort).delete(codeId, email);
    }
}
