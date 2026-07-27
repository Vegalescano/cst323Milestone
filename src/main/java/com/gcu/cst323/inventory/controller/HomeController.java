package com.gcu.cst323.inventory.controller;

import com.gcu.cst323.inventory.model.UserAccount;
import com.gcu.cst323.inventory.service.AuthService;
import com.gcu.cst323.inventory.service.CustomerService;
import com.gcu.cst323.inventory.service.OrderService;
import com.gcu.cst323.inventory.service.ProductService;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private final AuthService authService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final OrderService orderService;

    public HomeController(AuthService authService,
                          ProductService productService,
                          CustomerService customerService,
                          OrderService orderService) {
        this.authService = authService;
        this.productService = productService;
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @GetMapping({"/", "/login"})
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        Optional<UserAccount> userResult = authService.authenticate(username, password);

        if (userResult.isEmpty()) {
            model.addAttribute("loginError", "Invalid username or password.");
            return "login";
        }

        UserAccount user = userResult.get();

        session.setAttribute("loggedInUser", user.getUsername());
        session.setAttribute("loggedInRole", user.getRole());

        if (user.getCustomer() != null) {
            session.setAttribute("loggedInCustomerId", user.getCustomer().getCustomerId());
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam(defaultValue = "CUSTOMER") String role,
                           @RequestParam(required = false) String customerFirstName,
                           @RequestParam(required = false) String customerLastName,
                           @RequestParam(required = false) String customerEmail,
                           @RequestParam(required = false) String customerPhone,
                           @RequestParam(required = false) String customerAddress,
                           Model model) {
        try {
            authService.register(
                    username,
                    password,
                    role,
                    customerFirstName,
                    customerLastName,
                    customerEmail,
                    customerPhone,
                    customerAddress
            );

            model.addAttribute("registerSuccess", "User account created. You can now log in.");
        } catch (IllegalArgumentException exception) {
            model.addAttribute("registerError", exception.getMessage());
        }

        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object loggedInUser = session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", session.getAttribute("loggedInUser"));
        model.addAttribute("role", session.getAttribute("loggedInRole"));

        authService.findByUsername(loggedInUser.toString()).ifPresent(user -> {
            if (user.getCustomer() != null) {
                model.addAttribute("linkedCustomer", user.getCustomer());
            }
        });

        model.addAttribute("productCount", productService.countActiveProducts());
        model.addAttribute("customerCount", customerService.countCustomers());
        model.addAttribute("openOrderCount", orderService.countOpenOrders());
        model.addAttribute("lowStockProducts", productService.findLowStockProducts());

        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}