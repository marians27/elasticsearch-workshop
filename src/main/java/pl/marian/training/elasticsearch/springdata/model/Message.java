package pl.marian.training.elasticsearch.springdata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@Document(indexName = "es-workshop-2", type = "messages")
public class Message {

    @Id
    @JsonIgnore
    private String id;

    private MessageCategory category;

    private String content;

    private Date postDate;

    private Integer priority;

}
