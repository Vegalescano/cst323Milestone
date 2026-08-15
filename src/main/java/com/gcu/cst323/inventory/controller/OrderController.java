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
/**
 * Controller responsible for order management pages.
 * Handles order listing, creation, status updates, and deletion.
 */
@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;
    /**
     * Creates an OrderController with the services required to manage orders.
     *
     * @param orderService service used to manage order records
     * @param customerService service used to load customer choices for orders
     * @param productService service used to load product choices for orders
     */
    public OrderController(OrderService orderService,
                           CustomerService customerService,
                           ProductService productService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
    }
    /**
     * Checks whether the current user role is allowed to manage orders.
     *
     * @param session current HTTP session containing the logged in user role
     * @return true when the user can manage orders, otherwise false
     */
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
    /**
     * Displays the orders page with existing orders, customers, and products.
     *
     * @param session current HTTP session used to verify user access
     * @param model Spring MVC model used to pass order page data to the view
     * @return orders page when authorized, otherwise redirect to dashboard
     */
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
    /**
     * Creates a new order using the selected customer, product, and quantity.
     *
     * @param customerId selected customer id
     * @param productId selected product id
     * @param quantity quantity entered by the user
     * @param session current HTTP session used to verify user access
     * @return redirect to orders page after the order is created
     */
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
    /**
     * Updates the status of an existing order.
     *
     * @param id order id selected for status update
     * @param status new order status selected by the user
     * @param session current HTTP session used to verify user access
     * @return redirect to orders page after the status update
     */
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
    /**
     * Deletes an order record and its related order items.
     *
     * @param id order id selected for deletion
     * @param session current HTTP session used to verify user access
     * @return redirect to orders page after deletion
     */
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