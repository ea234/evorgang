
INSERT INTO evg_auth_users ( username, password, enable, locked ) VALUES
('admin',  '{noop}geheim', 1, 0 ),
('root',  '{noop}geheim', 1, 0 ),
('user',  '{noop}geheim', 1, 0 );


INSERT INTO evg_auth_authorities ( authority, user_entity_id ) VALUES
('ROLE_ADMIN',  1),
('ROLE_USER',  1),
('ROLE_ADMIN',  2),
('ROLE_USER',  2),
('ROLE_USER',  3);
