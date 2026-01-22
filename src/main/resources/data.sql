-- Seed categories
insert into categories (name, language, image_filename)
select 'Coffee', 'ru', '1763369959398_photo_2025-11-14_19-49-36.jpg'
where not exists (select 1 from categories where name = 'Coffee' and language = 'ru');

insert into categories (name, language, image_filename)
select 'Tea', 'ru', '33d00ea9-70c9-468a-84f3-4309ec9c257e.jpg'
where not exists (select 1 from categories where name = 'Tea' and language = 'ru');

insert into categories (name, language, image_filename)
select 'Desserts', 'ru', '4e8d6851-2990-4d15-820f-4625c2fe0afa.jpg'
where not exists (select 1 from categories where name = 'Desserts' and language = 'ru');

insert into categories (name, language, image_filename)
select 'Breakfast', 'ru', '64f67683-2b29-44a1-a3c7-bffc88a9a150.jpg'
where not exists (select 1 from categories where name = 'Breakfast' and language = 'ru');

insert into categories (name, language, image_filename)
select 'Coffee', 'en', '6806705d-3cde-4635-a6d4-16dba5d0725d.jpg'
where not exists (select 1 from categories where name = 'Coffee' and language = 'en');

insert into categories (name, language, image_filename)
select 'Tea', 'en', '93ff58f0-81e8-4321-9e96-5f6ebbf88385.jpg'
where not exists (select 1 from categories where name = 'Tea' and language = 'en');

insert into categories (name, language, image_filename)
select 'Desserts', 'en', 'efe479fa-93ef-4168-96b3-3dc5a685be02.jpg'
where not exists (select 1 from categories where name = 'Desserts' and language = 'en');

insert into categories (name, language, image_filename)
select 'Breakfast', 'en', '33d00ea9-70c9-468a-84f3-4309ec9c257e.jpg'
where not exists (select 1 from categories where name = 'Breakfast' and language = 'en');

-- Seed menu items (ru)
insert into menu_item (name, description, price, image_filename, category_id)
select 'Skibidi Latte', 'Velvety espresso with oat milk.', 220.00,
       '1763369959398_photo_2025-11-14_19-49-36.jpg', c.id
from categories c
where c.name = 'Coffee' and c.language = 'ru'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Skibidi Latte' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Dop Dop Mocha', 'Dark cocoa, espresso, vanilla.', 240.00,
       '6806705d-3cde-4635-a6d4-16dba5d0725d.jpg', c.id
from categories c
where c.name = 'Coffee' and c.language = 'ru'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Dop Dop Mocha' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Forest Mint Tea', 'Fresh mint and citrus peel.', 160.00,
       '93ff58f0-81e8-4321-9e96-5f6ebbf88385.jpg', c.id
from categories c
where c.name = 'Tea' and c.language = 'ru'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Forest Mint Tea' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Berry Rooibos', 'Rooibos, hibiscus, berries.', 170.00,
       '33d00ea9-70c9-468a-84f3-4309ec9c257e.jpg', c.id
from categories c
where c.name = 'Tea' and c.language = 'ru'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Berry Rooibos' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Caramel Cheesecake', 'Creamy cheesecake, salted caramel.', 260.00,
       '4e8d6851-2990-4d15-820f-4625c2fe0afa.jpg', c.id
from categories c
where c.name = 'Desserts' and c.language = 'ru'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Caramel Cheesecake' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Tiramisu Cloud', 'Mascarpone, espresso, cocoa.', 250.00,
       'efe479fa-93ef-4168-96b3-3dc5a685be02.jpg', c.id
from categories c
where c.name = 'Desserts' and c.language = 'ru'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Tiramisu Cloud' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Ham Croissant', 'Buttery croissant with ham and cheese.', 230.00,
       '64f67683-2b29-44a1-a3c7-bffc88a9a150.jpg', c.id
from categories c
where c.name = 'Breakfast' and c.language = 'ru'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Ham Croissant' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Morning Granola', 'Yogurt, granola, berries.', 210.00,
       '93ff58f0-81e8-4321-9e96-5f6ebbf88385.jpg', c.id
from categories c
where c.name = 'Breakfast' and c.language = 'ru'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Morning Granola' and mi.category_id = c.id
  );

-- Seed menu items (en)
insert into menu_item (name, description, price, image_filename, category_id)
select 'Skyline Latte', 'Silky espresso, vanilla foam.', 220.00,
       '6806705d-3cde-4635-a6d4-16dba5d0725d.jpg', c.id
from categories c
where c.name = 'Coffee' and c.language = 'en'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Skyline Latte' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Nitro Cold Brew', 'Smooth cold brew with cream.', 240.00,
       '1763369959398_photo_2025-11-14_19-49-36.jpg', c.id
from categories c
where c.name = 'Coffee' and c.language = 'en'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Nitro Cold Brew' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Citrus Sencha', 'Sencha with lemon and honey.', 160.00,
       '93ff58f0-81e8-4321-9e96-5f6ebbf88385.jpg', c.id
from categories c
where c.name = 'Tea' and c.language = 'en'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Citrus Sencha' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Chai Bloom', 'Spiced black tea with milk.', 190.00,
       '33d00ea9-70c9-468a-84f3-4309ec9c257e.jpg', c.id
from categories c
where c.name = 'Tea' and c.language = 'en'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Chai Bloom' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Brown Sugar Tart', 'Buttery tart, caramel crunch.', 260.00,
       '4e8d6851-2990-4d15-820f-4625c2fe0afa.jpg', c.id
from categories c
where c.name = 'Desserts' and c.language = 'en'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Brown Sugar Tart' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Vanilla Cloud Cake', 'Soft sponge, vanilla cream.', 250.00,
       'efe479fa-93ef-4168-96b3-3dc5a685be02.jpg', c.id
from categories c
where c.name = 'Desserts' and c.language = 'en'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Vanilla Cloud Cake' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Salmon Bagel', 'Smoked salmon, cream cheese.', 250.00,
       '64f67683-2b29-44a1-a3c7-bffc88a9a150.jpg', c.id
from categories c
where c.name = 'Breakfast' and c.language = 'en'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Salmon Bagel' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Berry Oats Bowl', 'Overnight oats, berry mix.', 210.00,
       '93ff58f0-81e8-4321-9e96-5f6ebbf88385.jpg', c.id
from categories c
where c.name = 'Breakfast' and c.language = 'en'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Berry Oats Bowl' and mi.category_id = c.id
  );
