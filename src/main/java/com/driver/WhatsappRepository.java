package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below-mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    // Check if User Exists in Database
    public boolean checkUser(String mobile){
        return userMobile.contains(mobile);
    }

    // Add User to the Database
    public String addUser(User user){
        userMobile.add(user.getMobile());

        return "SUCCESS";
    }

    // Add Group to Database
    public void addGroup(Group group, List<User> users){
        groupUserMap.put(group, users); // group and list of users
        groupMessageMap.put(group, new ArrayList<>()); // group and its messages
        adminMap.put(group,users.get(0)); // make first person as admin
    }

    // Add message to Database
    public void addMessage(Message message){
        senderMap.put(message,null);
    }

    // Check if the group exists
    public boolean checkGroup(Group group){
        return groupUserMap.containsKey(group);
    }

    // Check if the user exists in the froup
    public boolean checkUserInGroup(Group group, User user){
        return groupUserMap.get(group).stream().anyMatch(users -> users == user);
    }

    // Send Message in the Group
    public int sendMessageToGroup(Group group, User sender, Message message){
        groupMessageMap.get(group).add(message);
        senderMap.put(message, sender);

        return groupMessageMap.get(group).size();
    }

    // Check the appriover is the admin of the group
    public boolean checkAdmin(Group group, User approver){
        return adminMap.get(group) == approver;
    }

    // Transfer admin rights to another user
    public void transferAdmin(Group group, User user){
        adminMap.put(group, user);
    }

    // Check if the User Exists in any of the group
    public Group checkUserExistence(User user){
        return groupUserMap.keySet().stream().
                filter(group -> groupUserMap.get(group).stream().anyMatch(user1 -> user1==user)).
                findFirst().orElse(null);
    }

     // Remove user from the database
    public int removeUser(Group group, User user){
        groupUserMap.get(group).remove(user);

        senderMap.keySet().stream().filter(message -> senderMap.get(message) == user).forEach(message -> {
            senderMap.remove(message);
            groupMessageMap.get(group).remove(message);
        });

        return groupUserMap.get(group).size() + groupMessageMap.get(group).size() + senderMap.size();
    }

    // Getters and Setters

    public int getCustomGroupCount() {
        return customGroupCount;
    }

    public void setCustomGroupCount(int customGroupCount) {
        this.customGroupCount = customGroupCount;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}