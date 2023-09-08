package com.example.zoo.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * The main purpose for this controller is to represent a home page,
 * from which users can go wherever they want
 */
@Controller
public class HomeController {
    @GetMapping("/")
    public String getAll() {
        return "home";
    }
}
