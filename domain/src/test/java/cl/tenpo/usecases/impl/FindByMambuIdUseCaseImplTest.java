package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeEntity;
import cl.tenpo.ports.CodePort;
import cl.tenpo.usecases.FindByMambuIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindByMambuIdUseCaseImplTest {

    private CodePort codePort;
    private FindByMambuIdUseCase useCase;

    @BeforeEach
    void setUp() {
        codePort = mock(CodePort.class);
        useCase = new FindByMambuIdUseCaseImpl(codePort);
    }

    @Test
    void shouldReturnCodeEntityWhenFound() {
        int mambuId = 123;
        CodeEntity expected = CodeEntity.builder().mambuIdCode(mambuId).build();

        when(codePort.findbyMambuId(mambuId)).thenReturn(Optional.of(expected));

        CodeEntity result = useCase.find(mambuId);

        assertThat(result).isNotNull();
        assertThat(result.getMambuIdCode()).isEqualTo(mambuId);
    }

    @Test
    void shouldReturnNullWhenNotFound() {
        int mambuId = 456;

        when(codePort.findbyMambuId(mambuId)).thenReturn(Optional.empty());

        CodeEntity result = useCase.find(mambuId);

        assertThat(result).isNull();
    }
}
