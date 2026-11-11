SET search_path TO catalog;

-- TechKart: the store sells electronics. Category and brand let the assistant filter products.
ALTER TABLE products ADD COLUMN category text;
ALTER TABLE products ADD COLUMN brand text;

-- Existing product codes P100-P114 are kept (inventory and orders refer to them) and re-described.
UPDATE products SET name = 'Voltix One 5G 8GB/128GB', description = '6.6-inch 120Hz AMOLED, 50MP camera, 5000mAh battery, 8GB RAM, 128GB storage.', image_url = '/images/products/P100.svg', price = 18999, category = 'Phones', brand = 'Voltix' WHERE code = 'P100';
UPDATE products SET name = 'Voltix One Pro 5G 12GB/256GB', description = '6.7-inch 120Hz AMOLED, 108MP camera, 5200mAh battery, 12GB RAM, 256GB storage.', image_url = '/images/products/P101.svg', price = 28999, category = 'Phones', brand = 'Voltix' WHERE code = 'P101';
UPDATE products SET name = 'Voltix Lite 4G 6GB/128GB', description = '6.5-inch 90Hz LCD, 50MP camera, 5000mAh battery, 6GB RAM, 128GB storage.', image_url = '/images/products/P102.svg', price = 10999, category = 'Phones', brand = 'Voltix' WHERE code = 'P102';
UPDATE products SET name = 'Zenora Z9 5G 8GB/256GB', description = '6.4-inch 120Hz OLED, 64MP camera with OIS, 4700mAh battery, 8GB RAM, 256GB storage.', image_url = '/images/products/P103.svg', price = 34999, category = 'Phones', brand = 'Zenora' WHERE code = 'P103';
UPDATE products SET name = 'Zenora Z9 Ultra 5G 16GB/512GB', description = '6.8-inch 144Hz OLED, 200MP camera, 5000mAh battery, 16GB RAM, 512GB storage.', image_url = '/images/products/P104.svg', price = 74999, category = 'Phones', brand = 'Zenora' WHERE code = 'P104';
UPDATE products SET name = 'Nimbus Air 14 i5 16GB/512GB', description = '14-inch FHD IPS, Core i5, 16GB RAM, 512GB SSD, 1.3kg, 10-hour battery.', image_url = '/images/products/P105.svg', price = 54999, category = 'Laptops', brand = 'Nimbus' WHERE code = 'P105';
UPDATE products SET name = 'Nimbus Air 14 i7 16GB/1TB', description = '14-inch 2.8K OLED, Core i7, 16GB RAM, 1TB SSD, 1.3kg, 12-hour battery.', image_url = '/images/products/P106.svg', price = 79999, category = 'Laptops', brand = 'Nimbus' WHERE code = 'P106';
UPDATE products SET name = 'Nimbus Book 15 Ryzen 5 8GB/512GB', description = '15.6-inch FHD, Ryzen 5, 8GB RAM, 512GB SSD, 1.7kg. Good for students.', image_url = '/images/products/P107.svg', price = 39999, category = 'Laptops', brand = 'Nimbus' WHERE code = 'P107';
UPDATE products SET name = 'Aerion Blade 16 RTX 16GB/1TB', description = '16-inch 165Hz QHD, Core i7, RTX graphics, 16GB RAM, 1TB SSD. Built for gaming.', image_url = '/images/products/P108.svg', price = 119999, category = 'Laptops', brand = 'Aerion' WHERE code = 'P108';
UPDATE products SET name = 'Aerion Dev 14 Ryzen 7 32GB/1TB', description = '14-inch 2.5K IPS, Ryzen 7, 32GB RAM, 1TB SSD, Linux-friendly. Built for developers.', image_url = '/images/products/P109.svg', price = 94999, category = 'Laptops', brand = 'Aerion' WHERE code = 'P109';
UPDATE products SET name = 'Auris Buds 2 True Wireless', description = 'Bluetooth 5.3 earbuds, 30-hour battery with case, IPX4 sweat resistant.', image_url = '/images/products/P110.svg', price = 1999, category = 'Audio', brand = 'Auris' WHERE code = 'P110';
UPDATE products SET name = 'Auris Buds Pro ANC', description = 'Active noise cancelling earbuds, 6 mics, 36-hour battery with case, wireless charging.', image_url = '/images/products/P111.svg', price = 5999, category = 'Audio', brand = 'Auris' WHERE code = 'P111';
UPDATE products SET name = 'Auris Studio Over-Ear ANC', description = 'Over-ear noise cancelling headphones, 50-hour battery, foldable, USB-C fast charge.', image_url = '/images/products/P112.svg', price = 12999, category = 'Audio', brand = 'Auris' WHERE code = 'P112';
UPDATE products SET name = 'Beatlo Go Bluetooth Speaker', description = '10W portable speaker, 12-hour battery, IPX7 waterproof, built-in mic.', image_url = '/images/products/P113.svg', price = 2499, category = 'Audio', brand = 'Beatlo' WHERE code = 'P113';
UPDATE products SET name = 'Beatlo Boom 60W Party Speaker', description = '60W speaker with deep bass, RGB lights, 15-hour battery, karaoke mic input.', image_url = '/images/products/P114.svg', price = 8999, category = 'Audio', brand = 'Beatlo' WHERE code = 'P114';

insert into products(code, name, description, image_url, price, category, brand) values
('P115', 'Voltix Note 5G 8GB/256GB', '6.7-inch 120Hz AMOLED with stylus, 50MP camera, 5000mAh battery, 8GB RAM, 256GB storage.', '/images/products/P115.svg', 23999, 'Phones', 'Voltix'),
('P116', 'Voltix Fold 5G 12GB/512GB', '7.6-inch foldable AMOLED, 50MP camera, 4400mAh battery, 12GB RAM, 512GB storage.', '/images/products/P116.svg', 109999, 'Phones', 'Voltix'),
('P117', 'Zenora Z7 5G 6GB/128GB', '6.5-inch 120Hz LCD, 50MP camera, 5000mAh battery, 6GB RAM, 128GB storage.', '/images/products/P117.svg', 14999, 'Phones', 'Zenora'),
('P118', 'Zenora Z8 5G 8GB/128GB', '6.5-inch 120Hz AMOLED, 64MP camera, 5000mAh battery, 8GB RAM, 128GB storage.', '/images/products/P118.svg', 21999, 'Phones', 'Zenora'),
('P119', 'Zenora Mini 5G 8GB/256GB', '5.9-inch compact OLED, 50MP camera with OIS, 4000mAh battery, 8GB RAM, 256GB storage.', '/images/products/P119.svg', 44999, 'Phones', 'Zenora'),
('P120', 'Nimbus Book 15 i3 8GB/256GB', '15.6-inch FHD, Core i3, 8GB RAM, 256GB SSD, 1.7kg. Entry-level work laptop.', '/images/products/P120.svg', 29999, 'Laptops', 'Nimbus'),
('P121', 'Nimbus Air 13 i5 8GB/512GB', '13.3-inch FHD IPS, Core i5, 8GB RAM, 512GB SSD, 1.1kg, 11-hour battery.', '/images/products/P121.svg', 49999, 'Laptops', 'Nimbus'),
('P122', 'Nimbus Pro 16 i9 32GB/1TB', '16-inch 4K OLED, Core i9, 32GB RAM, 1TB SSD, RTX graphics. For creators.', '/images/products/P122.svg', 164999, 'Laptops', 'Nimbus'),
('P123', 'Aerion Blade 15 RTX 16GB/512GB', '15.6-inch 144Hz FHD, Core i5, RTX graphics, 16GB RAM, 512GB SSD.', '/images/products/P123.svg', 84999, 'Laptops', 'Aerion'),
('P124', 'Aerion Dev 16 Ryzen 9 64GB/2TB', '16-inch 2.5K IPS, Ryzen 9, 64GB RAM, 2TB SSD, Linux-friendly workstation.', '/images/products/P124.svg', 149999, 'Laptops', 'Aerion'),
('P125', 'Auris Buds Lite', 'Budget Bluetooth 5.3 earbuds, 20-hour battery with case, touch controls.', '/images/products/P125.svg', 999, 'Audio', 'Auris'),
('P126', 'Auris Neckband Pro', 'Neckband earphones, 40-hour battery, fast charge, dual device pairing.', '/images/products/P126.svg', 1499, 'Audio', 'Auris'),
('P127', 'Auris Studio Wired', 'Wired over-ear headphones, 40mm drivers, 3.5mm jack, detachable cable.', '/images/products/P127.svg', 3499, 'Audio', 'Auris'),
('P128', 'Beatlo Bar 120W Soundbar', '120W soundbar with wireless subwoofer, HDMI ARC, Bluetooth, wall mountable.', '/images/products/P128.svg', 11999, 'Audio', 'Beatlo'),
('P129', 'Beatlo Mic USB Condenser', 'USB condenser microphone with stand and pop filter. For calls, podcasts and reels.', '/images/products/P129.svg', 3999, 'Audio', 'Beatlo'),
('P130', 'Pulso Fit Band 3', 'Fitness band, 1.5-inch AMOLED, heart rate and SpO2, 14-day battery, 5ATM.', '/images/products/P130.svg', 2499, 'Wearables', 'Pulso'),
('P131', 'Pulso Watch S', 'Smartwatch, 1.8-inch AMOLED, Bluetooth calling, 100+ sport modes, 7-day battery.', '/images/products/P131.svg', 3999, 'Wearables', 'Pulso'),
('P132', 'Pulso Watch Pro GPS', 'Smartwatch with built-in GPS, 1.4-inch AMOLED, metal body, 10-day battery.', '/images/products/P132.svg', 8999, 'Wearables', 'Pulso'),
('P133', 'Pulso Watch Ultra LTE', 'Rugged LTE smartwatch, sapphire glass, dual-band GPS, 5-day battery.', '/images/products/P133.svg', 24999, 'Wearables', 'Pulso'),
('P134', 'Pulso Ring Gen 1', 'Smart ring for sleep and activity tracking, titanium, 6-day battery.', '/images/products/P134.svg', 14999, 'Wearables', 'Pulso'),
('P135', 'Pulso Kids Watch', 'Kids smartwatch with GPS location, SOS button, camera and school mode.', '/images/products/P135.svg', 4999, 'Wearables', 'Pulso'),
('P136', 'Lumina Tab 11 8GB/128GB', '11-inch 2K 90Hz display, 8GB RAM, 128GB storage, 8000mAh battery, quad speakers.', '/images/products/P136.svg', 19999, 'Tablets', 'Lumina'),
('P137', 'Lumina Tab 11 Pro 12GB/256GB', '11.5-inch 2.8K 144Hz display, 12GB RAM, 256GB storage, stylus and keyboard support.', '/images/products/P137.svg', 34999, 'Tablets', 'Lumina'),
('P138', 'Lumina Tab 8 4GB/64GB', '8.7-inch HD display, 4GB RAM, 64GB storage, 5100mAh battery. Light and portable.', '/images/products/P138.svg', 9999, 'Tablets', 'Lumina'),
('P139', 'Lumina Tab 13 Ultra 16GB/512GB', '13-inch 3K OLED 120Hz, 16GB RAM, 512GB storage, 10000mAh battery.', '/images/products/P139.svg', 64999, 'Tablets', 'Lumina'),
('P140', 'Kordo 65W GaN Charger', '65W USB-C GaN fast charger, 3 ports, charges laptops, phones and tablets.', '/images/products/P140.svg', 1999, 'Accessories', 'Kordo'),
('P141', 'Kordo 20000mAh Power Bank', '20000mAh power bank, 22.5W fast charging, USB-C in and out, 3 devices at once.', '/images/products/P141.svg', 1799, 'Accessories', 'Kordo'),
('P142', 'Kordo USB-C Hub 7-in-1', 'USB-C hub with HDMI 4K, 3 USB-A, SD and microSD readers, 100W pass-through.', '/images/products/P142.svg', 2499, 'Accessories', 'Kordo'),
('P143', 'Kordo Mechanical Keyboard K8', 'Tenkeyless mechanical keyboard, hot-swappable switches, Bluetooth and USB-C.', '/images/products/P143.svg', 4499, 'Accessories', 'Kordo'),
('P144', 'Kordo Wireless Mouse M3', 'Silent wireless mouse, Bluetooth and 2.4GHz, 12-month battery, 4000 DPI.', '/images/products/P144.svg', 899, 'Accessories', 'Kordo'),
('P145', 'Kordo Ergo Mouse Vertical', 'Vertical ergonomic mouse, rechargeable, 6 buttons, reduces wrist strain.', '/images/products/P145.svg', 1999, 'Accessories', 'Kordo'),
('P146', 'Lumina View 27 QHD Monitor', '27-inch QHD IPS monitor, 100Hz, USB-C 65W, height adjustable stand.', '/images/products/P146.svg', 21999, 'Accessories', 'Lumina'),
('P147', 'Lumina View 24 FHD Monitor', '24-inch FHD IPS monitor, 100Hz, HDMI and DisplayPort, slim bezels.', '/images/products/P147.svg', 9999, 'Accessories', 'Lumina'),
('P148', 'Kordo Laptop Stand Aluminium', 'Foldable aluminium laptop stand, 6 height levels, fits 11 to 16-inch laptops.', '/images/products/P148.svg', 1299, 'Accessories', 'Kordo'),
('P149', 'Kordo 1080p Webcam W2', 'Full HD webcam with autofocus, dual mics, privacy shutter, USB plug and play.', '/images/products/P149.svg', 2299, 'Accessories', 'Kordo');
