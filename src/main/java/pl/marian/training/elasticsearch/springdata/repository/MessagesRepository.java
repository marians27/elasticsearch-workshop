package pl.marian.training.elasticsearch.springdata.repository;

import pl.marian.training.elasticsearch.springdata.model.Message;
import pl.marian.training.elasticsearch.springdata.model.MessageCategory;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

public interface MessagesRepository extends ElasticsearchCrudRepository<Message, String> {

    List<Message> findByCategory(MessageCategory category);

    @Query("{\"match\" : {\"content\" : \"?0\"}}")
    List<Message> searchInContent(String content);

    long countByPriorityAndCategoryIn(Integer priority, List<MessageCategory> category);

}
