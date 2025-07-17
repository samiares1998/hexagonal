package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeEntity;
import cl.tenpo.ports.CodePort;
import cl.tenpo.usecases.CodeGroupUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CodeGroupUseCaseImplTest {

    private CodePort codePort;
    private CodeGroupUseCase codeGroupUseCase;

    @BeforeEach
    void setUp() {
        codePort = mock(CodePort.class);
        codeGroupUseCase = new CodeGroupUseCaseImpl(codePort);
    }

    @Test
    void shouldReturnCodeGroupsWhenCodePortWorks() {
        var expectedList = List.of(CodeEntity.builder().build());

        when(codePort.findCodeGroups()).thenReturn(expectedList);

        List<CodeEntity> result = codeGroupUseCase.getCodeGroups();

        assertThat(result).isNotNull().hasSize(1);
        verify(codePort).findCodeGroups();
    }

    @Test
    void shouldReturnNullWhenCodePortThrowsException() {
        when(codePort.findCodeGroups()).thenThrow(new RuntimeException("DB down"));

        List<CodeEntity> result = codeGroupUseCase.getCodeGroups();

        assertThat(result).isNull(); // porque así lo define tu implementación
        verify(codePort).findCodeGroups();
    }
}
