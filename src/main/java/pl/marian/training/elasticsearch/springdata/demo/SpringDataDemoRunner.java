package pl.marian.training.elasticsearch.springdata.demo;

import pl.marian.training.elasticsearch.springdata.configuration.SpringDataEsConfiguration;
import pl.marian.training.elasticsearch.springdata.model.Event;
import pl.marian.training.elasticsearch.springdata.repository.EventsRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

public class SpringDataDemoRunner {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                SpringDataEsConfiguration.class);
        EventsRepository repository = context.getBean(EventsRepository.class);

        System.out.println(String.format("There are %s events", repository.count()));
        repository.findAll().forEach(System.out::println);

        Event event1 = repository.save(createEvent(null, "Demo1", 0));
        System.out.println(String.format("Document inserted %s", event1));

        Event event2 = repository.save(createEvent("100", "Demo2", 0));
        System.out.println(String.format("Document inserted %s", event2));
    }

    private static Event createEvent(String id, String name, long counter) {
        Event event = new Event();
        event.setName(name);
        event.setCounter(counter);
        event.setDate(new Date());
        event.setId(id);
        return event;
    }
}
