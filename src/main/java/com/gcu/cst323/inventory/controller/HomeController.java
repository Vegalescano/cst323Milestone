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
/**
 * Controller responsible for login, account registration, dashboard display, and logout.
 * Handles the main entry points for public and authenticated users.
 */
@Controller
public class HomeController {
    private final AuthService authService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final OrderService orderService;
    /**
     * Creates a HomeController with services used for authentication and dashboard summaries.
     *
     * @param authService service used for registration and login authentication
     * @param productService service used to retrieve product summary data
     * @param customerService service used to retrieve customer summary data
     * @param orderService service used to retrieve order summary data
     */
    public HomeController(AuthService authService,
                          ProductService productService,
                          CustomerService customerService,
                          OrderService orderService) {
        this.authService = authService;
        this.productService = productService;
        this.customerService = customerService;
        this.orderService = orderService;
    }
    /**
     * Displays the login and create account page.
     *
     * @return login page template
     */
    @GetMapping({"/", "/login"})
    public String loginPage() {
        return "login";
    }
    /**
     * Authenticates the user and creates a logged in session when credentials are valid.
     *
     * @param username username entered by the user
     * @param password password entered by the user
     * @param session current HTTP session used to store logged in user details
     * @param model Spring MVC model used to pass login error messages
     * @return redirect to dashboard when login succeeds, otherwise login page
     */
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
    /**
     * Registers a new user account and optionally creates linked customer information.
     *
     * @param username new username entered by the user
     * @param password new password entered by the user
     * @param role role selected for the new account
     * @param firstName customer first name
     * @param lastName customer last name
     * @param email customer email address
     * @param phone customer phone number
     * @param address customer address
     * @param model Spring MVC model used to pass registration error messages
     * @return redirect to login page after successful registration, otherwise login page
     */
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
    /**
     * Displays the authenticated dashboard with summary counts and low stock information.
     *
     * @param session current HTTP session containing the logged in user
     * @param model Spring MVC model used to pass dashboard data to the view
     * @return dashboard page or redirect to login when the user is not authenticated
     */
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
    /**
     * Ends the current user session and returns the user to the login page.
     *
     * @param session current HTTP session to invalidate
     * @return redirect to login page
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}