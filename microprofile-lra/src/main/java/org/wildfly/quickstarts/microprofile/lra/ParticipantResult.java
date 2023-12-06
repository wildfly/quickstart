package org.wildfly.quickstarts.microprofile.lra;

public class ParticipantResult {

    private String workLRAId;
    private String workRecoveryId;
    private String completeLRAId;
    private String completeRecoveryId;
    private String compensateLRAId;
    private String compensateRecoveryId;

    public ParticipantResult() {}

    public ParticipantResult(String workLRAId, String workRecoveryId,
                       String completeLRAId, String completeRecoveryId,
                       String compensateLRAId, String compensateRecoveryId) {
        this.workLRAId = workLRAId;
        this.workRecoveryId =  workRecoveryId;
        this.completeLRAId = completeLRAId;
        this.completeRecoveryId = completeRecoveryId;
        this.compensateLRAId = compensateLRAId;
        this.compensateRecoveryId = compensateRecoveryId;
    }

    public String getWorkLRAId() {
        return workLRAId;
    }

    public void setWorkLRAId(String workLRAId) {
        this.workLRAId = workLRAId;
    }

    public String getWorkRecoveryId() {
        return workRecoveryId;
    }

    public void setWorkRecoveryId(String workRecoveryId) {
        this.workRecoveryId = workRecoveryId;
    }

    public String getCompleteLRAId() {
        return completeLRAId;
    }

    public void setCompleteLRAId(String completeLRAId) {
        this.completeLRAId = completeLRAId;
    }

    public String getCompleteRecoveryId() {
        return completeRecoveryId;
    }

    public void setCompleteRecoveryId(String completeRecoveryId) {
        this.completeRecoveryId = completeRecoveryId;
    }

    public String getCompensateLRAId() {
        return compensateLRAId;
    }

    public void setCompensateLRAId(String compensateLRAId) {
        this.compensateLRAId = compensateLRAId;
    }

    public String getCompensateRecoveryId() {
        return compensateRecoveryId;
    }

    public void setCompensateRecoveryId(String compensateRecoveryId) {
        this.compensateRecoveryId = compensateRecoveryId;
    }

    @Override
    public String toString() {
        return "ParticipantResult{" +
            "workLRAId='" + workLRAId + '\'' +
            ", workRecoveryId='" + workRecoveryId + '\'' +
            ", completeLRAId='" + completeLRAId + '\'' +
            ", completeRecoveryId='" + completeRecoveryId + '\'' +
            ", compensateLRAId='" + compensateLRAId + '\'' +
            ", compensateRecoveryId='" + compensateRecoveryId + '\'' +
            '}';
    }
}
