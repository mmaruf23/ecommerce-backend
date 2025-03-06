    package com.solo.ecommerce.model;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDateTime;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "order_histories")
    public class OrderHistory {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(name = "status",nullable = false)
        private Status status;

        @Column(name = "change_at", nullable = false)
        private LocalDateTime changeAt;

        @Column(name = "note")
        private String note;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "order_id", nullable = false)
        private Order order;
    }
