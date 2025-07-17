package cl.tenpo.controller;

import cl.tenpo.controller.dto.CodeDTO;
import cl.tenpo.controller.dto.CodeGroupResponse;
import cl.tenpo.entities.CodeEntity;
import cl.tenpo.entities.CodeGroupEntity;
import cl.tenpo.shared.pagination.PageResult;
import cl.tenpo.usecases.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CodeGroupControllerTest {

    @InjectMocks
    private CodeGroupController codeGroupController;

    @Mock
    private CodeGroupUseCase getCodeGroupUseCase;
    @Mock
    private CreateCodeGroupUseCase createCodeGroupUseCase;
    @Mock
    private FindAllCodeGroupsUseCase findAllCodeGroups;
    @Mock
    private GetAllGroupNamesUseCase getAllGroupNames;
    @Mock
    private FindByIdCodeGroupUseCase findByIdCodeGroupUseCase;
    @Mock
    private UpdateCodeGroupUseCase updateCodeGroupUseCase;
    @Mock
    private DeleteCodeGroupUseCase deleteCodeGroupUseCase;

    private final String email = "test@test.com";

    @Test
    void transaction() {
        CodeEntity entity = CodeEntity.builder()
                .groupId(UUID.randomUUID())
                .groupName("")
                .mambuCodeName("")
                .mambuCodeName("")
                .build();

        when(getCodeGroupUseCase.getCodeGroups()).thenReturn(List.of(entity));

        ResponseEntity<List<CodeDTO>> response = codeGroupController.transaction();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        verify(getCodeGroupUseCase).getCodeGroups();
    }

    @Test
    void createCodeGroup() {
        String name = "Group1";
        CodeGroupEntity entity = CodeGroupEntity.builder().build();

        when(createCodeGroupUseCase.create(name, email)).thenReturn(entity);

        ResponseEntity<CodeGroupResponse> response = codeGroupController.createCodeGroup(name, email);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        verify(createCodeGroupUseCase).create(name, email);
    }

    @Test
    void getAllCodeGroups() {
        int page = 0;
        int size = 15;
        String name = "group";

        PageResult<CodeGroupEntity> pageResult = new PageResult<>(List.of(CodeGroupEntity.builder().build()), 1, 1, 1);

        when(findAllCodeGroups.find(page, size, name)).thenReturn(pageResult);

        ResponseEntity<PageResult<CodeGroupResponse>> response = codeGroupController.getAllCodeGroups(page, size, name);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        verify(findAllCodeGroups).find(page, size, name);
    }

    @Test
    void getAllGroups() {
        List<CodeGroupEntity> entities = List.of(CodeGroupEntity.builder().build());

        when(getAllGroupNames.getAllGroupNames()).thenReturn(entities);

        ResponseEntity<List<CodeGroupResponse>> response = codeGroupController.getAllGroups();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(getAllGroupNames).getAllGroupNames();
    }

    @Test
    void getCodeGroupById() {
        UUID id = UUID.randomUUID();
        CodeGroupEntity entity = CodeGroupEntity.builder().build();

        when(findByIdCodeGroupUseCase.findById(id)).thenReturn(entity);

        ResponseEntity<CodeGroupResponse> response = codeGroupController.getCodeGroupById(id);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(findByIdCodeGroupUseCase).findById(id);
    }

    @Test
    void patchCodeGroup() {
        UUID id = UUID.randomUUID();
        String name = "UpdatedName";

        doNothing().when(updateCodeGroupUseCase).update(id, name, email);

        ResponseEntity<CodeGroupResponse> response = codeGroupController.patchCodeGroup(id, name, email);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(updateCodeGroupUseCase).update(id, name, email);
    }

    @Test
    void deleteCodeGroup() {
        UUID id = UUID.randomUUID();

        doNothing().when(deleteCodeGroupUseCase).delete(id, email);

        ResponseEntity<Void> response = codeGroupController.deleteCodeGroup(id, email);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(deleteCodeGroupUseCase).delete(id, email);
    }
}
