package cl.tenpo.usecases;

import cl.tenpo.entities.CodeGroupEntity;

import java.util.List;

public interface GetAllGroupNamesUseCase {
    List<CodeGroupEntity> getAllGroupNames();
}
