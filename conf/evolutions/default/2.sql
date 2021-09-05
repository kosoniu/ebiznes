# Users schema

# --- !Ups
CREATE TABLE "class" (
                       "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                       "name" VARCHAR NOT NULL,
                       "description" TEXT NOT NULL,
                       "health_points" INTEGER NOT NULL,
                       "health_points_on_higher_level" INTEGER NOT NULL,
                       "hit_dice" INTEGER NOT NULL
);

CREATE TABLE "class_proficiency" (
                      "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                      "class_id" INTEGER NOT NULL,
                      "proficiency_id" INTEGER NOT NULL
);

# --- !Downs

DROP TABLE "class";
DROP TABLE "class_proficiency";
