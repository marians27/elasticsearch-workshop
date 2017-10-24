package pl.marian.training.elasticsearch.springdata.repository;

import pl.marian.training.elasticsearch.springdata.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EventsRepository extends ElasticsearchRepository<Event, String> {

    List<Event> findByName(String name);

    List<Event> findByNameAndCounter(String name, Long counter);

    Page<Event> findByCounter(Long counter, Pageable pageable);

    @Query("{\"term\" : {\"name\" : \"?0\"}}")
    List<Event> searchCustom(String name);
}
