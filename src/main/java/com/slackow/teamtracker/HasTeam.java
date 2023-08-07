package com.slackow.teamtracker;

public interface HasTeam {
    default void setTeam(String s) {}
    default String getTeam(){return null;}
}
