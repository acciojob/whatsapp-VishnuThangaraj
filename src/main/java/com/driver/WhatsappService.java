package com.driver;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WhatsappService {
    WhatsappRepository whatsappRepository = new WhatsappRepository();

    // Create User
    public String createUser(String name, String mobile)throws Exception{
        // check if the mobile already exists
        if(whatsappRepository.checkUser(mobile))
            throw new Exception("User already exists");

        User user = new User(name, mobile); // Create new User
        return whatsappRepository.addUser(user); // Add user to the Database
    }

    // Create New Group
    public Group createGroup(List<User> users){
        Group group = new Group(); // Create Group

        // check the size of the Users if user is 2, then personal chat
        if(users.size() == 2){
            group.setName(users.get(1).getName()); // Name of group as second person-name in list
        }else{
            whatsappRepository.setCustomGroupCount(whatsappRepository.getCustomGroupCount()+1);
            group.setName("Group "+whatsappRepository.getCustomGroupCount()); // Name with group count
        }
        group.setNumberOfParticipants(users.size());
        whatsappRepository.addGroup(group, users); // Add group to Database

        return group;
    }

    // Create Message
    public int createMessage(String content){
        whatsappRepository.setMessageId(whatsappRepository.getMessageId()+1);
        int messageId = whatsappRepository.getMessageId();

        Message message = new Message(messageId, content, new Date(System.currentTimeMillis()));
        whatsappRepository.addMessage(message);

        return messageId;
    }

    // Send message to a group
    public int sendMessage(Message message, User sender, Group group) throws Exception{
        // check if the group exists
        if(!whatsappRepository.checkGroup(group))
            throw new Exception("Group does not exist");

        // check if the sender exists in the group
        if(!whatsappRepository.checkUserInGroup(group, sender))
            throw new Exception("You are not allowed to send message");

        // Send Message in the group
        return whatsappRepository.sendMessageToGroup(group, sender, message);
    }

    // Change admin of the group
    public String changeAdmin(User approver, User user, Group group) throws Exception{
        // check if the group exists
        if(!whatsappRepository.checkGroup(group))
            throw new Exception("Group does not exist");

        // Check the approver is admin of the Group
        if(!whatsappRepository.checkAdmin(group, approver))
            throw new Exception("Approver does not have rights");

        // check if the sender exists in the group
        if(!whatsappRepository.checkUserInGroup(group, user))
            throw new Exception("User is not a participant");

        whatsappRepository.transferAdmin(group, user);

        return "SUCCESS";
    }

    // Remove User from the Group
    public int removeUser(User user) throws Exception{
        Group group = whatsappRepository.checkUserExistence(user);

        // check if the user exists in any group
        if(group == null)
            throw new Exception("User not found");

        // check the user is admin
        if(whatsappRepository.checkAdmin(group, user))
            throw new Exception("Cannot remove admin");

        // Remove user from the group
        return whatsappRepository.removeUser(group, user);
    }

    // Find the larges Message in range
    public String findMessage(Date start, Date end, int K) throws Exception{
        return "Success";
    }
}
