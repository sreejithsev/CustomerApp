package com.dev.customerapp.controller;

import com.dev.customerapp.entity.Customer;
import com.dev.customerapp.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;

    @GetMapping("/new")
    public String showCustomerForm(Customer customer) {
        return "add";
    }

    @PostMapping("/add")
    public String getCustomers(@Valid Customer customer, BindingResult result){
        if (result.hasErrors()){
            return "add";
        }
        customerRepository.save(customer);
        return "redirect:customer";
    }

    @GetMapping("/customer")
    public String getCustomers(Model model){
        List<Customer> allCustomers = customerRepository.findAll();
        model.addAttribute("customers", allCustomers.isEmpty() ? null : allCustomers);
        return "customers";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("customer", customer);
        return "update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid Customer customer, BindingResult result) {
        if (result.hasErrors()) {
            customer.setId(id);
            return "update";
        }
        customerRepository.save(customer);
        return "redirect:/customer";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        customerRepository.delete(customer);
        return "redirect:/customer";
    }
}
