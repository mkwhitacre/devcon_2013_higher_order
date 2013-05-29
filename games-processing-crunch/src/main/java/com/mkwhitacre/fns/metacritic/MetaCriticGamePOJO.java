package com.mkwhitacre.fns.metacritic;

class MetaCriticGamePOJO {

//    {"name": "name", "type": "string", "default": "invalid" },
//    {"name": "platform", "type": "string", "default": "invalid"},
//    {"name": "score", "type": "int", "default": -1},
//    {"name": "user_score", "type":"float", "default": -1}]

//    {"gameName":"Ace Combat: Assault Horizon Legacy","metacriticScore":"71","userScore":"7.7"}

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
