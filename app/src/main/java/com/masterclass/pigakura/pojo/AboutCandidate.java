package com.masterclass.pigakura.pojo;

/**
 * Created by tirgei on 6/20/17.
 */

public class AboutCandidate {
    private String addKey;
    private int candidateId;
    private String picUrl;
    private String candidateName;
    private String partyName;
    private String aboutCandidate;
    private String aboutManifesto;
    private String runningMate;
    private String runningMatePic;
    private String partyPic;
    private int votes;

    public AboutCandidate(){

    }

    public AboutCandidate(int candidateId, String candidateName, String runningMate, String partyName, String aboutCandidate, String aboutManifesto){
        this.aboutCandidate = aboutCandidate;
        this.candidateName = candidateName;
        this.partyName = partyName;
        this.aboutManifesto = aboutManifesto;
        this.runningMate = runningMate;
        this.candidateId = candidateId;

    }

    public AboutCandidate(String addKey, int candidateId, String picUrl, String candidateName, String runningMate, String partyName, String aboutCandidate, String aboutManifesto){
        this.aboutCandidate = aboutCandidate;
        this.picUrl = picUrl;
        this.candidateName = candidateName;
        this.partyName = partyName;
        this.aboutManifesto = aboutManifesto;
        this.runningMate = runningMate;
        this.candidateId = candidateId;
        this.addKey = addKey;

    }

    public String getAddKey() {
        return addKey;
    }

    public void setAddKey(String addKey) {
        this.addKey = addKey;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getRunningMatePic() {
        return runningMatePic;
    }

    public void setRunningMatePic(String runningMatePic) {
        this.runningMatePic = runningMatePic;
    }

    public String getPartyPic() {
        return partyPic;
    }

    public void setPartyPic(String partyPic) {
        this.partyPic = partyPic;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getAboutManifesto() {
        return aboutManifesto;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public String getRunningMate() {
        return runningMate;
    }

    public void setRunningMate(String runningMate) {
        this.runningMate = runningMate;
    }


    public void setAboutManifesto(String aboutManifesto) {
        this.aboutManifesto = aboutManifesto;
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

    public String getAboutCandidate() {
        return aboutCandidate;
    }

    public void setAboutCandidate(String aboutCandidate) {
        this.aboutCandidate = aboutCandidate;
    }


}
