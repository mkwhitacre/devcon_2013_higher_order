package com.mkwhitacre;


import com.mkwhitacre.avro.MetaCriticGame;
import com.mkwhitacre.avro.VgChartzGame;

public class ModelHelper {

    public static VgChartzGame createVgChartz(String name, String platform, String genre){
        return VgChartzGame.newBuilder().setName(name).setPlatform(platform).setGenre(genre).build();
    }

    public static MetaCriticGame createMetaCriticGame(String name, int score, float userScore){
        return MetaCriticGame.newBuilder().setName(name).setScore(score).setUserScore(userScore)
                .build();
    }

}
