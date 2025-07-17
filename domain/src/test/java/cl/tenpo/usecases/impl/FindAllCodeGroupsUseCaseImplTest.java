package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeGroupEntity;
import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodeGroupPort;
import cl.tenpo.shared.pagination.PageResult;
import cl.tenpo.usecases.FindAllCodeGroupsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindAllCodeGroupsUseCaseImplTest {

    private CodeGroupPort codeGroupPort;
    private FindAllCodeGroupsUseCase useCase;

    @BeforeEach
    void setUp() {
        codeGroupPort = mock(CodeGroupPort.class);
        useCase = new FindAllCodeGroupsUseCaseImpl(codeGroupPort);
    }

    @Test
    void shouldThrowExceptionWhenPageIsNegative() {
        assertThatThrownBy(() -> useCase.find(-1, 10, null))
                .isInstanceOf(TenpoBusinessException.class)
                .hasMessageContaining("Page number must be >= 0");
    }

    @Test
    void shouldThrowExceptionWhenSizeIsLessThanOne() {
        assertThatThrownBy(() -> useCase.find(0, 0, null))
                .isInstanceOf(TenpoBusinessException.class)
                .hasMessageContaining("Page size must be >= 1");
    }

    @Test
    void shouldThrowExceptionWhenResultIsNull() {
        when(codeGroupPort.findAll(0, 10, null)).thenReturn(null);

        assertThatThrownBy(() -> useCase.find(0, 10, null))
                .isInstanceOf(TenpoBusinessException.class)
                .hasMessageContaining("No se encontraron grupos de codigos");
    }

    @Test
    void shouldReturnPageResultWhenValid() {
        PageResult<CodeGroupEntity> expected = new PageResult<>(List.of(CodeGroupEntity.builder().build()), 1, 1, 1);

        when(codeGroupPort.findAll(0, 10, "grupo")).thenReturn(expected);

        PageResult<CodeGroupEntity> result = useCase.find(0, 10, "grupo");

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(codeGroupPort).findAll(0, 10, "grupo");
    }
}
