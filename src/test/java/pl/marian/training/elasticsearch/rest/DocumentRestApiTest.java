package pl.marian.training.elasticsearch.rest;

import pl.marian.training.elasticsearch.springdata.configuration.SpringDataEsConfiguration;
import pl.marian.training.elasticsearch.springdata.model.Event;
import pl.marian.training.elasticsearch.springdata.repository.EventsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/*
    1) Add new document to index es-workshop-2 and type events:
        - having field "name" set to value test1
	    - having field "counter" set to value 44
    2) Add another document to the same index and type
	    - having field "name" set to value test2
	    - having field "date" set to current date
    3) Update document
        - Create document with id 999 in the index es-workshop-2 and type events
        - Update document with id 999 in the index es-workshop-2 and type events by setting counter to 2
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringDataEsConfiguration.class)
public class DocumentRestApiTest {

    @Autowired
    private EventsRepository eventsRepository;

    @Test
    public void test1DocumentCreated() throws Exception {
        List<Event> result = eventsRepository.findByNameAndCounter("test1", 44L);

        assertThat(result).isNotEmpty();
    }

    @Test
    public void test2DocumentCreated() throws Exception {
        List<Event> result = eventsRepository.findByName("test2");

        assertThat(result).isNotEmpty();
    }

    @Test
    public void test2DocumentHasCorrectDate() throws Exception {
        List<Event> result = eventsRepository.findByName("test2");

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getDate()).isToday();
    }

    @Test
    public void document999ExistsWithProperCounter() throws Exception {
        Optional<Event> result = eventsRepository.findById("999");

        assertThat(result).isPresent();
        assertThat(result.orElse(null).getCounter()).isEqualTo(2);
    }

    @Test
    public void document999ShouldHaveHigherVersionThanOne() throws Exception {
        HttpHeaders headers = null;
        try {
            headers = new RestTemplate().headForHeaders("http://localhost:9200/es-workshop-2/events/999?version=1");
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
        assertThat(headers)
                .withFailMessage("Document should exists with version greater than 1. " +
                        "Currently it exists with version 1, so it was not updated.")
                .isNull();
    }
}
