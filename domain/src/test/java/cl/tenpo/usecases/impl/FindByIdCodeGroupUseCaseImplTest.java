package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeGroupEntity;
import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodeGroupPort;
import cl.tenpo.usecases.FindByIdCodeGroupUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindByIdCodeGroupUseCaseImplTest {

    private CodeGroupPort codeGroupPort;
    private FindByIdCodeGroupUseCase useCase;

    @BeforeEach
    void setUp() {
        codeGroupPort = mock(CodeGroupPort.class);
        useCase = new FindByIdCodeGroupUseCaseImpl(codeGroupPort);
    }

    @Test
    void shouldReturnCodeGroupEntityWhenFound() {
        UUID id = UUID.randomUUID();
        CodeGroupEntity expected = CodeGroupEntity.builder().id(id).codeGroupName("Grupo 1").build();

        when(codeGroupPort.finById(id)).thenReturn(expected);

        CodeGroupEntity result = useCase.findById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getCodeGroupName()).isEqualTo("Grupo 1");
    }


    @Test
    void shouldThrowExceptionWhenGroupNotFound() {
        UUID id = UUID.randomUUID();

        when(codeGroupPort.finById(id)).thenReturn(null);

        assertThatThrownBy(() -> useCase.findById(id))
                .isInstanceOf(TenpoBusinessException.class)
                .hasMessageContaining("No se encontró el grupo con ID");
    }

}
