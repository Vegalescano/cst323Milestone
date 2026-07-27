package com.gcu.cst323.inventory.controller;

import com.gcu.cst323.inventory.service.CustomerService;
import com.gcu.cst323.inventory.service.OrderService;
import com.gcu.cst323.inventory.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;

    public OrderController(OrderService orderService,
                           CustomerService customerService,
                           ProductService productService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
    }

    private boolean canManageOrders(HttpSession session) {
        Object role = session.getAttribute("loggedInRole");

        if (role == null) {
            return false;
        }

        String userRole = role.toString();

        return userRole.equals("ADMIN")
                || userRole.equals("EMPLOYEE")
                || userRole.equals("MANAGER");
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        if (!canManageOrders(session)) {
            return "redirect:/dashboard";
        }

        model.addAttribute("orders", orderService.findAll());
        model.addAttribute("customers", customerService.findAll(null));
        model.addAttribute("products", productService.findAll(null));

        return "orders";
    }

    @PostMapping("/save")
    public String save(@RequestParam Long customerId,
                       @RequestParam Long productId,
                       @RequestParam Integer quantity,
                       HttpSession session) {
        if (!canManageOrders(session)) {
            return "redirect:/dashboard";
        }

        orderService.createOrder(customerId, productId, quantity);
        return "redirect:/orders";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               HttpSession session) {
        if (!canManageOrders(session)) {
            return "redirect:/dashboard";
        }

        orderService.updateStatus(id, status);
        return "redirect:/orders";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         HttpSession session) {
        if (!canManageOrders(session)) {
            return "redirect:/dashboard";
        }

        orderService.delete(id);
        return "redirect:/orders";
    }
}