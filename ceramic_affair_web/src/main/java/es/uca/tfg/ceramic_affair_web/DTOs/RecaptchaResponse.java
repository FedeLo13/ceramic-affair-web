package es.uca.tfg.ceramic_affair_web.DTOs;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object (DTO) para la respuesta de reCAPTCHA.
 * Este DTO se utiliza para encapsular la respuesta del servicio de reCAPTCHA,
 * incluyendo información sobre la validez del token y posibles errores.
 * 
 * @version 1.0
 */
public class RecaptchaResponse {

    private boolean success;
    private float score;
    private String action;
    private LocalDateTime challengeTs;
    private String hostname;
    private List<String> errorCodes;

    public RecaptchaResponse() {
        // Constructor por defecto
    }

    public RecaptchaResponse(boolean success, float score, String action, LocalDateTime challengeTs, String hostname, List<String> errorCodes) {
        this.success = success;
        this.score = score;
        this.action = action;
        this.challengeTs = challengeTs;
        this.hostname = hostname;
        this.errorCodes = errorCodes;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getChallengeTs() {
        return challengeTs;
    }

    public void setChallengeTs(LocalDateTime challengeTs) {
        this.challengeTs = challengeTs;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<String> errorCodes) {
        this.errorCodes = errorCodes;
    }

    @Override
    public String toString() {
        return "RecaptchaResponse{" +
                "success=" + success +
                ", score=" + score +
                ", action='" + action + '\'' +
                ", challengeTs='" + challengeTs + '\'' +
                ", hostname='" + hostname + '\'' +
                ", errorCodes=" + errorCodes +
                '}';
    }
}