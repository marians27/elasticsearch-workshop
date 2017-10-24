package pl.marian.training.elasticsearch.springdata.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@Document(indexName = "es-workshop-2", type = "events")
public class Event {

    @Id
    private String id;

    private String name;

    private Long counter;

    private Date date;

}
