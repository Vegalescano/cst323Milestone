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

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private boolean canManageProducts(HttpSession session) {
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