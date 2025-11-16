package org.example.mcpserver.tools;

import org.example.mcpserver.records.Product;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AITools {
    @Tool
    public Product getProduct(@ToolParam String name) {
        return new Product(name, "Product description", 150.0, 20);
    }

    @Tool
    public List<Product> getAllProducts(@ToolParam String name) {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Laptop", "Laptop description", 4000, 10));
        products.add(new Product("Phone", "Phone description", 2000, 20));
        products.add(new Product("Mouse", "Mouse description", 300, 40));

        return products;
    }
}
