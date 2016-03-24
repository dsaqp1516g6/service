package edu.upc.eetac.dsa.secretsites.entity;

/**
 * Created by Marti on 24/03/2016.
 */
public class SecretSitesError {
    private int status;
    private String reason;

    public SecretSitesError() {
    }

    public SecretSitesError(int status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
