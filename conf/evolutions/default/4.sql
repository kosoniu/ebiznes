# Users schema

# --- !Ups
CREATE TABLE "hero" (
                        "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        "level" INTEGER NOT NULL,
                        "name" INTEGER NOT NULL,
                        "race_id" INTEGER NOT NULL,
                        "class_id" INTEGER NOT NULL,
                        "origin_id" INTEGER NOT NULL

);

# --- !Downs

DROP TABLE "hero";
