package com.gcu.cst323.inventory.controller;

import com.gcu.cst323.inventory.model.Customer;
import com.gcu.cst323.inventory.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customers")
/**
 * Controller responsible for customer management pages.
 * Handles customer listing, searching, editing, saving, and deletion.
 */
public class CustomerController {
    private final CustomerService customerService;
    /**
     * Creates a CustomerController with the required customer service.
     *
     * @param customerService service used to manage customer records
     */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    /**
     * Checks whether the current user role is allowed to manage customer records.
     *
     * @param session current HTTP session containing the logged in user role
     * @return true when the user can manage customers, otherwise false
     */
    private boolean canManageCustomers(HttpSession session) {
        Object role = session.getAttribute("loggedInRole");

        if (role == null) {
            return false;
        }

        String userRole = role.toString();

        return userRole.equals("ADMIN") || userRole.equals("EMPLOYEE");
    }
    /**
     * Displays the customers page and applies search filtering when a search value is provided.
     *
     * @param search optional search text entered by the user
     * @param session current HTTP session used to verify access
     * @param model Spring MVC model used to pass customer data to the view
     * @return customers page or redirect when access is denied
     */
    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       HttpSession session,
                       Model model) {
        if (!canManageCustomers(session)) {
            return "redirect:/dashboard";
        }

        model.addAttribute("customer", new Customer());
        model.addAttribute("customers", customerService.findAll(search));
        model.addAttribute("search", search);

        return "customers";
    }
    /**
     * Loads a selected customer record into the edit form.
     *
     * @param id customer id selected for editing
     * @param search optional customer search text
     * @param session current HTTP session used to verify user access
     * @param model Spring MVC model used to pass customer data to the view
     * @return customers page with the selected customer loaded for editing
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam(required = false) String search,
                       HttpSession session,
                       Model model) {
        if (!canManageCustomers(session)) {
            return "redirect:/dashboard";
        }

        model.addAttribute("customer", customerService.findById(id));
        model.addAttribute("customers", customerService.findAll(search));
        model.addAttribute("search", search);

        return "customers";
    }
    /**
     * Saves a new or edited customer record.
     *
     * @param customer customer form data submitted by the user
     * @param bindingResult validation results for the customer form
     * @param session current HTTP session used to verify access
     * @param model Spring MVC model used to return validation errors if needed
     * @return redirect to customers page after successful save
     */
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("customer") Customer customer,
                       BindingResult bindingResult,
                       HttpSession session,
                       Model model) {
        if (!canManageCustomers(session)) {
            return "redirect:/dashboard";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("customers", customerService.findAll(null));
            return "customers";
        }

        customerService.save(customer);
        return "redirect:/customers";
    }
    /**
     * Deletes a customer record when the customer is not linked to existing orders.
     *
     * @param id customer id selected for deletion
     * @param session current HTTP session used to verify user access
     * @param redirectAttributes redirect attributes used to pass success or error messages
     * @return redirect to customers page after the delete request
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        if (!canManageCustomers(session)) {
            return "redirect:/dashboard";
        }

        try {
            customerService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Customer deleted successfully.");
        } catch (IllegalStateException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/customers";
    }
}