package hr.xmjosic.spring.boot.mongodb.demo;

import hr.xmjosic.spring.boot.mongodb.demo.model.Expense;
import hr.xmjosic.spring.boot.mongodb.demo.model.ExpenseCategory;
import hr.xmjosic.spring.boot.mongodb.demo.repository.ExpenseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class SpringBootMongodbDemo2ApplicationTests {

	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

	{
		mongoDBContainer.start();
	}

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Autowired
	private ExpenseRepository expenseRepository;

	@Test
	@DisplayName("Should find expense by name")
	void shouldFindExpenseByName() {
		Expense expectedExpense = new Expense();
		expectedExpense.setExpenseName("Paid tutorials");
		expectedExpense.setExpenseCategory(ExpenseCategory.MISCELLANEOUS);
		expectedExpense.setExpenseAmount(BigDecimal.TEN);

		expenseRepository.save(expectedExpense);

		Expense actualExpense = expenseRepository.findByName("Paid tutorials").orElseThrow();

		assertEquals(expectedExpense.getExpenseName(), actualExpense.getExpenseName());
		assertEquals(expectedExpense.getExpenseCategory(), actualExpense.getExpenseCategory());
		assertEquals(expectedExpense.getExpenseAmount(), actualExpense.getExpenseAmount());
	}

}
