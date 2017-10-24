package pl.marian.training.elasticsearch.springdata.repository;

import pl.marian.training.elasticsearch.springdata.configuration.SpringDataEsConfiguration;
import pl.marian.training.elasticsearch.springdata.model.Message;
import pl.marian.training.elasticsearch.springdata.model.MessageCategory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static pl.marian.training.elasticsearch.springdata.model.MessageCategory.FINANCIAL;
import static pl.marian.training.elasticsearch.springdata.model.MessageCategory.MEDICAL;
import static pl.marian.training.elasticsearch.springdata.model.MessageCategory.OTHER;
import static pl.marian.training.elasticsearch.springdata.model.MessageCategory.TECHNICAL;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringDataEsConfiguration.class)
public class MessagesRepositoryTest {

    @Autowired
    private MessagesRepository messagesRepository;

    @Before
    public void populateTestData() {
        messagesRepository.deleteAll();
        messagesRepository.saveAll(asList(
                message(FINANCIAL, new Date(), 2, "Dollar index slips, still on track for biggest weekly gain since February"),
                message(FINANCIAL, new Date(), 1, "Dollar index hits 1-month high as Republican tax-plan details emerge"),
                message(MEDICAL, new Date(), 4, "Parkinson's breakthrough: New disease-causing mechanism found"),
                message(MEDICAL, new Date(), 5, "HPV and cancer: Key mechanism may suggest treatment"),
                message(MEDICAL, new Date(), 7, "Cancer causing chemicals found"),
                message(MEDICAL, new Date(), 1, "A new treatment for this deadly brain tumor."),
                message(TECHNICAL, new Date(), 7, "Apple Unveils Highly Secure, High-Priced iPhones"),
                message(TECHNICAL, new Date(), 10, "Apple's Worthy iPhone 8 Models May Languish in X's Shadow"),
                message(TECHNICAL, new Date(), 2, "The New iPhones: Apple's Strangely Wrongheaded Pivot"),
                message(TECHNICAL, new Date(), 7, "SQL Server 2017 Embraces Linux, Docker"),
                message(OTHER, new Date(), 8, "Catalonia referendum: Violence as police block voting"),
                message(OTHER, new Date(), 7, "Trump to Tillerson: N Korea negotiations a waste of time"),
                message(OTHER, new Date(), 4, "North Korea nuclear tests: What did they achieve?")
        ));
    }

    @Test
    public void findByCategoryTest() {
        List<Message> result = messagesRepository.findByCategory(MEDICAL);

        print(result);
        assertThat(result).hasSize(4);
    }

    @Test
    public void countByPriorityAndCategoryInTest() {
        long count = messagesRepository.countByPriorityAndCategoryIn(7, asList(TECHNICAL, OTHER));

        assertThat(count).isEqualTo(3);
    }

    @Test
    public void searchInContentTest() {
        List<Message> result = messagesRepository.searchInContent("Cancer treatment");

        print(result);
        assertThat(result).hasSize(3);
    }

    private void print(List<Message> result) {
        result.forEach(System.out::println);
    }

    private Message message(MessageCategory category, Date postDate, Integer priority, String content) {
        Message message = new Message();
        message.setCategory(category);
        message.setPostDate(postDate);
        message.setPriority(priority);
        message.setContent(content);
        return message;
    }
}