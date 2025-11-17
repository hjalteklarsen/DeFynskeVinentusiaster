SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE rating;
TRUNCATE TABLE wine;
TRUNCATE TABLE meeting_wine;
TRUNCATE TABLE meeting;
TRUNCATE TABLE member;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================
-- MEMBERS
-- ============================
INSERT INTO member (id, display_name, username, password, role)
VALUES
  (1, 'Test User', 'test',
   '$2a$10$CwTycUXWue0Thq9StjUM0uJ8Z/6yQ6Zz6E2zWq1cV5hRJzvM8yRz.', -- bcrypt of "test"
   'USER'),
  
  (2, 'Anna Jensen', 'anna', 
   '$2a$10$CwTycUXWue0Thq9StjUM0uJ8Z/6yQ6Zz6E2zWq1cV5hRJzvM8yRz.', 
   'USER'),

  (3, 'Mikkel Sørensen', 'mikkel', 
   '$2a$10$CwTycUXWue0Thq9StjUM0uJ8Z/6yQ6Zz6E2zWq1cV5hRJzvM8yRz.', 
   'USER'),

  (4, 'Maria Lunde', 'maria',
   '$2a$10$CwTycUXWue0Thq9StjUM0uJ8Z/6yQ6Zz6E2zWq1cV5hRJzvM8yRz.', 
   'USER');


-- ============================
-- MEETINGS
-- ============================
INSERT INTO meeting (id, title, description, date)
VALUES
  (1, 'Forårs Smagning', 'Lettere vine med fokus på friskhed og balance.', '2025-03-01 18:00:00'),
  (2, 'Rødvin Aften', 'Fyldige rødvine fra Italien, Spanien og Frankrig.', '2025-04-15 18:00:00');


-- ============================
-- WINES
-- ============================
INSERT INTO wine (id, name, country, year, grape)
VALUES
  (1, 'Barolo DOCG', 'Italien', 2018, 'Nebbiolo'),
  (2, 'Chardonnay Reserve', 'Frankrig', 2020, 'Chardonnay'),
  (3, 'Rioja Crianza', 'Spanien', 2019, 'Tempranillo'),
  (4, 'Sancerre Blanc', 'Frankrig', 2021, 'Sauvignon Blanc');


-- ============================
-- MEETING → WINES (join table)
-- ============================
INSERT INTO meeting_wine (meeting_id, wine_id)VALUES
  (1, 2),
  (1, 4),
  (2, 1),
  (2, 3);


-- ============================
-- RATINGS
-- ============================
INSERT INTO rating (id, member_id, wine_id, meeting_id, rating_value, comment)
VALUES
  (1, 1, 1, 2, 8, 'Intens og elegant.'),
  (2, 2, 3, 2, 7, 'Frugtig og afbalanceret.'),
  (3, 3, 2, 1, 9, 'Lækker Chardonnay med god fylde.'),
  (4, 4, 4, 1, 8, 'Frisk og mineralsk.'),
  (5, 1, 3, 2, 6, 'Lidt for tanninrig for min smag.');


-- ============================
-- If your rating seq exists (optional)
-- ============================
-- UPDATE rating_seq START WITH (SELECT MAX(id)+1 FROM rating);

