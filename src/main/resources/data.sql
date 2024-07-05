INSERT INTO member (name, email, password, role) VALUES ('어드민', 'admin@email.com', 'password1', 'USER');
INSERT INTO member (name, email, password, role) VALUES ('유저1', 'user@email.com', 'password2', 'USER');
INSERT INTO member (name, email, password, role) VALUES ('유저2', 'user2@email.com', 'password3', 'USER');

INSERT INTO time (start_at) VALUES ('10:00');
INSERT INTO time (start_at) VALUES ('11:00');
INSERT INTO time (start_at) VALUES ('12:00');

INSERT INTO theme (name, description, thumbnail) VALUES ('Theme 1', 'Description 1', 'thumbnail1.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('Theme 2', 'Description 1', 'thumbnail1.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('Theme 3', 'Description 1', 'thumbnail1.jpg');

INSERT INTO reservation (name, date, status, member_id, time_id, theme_id) VALUES ('어드민1', '2024-07-15', '예약', 1, 1, 1);
INSERT INTO reservation (name, date, status, member_id, time_id, theme_id) VALUES ('유저1', '2024-07-16', '예약', 2, 2, 2);
INSERT INTO reservation (name, date, status, member_id, time_id, theme_id) VALUES ('유저1', '2024-07-17', '예약', 3, 3, 3);

UPDATE member SET role = 'ADMIN' WHERE id = 1;