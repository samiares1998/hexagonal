package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeEntity;
import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodePort;
import cl.tenpo.shared.pagination.PageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AllCodesUseCaseImplTest {

    private CodePort codePort;
    private AllCodesUseCaseImpl allCodesUseCase;

    @BeforeEach
    void setUp() {
        codePort = mock(CodePort.class);
        allCodesUseCase = new AllCodesUseCaseImpl(codePort);
    }

    @Test
    void shouldThrowExceptionWhenPageIsNegative() {
        assertThatThrownBy(() -> allCodesUseCase.getAll(-1, 10, null, null))
                .isInstanceOf(TenpoBusinessException.class)
                .hasMessageContaining("Page number must be >= 0");
    }

    @Test
    void shouldThrowExceptionWhenSizeIsLessThanOne() {
        assertThatThrownBy(() -> allCodesUseCase.getAll(0, 0, null, null))
                .isInstanceOf(TenpoBusinessException.class)
                .hasMessageContaining("Page size must be >= 1");
    }

    @Test
    void shouldThrowExceptionWhenCodePortReturnsNull() {
        when(codePort.getAll(0, 10, null, null)).thenReturn(null);

        assertThatThrownBy(() -> allCodesUseCase.getAll(0, 10, null, null))
                .isInstanceOf(TenpoBusinessException.class)
                .hasMessageContaining("No se encontraron codes de canales");
    }

    @Test
    void shouldReturnPageResultWhenValidInput() {
        PageResult<CodeEntity> expected = new PageResult<>(List.of(CodeEntity.builder().build()), 1, 1, 1);

        when(codePort.getAll(0, 10, "foo", "bar")).thenReturn(expected);

        PageResult<CodeEntity> result = allCodesUseCase.getAll(0, 10, "foo", "bar");

        assertThat(result).isNotNull();
        verify(codePort, times(2)).getAll(0, 10, "foo", "bar"); // se llama 2 veces en el método
    }
}
