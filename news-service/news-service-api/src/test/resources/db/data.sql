TRUNCATE TABLE news RESTART IDENTITY;

INSERT INTO news (id, created_at, title, text, author)
VALUES ('45f1ab38-8678-4271-a0b2-82f5c4de549c', NOW(), 'News Title', 'News content for News Title', 'test'),
       ('366421fb-dea9-476a-b655-0a1aad1e9af1', NOW(), 'News 2', 'Content news 2', 'test'),
       ('48f7837b-9a80-4d64-b6ec-1f33293b0733', NOW(), 'News 3', 'Content news 3', 'test');
