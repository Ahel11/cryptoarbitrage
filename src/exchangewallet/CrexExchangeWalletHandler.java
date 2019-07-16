package exchangewallet;

import model.WalletStatusHolder;

import java.util.ArrayList;

public class CrexExchangeWalletHandler {

    public static void parseSource(String source) {
        ArrayList<WalletStatusHolder> walletStatusHolders = new ArrayList<>();

        String splitted[] = source.split("%");
        int j=0;
        for(int i=0; i<splitted.length-1; i++) {
            String currString = splitted[i];
            System.out.print(currString + "\n");
            String currencyName = splitted[i+1];

            boolean isFoundCurrency = isFoundCurrency(currString, currencyName);
            if(isFoundCurrency){
                WalletStatusHolder currHolder = getHolderFromIndex(i, splitted);
                currencyName = currencyName.replace("3A", "");
                currHolder.setPair(currencyName);
                walletStatusHolders.add(currHolder);
                i=i+2;
            }
        }

    }

    public static WalletStatusHolder getHolderFromIndex(int index, String splitted[]) {
        WalletStatusHolder holder = new WalletStatusHolder();
        Boolean depositAllowed = new Boolean(false);
        Boolean withdrawalAllowed = new Boolean(false);

        for(int j=index; j<splitted.length; j++) {
            if(splitted[j].contains("IsFiatMoney")) {
                break;
            }

            if(splitted[j].contains("DepositsAllowed")) {
                String boolVal = splitted[j+2];
                boolVal = boolVal.replace("3A", "");
                depositAllowed = new Boolean(boolVal);
            }
            if(splitted[j].contains("WithdrawalsAllowed")) {
                String boolVal = splitted[j+2];
                boolVal = boolVal.replace("3A", "");
                withdrawalAllowed = new Boolean(boolVal);
            }
        }
        holder.setWalletDepositEnabled(depositAllowed);
        holder.setWalletWithdrawalEnabled(withdrawalAllowed);
        return holder;
    }

    public static boolean isFoundCurrency(String currString, String frontString) {
        if(currString.contains("Currency")
                && !currString.contains("CurrencyInfoItem")
                && !frontString.contains("Info")
                && !frontString.contains("22")) {
            return true;
        }
        return false;
    }

}
