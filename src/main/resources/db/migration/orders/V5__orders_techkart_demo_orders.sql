SET search_path TO orders;

-- The three original sample orders now show the TechKart products that replaced those product codes.
UPDATE orders SET product_name = 'Voltix One 5G 8GB/128GB', product_price = 18999 WHERE id = 1;
UPDATE orders SET product_name = 'Voltix One Pro 5G 12GB/256GB', product_price = 28999 WHERE id = 2;
UPDATE orders SET product_name = 'Voltix Lite 4G 6GB/128GB', product_price = 10999 WHERE id = 3;

-- Six orders for the demo customer (user id 3) in different statuses.
insert into orders(id, order_number, customer_name, customer_email, customer_phone, delivery_address, product_code, product_name, product_price, quantity, status, comments, created_at, user_id) values
(4, 'b8d0e366-5962-4d3e-967c-ee31d9e68c87', 'Karthik', 'karthik@techkart.test', '9840012345', 'Karthik, Anna Nagar, Chennai, India', 'P105', 'Nimbus Air 14 i5 16GB/512GB', 54999, 1, 'NEW', null, CURRENT_TIMESTAMP - INTERVAL '0 days', 3),
(5, '267b321e-8e9b-48e4-a6d2-dddc665a7a7d', 'Karthik', 'karthik@techkart.test', '9840012345', 'Karthik, Anna Nagar, Chennai, India', 'P110', 'Auris Buds 2 True Wireless', 1999, 2, 'NEW', null, CURRENT_TIMESTAMP - INTERVAL '1 days', 3),
(6, '3ee567c0-ada5-4744-a1ed-7d2a8def149e', 'Karthik', 'karthik@techkart.test', '9840012345', 'Karthik, Anna Nagar, Chennai, India', 'P143', 'Kordo Mechanical Keyboard K8', 4499, 1, 'IN_PROCESS', null, CURRENT_TIMESTAMP - INTERVAL '3 days', 3),
(7, '2de2ba6f-f6ff-4702-bf10-d22a38cea030', 'Karthik', 'karthik@techkart.test', '9840012345', 'Karthik, Anna Nagar, Chennai, India', 'P100', 'Voltix One 5G 8GB/128GB', 18999, 1, 'DELIVERED', null, CURRENT_TIMESTAMP - INTERVAL '20 days', 3),
(8, '8f1df339-1b6a-4b94-a200-defc9601e0ce', 'Karthik', 'karthik@techkart.test', '9840012345', 'Karthik, Anna Nagar, Chennai, India', 'P130', 'Pulso Fit Band 3', 2499, 1, 'DELIVERED', null, CURRENT_TIMESTAMP - INTERVAL '45 days', 3),
(9, 'fdaf4b47-5b1c-4abc-aa9b-c7dc2bba592f', 'Karthik', 'karthik@techkart.test', '9840012345', 'Karthik, Anna Nagar, Chennai, India', 'P113', 'Beatlo Go Bluetooth Speaker', 2499, 1, 'CANCELLED', 'Cancelled by customer', CURRENT_TIMESTAMP - INTERVAL '10 days', 3);
