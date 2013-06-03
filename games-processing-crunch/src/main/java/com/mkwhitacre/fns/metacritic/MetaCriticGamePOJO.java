package com.mkwhitacre.fns.metacritic;

/**
 * Simple POJO to make parsing JSON easier.
 */
class MetaCriticGamePOJO {

    public String gameName;
    public String metacriticScore;
    public String userScore;

    public MetaCriticGamePOJO(){
    }

    public MetaCriticGamePOJO(String gameName, String metacriticScore, String userScore){
        this.gameName = gameName;
        this.metacriticScore = metacriticScore;
        this.userScore = userScore;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(final String gameName) {
        this.gameName = gameName;
    }

    public String getMetacriticScore() {
        return metacriticScore;
    }

    public void setMetacriticScore(final String metacriticScore) {
        this.metacriticScore = metacriticScore;
    }

    public String getUserScore() {
        return userScore;
    }

    public void setUserScore(final String userScore) {
        this.userScore = userScore;
    }
}
