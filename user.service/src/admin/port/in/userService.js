import { User } from "../../user.js";

export class UserAdminService{
    
    userList = [];

    getAllUser(){
        return this.userList;
    }

    getUser(userId){
        const userFound = this.userList.find(current => current.externalId == userId);
        return userFound;
    }

    createUser(firstName,lastName, userId){
        const defaultStatus = 'ACTIVE';
        const currentDate = new Date();
        
        const user = new User(firstName,lastName,defaultStatus,currentDate.getTime(),userId);
        this.userList.push(user);
    }

    deleteUser(userId){
        this.userList = this.userList.filter(current => current.externalId != userId);
    }

}