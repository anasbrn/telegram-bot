package org.example.mcpserver.records;

public record Product(
        String name,
        String description,
        double price,
        int quantity
) {
}
