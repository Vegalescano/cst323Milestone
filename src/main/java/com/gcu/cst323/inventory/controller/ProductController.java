package com.gcu.cst323.inventory.controller;

import com.gcu.cst323.inventory.model.Product;
import com.gcu.cst323.inventory.service.ProductService;
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
/**
 * Controller responsible for product management pages.
 * Handles product listing, searching, editing, saving, and deactivation.
 */
@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    /**
     * Creates a ProductController with the required product service.
     *
     * @param productService service used to manage product records
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    /**
     * Checks whether the current user role is allowed to manage products.
     *
     * @param session current HTTP session containing the logged in user role
     * @return true when the user can manage products, otherwise false
     */
    private boolean canManageProducts(HttpSession session) {
        Object role = session.getAttribute("loggedInRole");

        if (role == null) {
            return false;
        }

        String userRole = role.toString();

        return userRole.equals("ADMIN") || userRole.equals("EMPLOYEE");
    }
    /**
     * Displays the products page and applies search filtering when provided.
     *
     * @param search optional product search text
     * @param session current HTTP session used to verify user access
     * @param model Spring MVC model used to pass product data to the view
     * @return products page when authorized, otherwise redirect to dashboard
     */
    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       HttpSession session,
                       Model model) {
        if (!canManageProducts(session)) {
            return "redirect:/dashboard";
        }

        Product product = new Product();
        product.setActive(true);

        model.addAttribute("product", product);
        model.addAttribute("products", productService.findAll(search));
        model.addAttribute("search", search);

        return "products";
    }
    /**
     * Loads a selected product record into the edit form.
     *
     * @param id product id selected for editing
     * @param search optional product search text
     * @param session current HTTP session used to verify user access
     * @param model Spring MVC model used to pass product data to the view
     * @return products page with the selected product loaded for editing
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam(required = false) String search,
                       HttpSession session,
                       Model model) {
        if (!canManageProducts(session)) {
            return "redirect:/dashboard";
        }

        model.addAttribute("product", productService.findById(id));
        model.addAttribute("products", productService.findAll(search));
        model.addAttribute("search", search);

        return "products";
    }
    /**
     * Saves a new or edited product record submitted from the product form.
     *
     * @param product product form data submitted by the user
     * @param bindingResult validation result for the product form
     * @param session current HTTP session used to verify user access
     * @param model Spring MVC model used to return validation errors if needed
     * @return redirect to products page after a successful save
     */
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("product") Product product,
                       BindingResult bindingResult,
                       HttpSession session,
                       Model model) {
        if (!canManageProducts(session)) {
            return "redirect:/dashboard";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("products", productService.findAll(null));
            model.addAttribute("search", null);
            return "products";
        }

        productService.save(product);
        return "redirect:/products";
    }
    /**
     * Deactivates a product so it no longer appears as an active inventory item.
     *
     * @param id product id selected for deactivation
     * @param session current HTTP session used to verify user access
     * @return redirect to products page after product deactivation
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         HttpSession session) {
        if (!canManageProducts(session)) {
            return "redirect:/dashboard";
        }

        productService.deactivate(id);
        return "redirect:/products";
    }
}