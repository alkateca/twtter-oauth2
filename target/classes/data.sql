CREATE TABLE IF NOT EXISTS tb_roles (
                                        role_id INT PRIMARY KEY,
                                        name VARCHAR(50) NOT NULL
);
INSERT IGNORE INTO tb_roles (role_id, name) VALUES (1, "admin");
INSERT IGNORE INTO tb_roles (role_id, name) VALUES (2, "basic");