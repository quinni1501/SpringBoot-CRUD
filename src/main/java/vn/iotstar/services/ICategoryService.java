package vn.iotstar.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Example;
import vn.iotstar.entity.Category;

public interface ICategoryService {

    Page<Category> findByNameContaining(String name, Pageable pageable);

    List<Category> findByNameContaining(String name);

    void deleteAll();

    void delete(Category entity);

    void deleteById(Long id);

    long count();

    <S extends Category> Optional<S> findOne(Example<S> example);

    Optional<Category> findById(Long id);

    List<Category> findAllById(Iterable<Long> ids);

    List<Category> findAll(Sort sort);

    Page<Category> findAll(Pageable pageable);

    List<Category> findAll();

    <S extends Category> S save(S entity);
}
