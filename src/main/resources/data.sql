-- Seed categories (ru)
insert into categories (name, language, image_filename)
select 'Кофе', 'ru', '0ef15b55-5572-4714-923c-e6a55b48d10c.jpg'
where not exists (select 1 from categories where name = 'Кофе' and language = 'ru');

insert into categories (name, language, image_filename)
select 'Чай', 'ru', '174198ec-4e5f-46b6-8499-4f6c4e589349.jpg'
where not exists (select 1 from categories where name = 'Чай' and language = 'ru');

insert into categories (name, language, image_filename)
select 'Десерты', 'ru', '187f428c-5c64-453f-898e-9cb22db28590.jpg'
where not exists (select 1 from categories where name = 'Десерты' and language = 'ru');

insert into categories (name, language, image_filename)
select 'Завтраки', 'ru', '2236848e-c074-40dc-85ea-2adc1cd2cbc0.jpg'
where not exists (select 1 from categories where name = 'Завтраки' and language = 'ru');

-- Seed categories (en)
insert into categories (name, language, image_filename)
select 'Coffee', 'en', '23928f16-516e-471a-bcd9-3c59e1d41990.jpg'
where not exists (select 1 from categories where name = 'Coffee' and language = 'en');

insert into categories (name, language, image_filename)
select 'Tea', 'en', '33d00ea9-70c9-468a-84f3-4309ec9c257e.jpg'
where not exists (select 1 from categories where name = 'Tea' and language = 'en');

insert into categories (name, language, image_filename)
select 'Desserts', 'en', '34e4ad15-ea4d-4ce1-a22d-b4c26776078e.jpg'
where not exists (select 1 from categories where name = 'Desserts' and language = 'en');

insert into categories (name, language, image_filename)
select 'Breakfast', 'en', '4e8d6851-2990-4d15-820f-4625c2fe0afa.jpg'
where not exists (select 1 from categories where name = 'Breakfast' and language = 'en');

-- Seed menu items (ru)
insert into menu_item (name, description, price, image_filename, category_id)
select 'Скибиди Латте', 'Нежный эспрессо с овсяным молоком.', 220.00,
       '6806705d-3cde-4635-a6d4-16dba5d0725d.jpg', c.id
from categories c
where c.name = 'Кофе' and c.language = 'ru'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Скибиди Латте' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Доп Доп Мокка', 'Тёмное какао, эспрессо, ваниль.', 240.00,
       '7e1dfa9c-bc04-4c04-904e-e6ed6c5e3b6b.jpg', c.id
from categories c
where c.name = 'Кофе' and c.language = 'ru'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Доп Доп Мокка' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Лесной мятный чай', 'Свежая мята и цедра цитрусов.', 160.00,
       '830c5bf4-d06f-421f-a0dd-b1743cd2576d.jpg', c.id
from categories c
where c.name = 'Чай' and c.language = 'ru'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Лесной мятный чай' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Ягодный ройбуш', 'Ройбуш, гибискус, ягоды.', 170.00,
       '8c09fa52-63ed-45f7-a4c5-27c4e8e667c0.jpg', c.id
from categories c
where c.name = 'Чай' and c.language = 'ru'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Ягодный ройбуш' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Карамельный чизкейк', 'Нежный чизкейк с солёной карамелью.', 260.00,
       '93ff58f0-81e8-4321-9e96-5f6ebbf88385.jpg', c.id
from categories c
where c.name = 'Десерты' and c.language = 'ru'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Карамельный чизкейк' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Тирамису Облако', 'Маскарпоне, эспрессо, какао.', 250.00,
       '9951c08e-7493-471d-9162-f54ff68843c2.jpg', c.id
from categories c
where c.name = 'Десерты' and c.language = 'ru'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Тирамису Облако' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Круассан с ветчиной', 'Хрустящий круассан с ветчиной и сыром.', 230.00,
       'a95539e5-3858-4c95-8593-0aa37b0a769e.jpg', c.id
from categories c
where c.name = 'Завтраки' and c.language = 'ru'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Круассан с ветчиной' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Утренняя гранола', 'Йогурт, гранола, ягоды.', 210.00,
       'c85fbb55-0c6f-4b1d-a1e7-4cc1fa8b277e.jpg', c.id
from categories c
where c.name = 'Завтраки' and c.language = 'ru'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Утренняя гранола' and mi.category_id = c.id
  );

-- Seed menu items (en)
insert into menu_item (name, description, price, image_filename, category_id)
select 'Skyline Latte', 'Silky espresso, vanilla foam.', 220.00,
       'cb322ab8-0185-44be-b038-ef595edc5c86.jpg', c.id
from categories c
where c.name = 'Coffee' and c.language = 'en'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Skyline Latte' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Nitro Cold Brew', 'Smooth cold brew with cream.', 240.00,
       'd71c3c8f-02b1-4aa2-9751-feb7f19d4f65.jpg', c.id
from categories c
where c.name = 'Coffee' and c.language = 'en'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Nitro Cold Brew' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Citrus Sencha', 'Sencha with lemon and honey.', 160.00,
       'de86e36d-5634-4b3f-be1c-d6628f5d65ee.jpg', c.id
from categories c
where c.name = 'Tea' and c.language = 'en'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Citrus Sencha' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Chai Bloom', 'Spiced black tea with milk.', 190.00,
       'efe479fa-93ef-4168-96b3-3dc5a685be02.jpg', c.id
from categories c
where c.name = 'Tea' and c.language = 'en'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Chai Bloom' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Brown Sugar Tart', 'Buttery tart, caramel crunch.', 260.00,
       'fba18624-ecbf-46bb-8e3c-b50f1791cea7.jpg', c.id
from categories c
where c.name = 'Desserts' and c.language = 'en'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Brown Sugar Tart' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Vanilla Cloud Cake', 'Soft sponge, vanilla cream.', 250.00,
       '0ef15b55-5572-4714-923c-e6a55b48d10c.jpg', c.id
from categories c
where c.name = 'Desserts' and c.language = 'en'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Vanilla Cloud Cake' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Salmon Bagel', 'Smoked salmon, cream cheese.', 250.00,
       '174198ec-4e5f-46b6-8499-4f6c4e589349.jpg', c.id
from categories c
where c.name = 'Breakfast' and c.language = 'en'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Salmon Bagel' and mi.category_id = c.id
  );

insert into menu_item (name, description, price, image_filename, category_id)
select 'Berry Oats Bowl', 'Overnight oats, berry mix.', 210.00,
       '187f428c-5c64-453f-898e-9cb22db28590.jpg', c.id
from categories c
where c.name = 'Breakfast' and c.language = 'en'
  and not exists (
    select 1 from menu_item mi where mi.name = 'Berry Oats Bowl' and mi.category_id = c.id
  );
