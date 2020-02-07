package com.rrcc.appcultural20;

public class Favoritos {
    private String postKey;
    private String userId;
    private String fav1;
    private String fav2;
    private String fav3;

    public Favoritos(){}
    public Favoritos(String userId, String fav1, String fav2, String fav3) {
        this.userId = userId;
        this.fav1 = fav1;
        this.fav2 = fav2;
        this.fav3 = fav3;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFav1() {
        return fav1;
    }

    public void setFav1(String fav1) {
        this.fav1 = fav1;
    }

    public String getFav2() {
        return fav2;
    }

    public void setFav2(String fav2) {
        this.fav2 = fav2;
    }

    public String getFav3() {
        return fav3;
    }

    public void setFav3(String fav3) {
        this.fav3 = fav3;
    }
}
