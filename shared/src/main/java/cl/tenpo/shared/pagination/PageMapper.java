package cl.tenpo.shared.pagination;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public class PageMapper {
    public static <T, R> PageResult<R> map(Page<T> page, Function<T, R> mapper) {
        List<R> content = page.getContent().stream().map(mapper).toList();
        return new PageResult<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }
}
