package model;

public class WalletStatusHolder {

    private boolean isWalletDepositEnabled;
    private boolean isWalletWithdrawalEnabled;
    private String pair;

    public boolean isWalletDepositEnabled() {
        return isWalletDepositEnabled;
    }

    public void setWalletDepositEnabled(boolean walletDepositEnabled) {
        isWalletDepositEnabled = walletDepositEnabled;
    }

    public boolean isWalletWithdrawalEnabled() {
        return isWalletWithdrawalEnabled;
    }

    public void setWalletWithdrawalEnabled(boolean walletWithdrawalEnabled) {
        isWalletWithdrawalEnabled = walletWithdrawalEnabled;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    @Override
    public String toString() {
        return "WalletStatusHolder{" +
                "isWalletDepositEnabled=" + isWalletDepositEnabled +
                ", isWalletWithdrawalEnabled=" + isWalletWithdrawalEnabled +
                ", pair='" + pair + '\'' +
                '}';
    }
}
