package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeEntity;
import cl.tenpo.entities.CodeGroupEntity;
import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodeGroupPort;
import cl.tenpo.ports.CodePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CodeCreateUseCaseImplTest {

    private CodePort codePort;
    private CodeGroupPort codeGroupPort;
    private CodeCreateUseCaseImpl codeCreateUseCase;

    private final String validEmail = "test@test.com";

    @BeforeEach
    void setUp() {
        codePort = mock(CodePort.class);
        codeGroupPort = mock(CodeGroupPort.class);
        codeCreateUseCase = new CodeCreateUseCaseImpl(codePort, codeGroupPort);
    }

    @Test
    void shouldThrowExceptionWhenCodeEntityIsNull() {
        assertThatThrownBy(() -> codeCreateUseCase.create(null, validEmail))
                .isInstanceOf(TenpoBusinessException.class)
                .hasMessageContaining("codeEntity no puede estar vacio");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsBlank() {
        CodeEntity entity = CodeEntity.builder().build();
        assertThatThrownBy(() -> codeCreateUseCase.create(entity, ""))
                .isInstanceOf(TenpoBusinessException.class)
                .hasMessageContaining("codeEntity no puede estar vacio");
    }

    @Test
    void shouldThrowExceptionWhenGroupIdNotExists() {
        UUID groupId = UUID.randomUUID();
        CodeEntity entity = CodeEntity.builder().groupId(groupId).build();

        when(codeGroupPort.finById(groupId)).thenReturn(null);

        assertThatThrownBy(() -> codeCreateUseCase.create(entity, validEmail))
                .isInstanceOf(TenpoBusinessException.class)
                .hasMessageContaining("No existe el id del grupo");
    }

    @Test
    void shouldThrowExceptionWhenMambuIdAlreadyExists() {
        UUID groupId = UUID.randomUUID();
        Integer mambuId = 123;
        CodeEntity entity = CodeEntity.builder().groupId(groupId).mambuIdCode(mambuId).build();

        when(codeGroupPort.finById(groupId)).thenReturn(CodeGroupEntity.builder().build()); // dummy object
        when(codePort.findbyMambuId(mambuId)).thenReturn(Optional.of(CodeEntity.builder().build()));

        assertThatThrownBy(() -> codeCreateUseCase.create(entity, validEmail))
                .isInstanceOf(TenpoBusinessException.class)
                .hasMessageContaining("Ya existe un código con el mambu id");
    }

    @Test
    void shouldCreateCodeWhenValidInput() {
        UUID groupId = UUID.randomUUID();
        Integer mambuId = 456;
        CodeEntity entity = CodeEntity.builder().groupId(groupId).mambuIdCode(mambuId).build();

        when(codeGroupPort.finById(groupId)).thenReturn(CodeGroupEntity.builder().build());
        when(codePort.findbyMambuId(mambuId)).thenReturn(Optional.empty());
        when(codePort.create(entity, validEmail)).thenReturn(entity);

        CodeEntity result = codeCreateUseCase.create(entity, validEmail);

        assertThat(result).isEqualTo(entity);
        verify(codePort).create(entity, validEmail);
    }
}
