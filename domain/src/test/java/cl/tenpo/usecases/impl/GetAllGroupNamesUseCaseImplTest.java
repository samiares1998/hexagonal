package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeGroupEntity;
import cl.tenpo.ports.CodeGroupPort;
import cl.tenpo.usecases.GetAllGroupNamesUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAllGroupNamesUseCaseImplTest {

    private CodeGroupPort codeGroupPort;
    private GetAllGroupNamesUseCase useCase;

    @BeforeEach
    void setUp() {
        codeGroupPort = mock(CodeGroupPort.class);
        useCase = new GetAllGroupNamesUseCaseImpl(codeGroupPort);
    }

    @Test
    void shouldReturnGroupNames() {
        CodeGroupEntity group1 = CodeGroupEntity.builder().codeGroupName("Grupo 1").build();
        CodeGroupEntity group2 = CodeGroupEntity.builder().codeGroupName("Grupo 2").build();
        List<CodeGroupEntity> mockGroups = List.of(group1, group2);

        when(codeGroupPort.findAllNames()).thenReturn(mockGroups);

        List<CodeGroupEntity> result = useCase.getAllGroupNames();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(CodeGroupEntity::getCodeGroupName)
                .containsExactly("Grupo 1", "Grupo 2");
    }

    @Test
    void shouldReturnEmptyListWhenNoGroupsFound() {
        when(codeGroupPort.findAllNames()).thenReturn(List.of());

        List<CodeGroupEntity> result = useCase.getAllGroupNames();

        assertThat(result).isEmpty();
    }
}
