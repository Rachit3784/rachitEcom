package com.example.ecommerce;

public class ContactUserProfile {

   private String FriendUID;
   private String Name;
   private String ProfileURl;
   private String MyUID;
  private  String Myname;
  private String Myprofileurl;

    public ContactUserProfile(){

    }

    public ContactUserProfile(String friendUID, String name, String profileURl, String myUID, String myname, String myprofileurl) {
        FriendUID = friendUID;
        Name = name;
        ProfileURl = profileURl;
        MyUID = myUID;
        Myname = myname;
        Myprofileurl = myprofileurl;
    }


    public String getFriendUID() {
        return FriendUID;
    }

    public void setFriendUID(String friendUID) {
        FriendUID = friendUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfileURl() {
        return ProfileURl;
    }

    public void setProfileURl(String profileURl) {
        ProfileURl = profileURl;
    }

    public String getMyUID() {
        return MyUID;
    }

    public void setMyUID(String myUID) {
        MyUID = myUID;
    }

    public String getMyname() {
        return Myname;
    }

    public void setMyname(String myname) {
        Myname = myname;
    }

    public String getMyprofileurl() {
        return Myprofileurl;
    }

    public void setMyprofileurl(String myprofileurl) {
        Myprofileurl = myprofileurl;
    }
}
