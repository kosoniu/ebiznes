# Users schema

# --- !Ups

CREATE TABLE "race" (
                        "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        "name" VARCHAR NOT NULL,
                        "description" TEXT NOT NULL
);

CREATE TABLE "class" (
                       "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                       "name" VARCHAR NOT NULL,
                       "description" TEXT NOT NULL,
                       "health_points" INTEGER NOT NULL,
                       "hit_dice" VARCHAR NOT NULL
);

CREATE TABLE "origin" (
                         "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                         "name" VARCHAR NOT NULL,
                         "description" TEXT NOT NULL
);

CREATE TABLE "proficiency" (
                           "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                           "name" VARCHAR NOT NULL,
                           "type" INTEGER NOT NULL
);

CREATE TABLE "origin_proficiency" (
                          "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                          "origin_id" INTEGER NOT NULL,
                          "proficiency_id" INTEGER NOT NULL
);

# --- !Downs

DROP TABLE "race";
DROP TABLE "class";
DROP TABLE "origin";
DROP TABLE "proficiency";
DROP TABLE "origin_proficiency";
