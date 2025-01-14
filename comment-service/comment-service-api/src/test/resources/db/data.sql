DELETE FROM comment;
DELETE FROM news;

INSERT INTO news (id, created_at, title, text, author)
VALUES ('45f1ab38-8678-4271-a0b2-82f5c4de549c', NOW(), 'News Title', 'News content for News Title', 'crazy'),
       ('366421fb-dea9-476a-b655-0a1aad1e9af1', NOW(), 'News 2', 'Content news 2', 'crazy'),
       ('48f7837b-9a80-4d64-b6ec-1f33293b0733', NOW(), 'News 3', 'Content news 3', 'crazy');

INSERT INTO comment (id, created_at, text, author, news_id)
VALUES ('4a1147e2-17f5-4c20-8558-c543a2e6ab78', NOW(), 'First comment', 'kitty', '45f1ab38-8678-4271-a0b2-82f5c4de549c'),
       ('b6c4f1bc-4553-48f1-b6c6-f5688c36fd19', NOW(), 'This is a comment', 'doberman', '45f1ab38-8678-4271-a0b2-82f5c4de549c'),
       ('b446ed60-f4c3-4d6f-bc68-1392a3608cc1', NOW(), 'This is a comment Sarah back', 'tara', '366421fb-dea9-476a-b655-0a1aad1e9af1'),
       ('c467f5f1-b42a-4aa2-8048-692cc9d94743', NOW(), 'This is a comment', 'nick', '366421fb-dea9-476a-b655-0a1aad1e9af1');