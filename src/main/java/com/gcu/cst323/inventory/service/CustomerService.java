package com.gcu.cst323.inventory.service;

import com.gcu.cst323.inventory.model.Customer;
import com.gcu.cst323.inventory.repository.CustomerRepository;
import com.gcu.cst323.inventory.repository.OrderRecordRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
/**
 * Creates an AuthService with repositories required for account and customer access.
 *
 * @param userAccountRepository repository used to access user account records
 * @param customerRepository repository used to access customer records
 */
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final OrderRecordRepository orderRecordRepository;
    /**
     * Creates a CustomerService with repositories required for customer and order validation.
     *
     * @param customerRepository repository used to access customer records
     * @param orderRecordRepository repository used to check customer order relationships
     */
    public CustomerService(CustomerRepository customerRepository,
                           OrderRecordRepository orderRecordRepository) {
        this.customerRepository = customerRepository;
        this.orderRecordRepository = orderRecordRepository;
    }
    /**
     * Creates a CustomerService with repositories required for customer and order validation.
     *
     * @param customerRepository repository used to access customer records
     * @param orderRecordRepository repository used to check customer order relationships
     */
    public List<Customer> findAll(String search) {
        if (search != null && !search.isBlank()) {
            String query = search.trim();

            return customerRepository
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            query, query, query)
                    .stream()
                    .sorted(Comparator.comparing(Customer::getLastName))
                    .toList();
        }

        return customerRepository.findAll().stream()
                .sorted(Comparator.comparing(Customer::getLastName))
                .toList();
    }
    /**
     * Finds all customers or filters customers by search text.
     *
     * @param search optional search text for first name, last name, or email
     * @return list of matching customers
     */
    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
    }
    /**
     * Finds all customers or filters customers by search text.
     *
     * @param search optional search text for first name, last name, or email
     * @return list of matching customers
     */
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
    /**
     * Finds a customer by id.
     *
     * @param id customer id to search for
     * @return matching customer
     * @throws IllegalArgumentException if the customer is not found
     */
    public void delete(Long id) {
        if (orderRecordRepository.existsByCustomer_CustomerId(id)) {
            throw new IllegalStateException(
                    "Cannot delete this customer because the customer already has orders. " +
                    "Keep the customer record for order history, or delete the related orders first."
            );
        }

        customerRepository.deleteById(id);
    }
    /**
     * Saves a new or edited customer record.
     *
     * @param customer customer record to save
     * @return saved customer
     */
    public long countCustomers() {
        return customerRepository.count();
    }
}