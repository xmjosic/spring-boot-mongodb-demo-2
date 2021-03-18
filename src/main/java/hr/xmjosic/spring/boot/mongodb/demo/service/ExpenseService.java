package hr.xmjosic.spring.boot.mongodb.demo.service;

import hr.xmjosic.spring.boot.mongodb.demo.exception.SpringBootMongodbDemoException;
import hr.xmjosic.spring.boot.mongodb.demo.model.Expense;
import hr.xmjosic.spring.boot.mongodb.demo.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public void addExpense(Expense expense) {
        expenseRepository.insert(expense);
    }

    public void updateExpense(Expense expense) {
        Expense savedExpense = expenseRepository.findById(expense.getId())
                .orElseThrow(() -> new SpringBootMongodbDemoException(
                        String.format("There is no expense by ID %s", expense.getId())
                ));

        savedExpense.setExpenseName(expense.getExpenseName());
        savedExpense.setExpenseCategory(expense.getExpenseCategory());
        savedExpense.setExpenseAmount(expense.getExpenseAmount());

        expenseRepository.save(savedExpense);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseByName(String name) {
        return expenseRepository.findByName(name)
                .orElseThrow(() -> new SpringBootMongodbDemoException(
                        String.format("There is no expense by name %s", name)
                ));
    }

    public void deleteExpense(String id) {
        expenseRepository.deleteById(id);
    }

}
