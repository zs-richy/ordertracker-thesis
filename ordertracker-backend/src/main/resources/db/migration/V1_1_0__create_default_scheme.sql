create table images (
    id SERIAL PRIMARY KEY,
    type INTEGER,
    image_data VARCHAR,
    product_id INTEGER NOT NULL
);

create table order_items (
    id SERIAL PRIMARY KEY,
    count INTEGER NOT NULL,
    price INTEGER NOT NULL,
    order_id INTEGER,
    product_id INTEGER
);

create table orders
(
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    status INTEGER,
    status_last_modify TIMESTAMP WITHOUT TIME ZONE,
    user_id INTEGER
);

create table products
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    price INTEGER NOT NULL
);


create table user_role
(
    user_id INTEGER NOT NULL,
    roles VARCHAR(255)
);


create table users
(
    id       SERIAL PRIMARY KEY,
    password VARCHAR(255),
    username VARCHAR(255)
);


alter table if exists users
    add constraint c_uniq_name unique (username);


alter table if exists images
    add constraint fk_productid
    foreign key (product_id)
    references products;


alter table if exists order_items
    add constraint fk_orderid
    foreign key (order_id)
    references orders;


alter table if exists order_items
    add constraint fk_productid
    foreign key (product_id)
    references products;


alter table if exists user_role
    add constraint fk_userid
    foreign key (user_id)
    references users;