CREATE TABLE note (
      id IDENTITY PRIMARY KEY,
      title VARCHAR(100) NOT NULL CHECK (LENGTH(title) >= 2 AND LENGTH(title) <= 100),
      content VARCHAR(1000) NOT NULL CHECK (LENGTH(content) <= 1000)
);