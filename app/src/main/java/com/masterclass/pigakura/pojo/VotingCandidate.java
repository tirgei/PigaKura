package com.masterclass.pigakura.pojo;

/**
 * Created by tirgei on 6/19/17.
 */

public class VotingCandidate {

    private String candidateName, partyName, runningMate;
    private int id;

    public VotingCandidate(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VotingCandidate(int id, String candidateName, String partyName, String runningMate){
        this.id = id;
        this.candidateName = candidateName;
        this.partyName = partyName;
        this.runningMate = runningMate;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getRunningMate() {
        return runningMate;
    }

    public void setRunningMate(String runningMate) {
        this.runningMate = runningMate;
    }
}
