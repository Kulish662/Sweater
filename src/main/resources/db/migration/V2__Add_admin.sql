insert into usr (id, username, password, active)
    values (1, 'admin', '1', true);

insert into user_role (user_id, roles)
    VALUES (1, 'ADMIN'), (1, 'USER');