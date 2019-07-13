package model;

import java.text.DecimalFormat;

public class ProfitResult {

    private double volumeUsed = 0;
    private double profitUsd = 0;
    private double amountToSendCrypto= 0;
    private double amountToSendUSD = 0;
    private DecimalFormat formatC = new DecimalFormat("#.######");
    private DecimalFormat formatUSD = new DecimalFormat("#.##");


    public double getVolumeUsed() {
        return volumeUsed;
    }

    public void setVolumeUsed(double volumeUsed) {
        this.volumeUsed = volumeUsed;
    }

    public double getProfitUsd() {
        return profitUsd;
    }

    public void setProfitUsd(double profitUsd) {
        this.profitUsd = profitUsd;
    }

    public double getAmountToSendCrypto() {
        return amountToSendCrypto;
    }

    public void setAmountToSendCrypto(double amountToSendCrypto) {
        this.amountToSendCrypto = amountToSendCrypto;
    }

    public double getAmountToSendUSD() {
        return amountToSendUSD;
    }

    public void setAmountToSendUSD(double amountToSendUSD) {
        this.amountToSendUSD = amountToSendUSD;
    }

    public String toString() {
        return "[" + formatUSD.format(amountToSendUSD) + ":" + formatC.format(amountToSendCrypto) + "]";
    }

}




























































