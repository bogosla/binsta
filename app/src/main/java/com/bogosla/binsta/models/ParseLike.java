package com.bogosla.binsta.models;


import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Like")
public class ParseLike extends ParseObject {
    public static final String USER_KEY = "user";
    public static final String POST_KEY = "post";

    public ParseUser getUser() {
        return getParseUser(USER_KEY);
    }
    public void setUser(ParseUser user) {
        put(USER_KEY, user);
    }

    public ParsePost getPost() {
        return (ParsePost) getParseObject(POST_KEY);
    }
    public void setPost(ParsePost post) {
        put(POST_KEY, post);
    }
}
