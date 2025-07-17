package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeGroupEntity;
import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodeGroupPort;
import cl.tenpo.usecases.CreateCodeGroupUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateCodeGroupUseCaseImplTest {

    private CodeGroupPort codeGroupPort;
    private CreateCodeGroupUseCase createCodeGroupUseCase;

    @BeforeEach
    void setUp() {
        codeGroupPort = mock(CodeGroupPort.class);
        createCodeGroupUseCase = new CreateCodeGroupUseCaseImpl(codeGroupPort);
    }

    @Test
    void shouldThrowExceptionWhenGroupNameAlreadyExists() {
        String name = "GrupoExistente";
        String email = "test@test.com";

        when(codeGroupPort.existsByName(name)).thenReturn(true);

        assertThatThrownBy(() -> createCodeGroupUseCase.create(name, email))
                .isInstanceOf(TenpoBusinessException.class)
                .hasMessageContaining("Ya existe un grupo con el nombre");
    }

    @Test
    void shouldCreateGroupWhenNameIsNew() {
        String name = "GrupoNuevo";
        String email = "test@test.com";
        CodeGroupEntity entity = CodeGroupEntity.builder().codeGroupName(name).build();

        when(codeGroupPort.existsByName(name)).thenReturn(false);
        when(codeGroupPort.createCodeGroup(name, email)).thenReturn(entity);

        CodeGroupEntity result = createCodeGroupUseCase.create(name, email);

        assertThat(result).isNotNull();
        assertThat(result.getCodeGroupName()).isEqualTo(name);
        verify(codeGroupPort).createCodeGroup(name, email);
    }
}
