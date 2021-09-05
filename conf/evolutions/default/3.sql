# Users schema

# --- !Ups
CREATE TABLE "race_feature" (
                       "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                       "name" VARCHAR NOT NULL,
                       "description" TEXT NOT NULL,
                       "race_id" INTEGER NOT NULL
);

# --- !Downs

DROP TABLE "race_feature";
