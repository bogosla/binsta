package com.bogosla.binsta.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class ParseComment extends ParseObject {
    public static final String TEXT_KEY = "text";
    public static final String USER_KEY = "user";
    public static final String POST_KEY = "post";

    public String getText() {
        return getString(TEXT_KEY);
    }
    public void setText(String description) {
        put(TEXT_KEY, description);
    }

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
