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
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    private boolean canManageCustomers(HttpSession session) {
        Object role = session.getAttribute("loggedInRole");

        if (role == null) {
            return false;
        }

        String userRole = role.toString();

        return userRole.equals("ADMIN") || userRole.equals("EMPLOYEE");
    }

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