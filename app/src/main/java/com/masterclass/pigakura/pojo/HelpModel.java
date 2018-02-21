package com.masterclass.pigakura.pojo;

/**
 * Created by root on 7/19/17.
 */

public class HelpModel {
    private String profileHelp, votingHelp, resultsHelp, forumHelp;

    public HelpModel(){}

    public HelpModel(String profileHelp, String votingHelp, String resultsHelp, String forumHelp){
        this.profileHelp = profileHelp;
        this.votingHelp = votingHelp;
        this.resultsHelp = resultsHelp;
        this.forumHelp = forumHelp;
    }

    public String getProfileHelp() {
        return profileHelp;
    }

    public void setProfileHelp(String profileHelp) {
        this.profileHelp = profileHelp;
    }

    public String getVotingHelp() {
        return votingHelp;
    }

    public void setVotingHelp(String votingHelp) {
        this.votingHelp = votingHelp;
    }

    public String getResultsHelp() {
        return resultsHelp;
    }

    public void setResultsHelp(String resultsHelp) {
        this.resultsHelp = resultsHelp;
    }

    public String getForumHelp() {
        return forumHelp;
    }

    public void setForumHelp(String forumHelp) {
        this.forumHelp = forumHelp;
    }
}
