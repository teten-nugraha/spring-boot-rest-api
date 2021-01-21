DROP TABLE IF EXISTS users;

CREATE TABLE users(
    id INT AUTO_INCREMENT  PRIMARY KEY,
    name VARCHAR(20),
    email VARCHAR(20) UNIQUE,
    password varchar(100) NOT NULL
);

INSERT INTO users (name, email, password ) VALUES   ( 'admin', 'admin@mail.com', 'SampleP@s123' ),
                                                    ( 'johny', 'johny@mail.com', 'SampleP@s123' ),
                                                    ( 'ramazan', 'rmzn@mail.com', 'SampleP@s123' ),
                                                    ( 'teten-nugraha', 'teten@mail.com', 'SampleP@s123' );