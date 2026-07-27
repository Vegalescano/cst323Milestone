package com.gcu.cst323.inventory.service;

import com.gcu.cst323.inventory.model.Customer;
import com.gcu.cst323.inventory.repository.CustomerRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

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

    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    public long countCustomers() {
        return customerRepository.count();
    }
}