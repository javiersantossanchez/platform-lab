import express from 'express';
import { User } from './user.js';
import {UserAdminService} from './port/in/userService.js'

export class AdminRouter{

    constructor(){
        this.router = express.Router();
        this.userAdminService = new UserAdminService();
    }

    getRouter(){
        return this.router;
    }

    configurePaths(){
        this.getAllUserPath();
        this.getUserPath();
        this.createUserPath();
        this.deleteUserPath();
    }

    getAllUserPath(){
        /**
         * @swagger
         * /:
         *   get:
         *     description: Welcome to swagger-jsdoc!
         *     responses:
         *       200:
         *         description: Returns a mysterious string.
         */
        this.router.get("/", (req, res) => {
            const result = this.userAdminService.getAllUser()
            res.json(result);
          });
    }

    getUserPath(){
      /**
       * @swagger
       * /:
       *   get:
       *     description: Welcome to swagger-jsdoc!
       *     responses:
       *       200:
       *         description: Returns a mysterious string.
       */
      this.router.get("/:userExternalId", (req, res) => {
        const user = this.userAdminService.getUser(req.params.userExternalId);
          res.json(user);
        });
  }

    createUserPath(){
      /**
       * @swagger
       * /:
       *   get:
       *     description: Welcome to swagger-jsdoc!
       *     responses:
       *       200:
       *         description: Returns a mysterious string.
       */
      this.router.post("/", (req, res) => {
        this.userAdminService.createUser(req.body.firstName,req.body.lastName,req.body.externalId);
        res.send("User Created");
      });
    }

    deleteUserPath(){
      /**
       * @swagger
       * /:
       *   get:
       *     description: Welcome to swagger-jsdoc!
       *     responses:
       *       200:
       *         description: Returns a mysterious string.
       */
      this.router.delete("/:userExternalId", (req, res) => {
        this.userAdminService.deleteUser(req.params.userExternalId);
        res.send("User Deleted");
        });
    }
}

/*

const express = require("express");
const router = express.Router();

router.get("/", (req, res) => {
  res.send("Get all workouts");
});



router.get("/:workoutId", (req, res) => {
  res.send("Get an existing workout");
});

router.post("/", (req, res) => {
  res.send("Create a new workout");
});

router.patch("/:workoutId", (req, res) => {
  res.send("Update an existing workout");
});

router.delete("/:workoutId", (req, res) => {
  res.send("Delete an existing workout");
});



module.exports = router;*/