package ir.ac.kntu;

import java.util.Random;

enum Method {ACCOUNT, CONTACT}

public class TransferReceipt extends Receipt {
    private User transmitter;
    private User receiver;
    private String transferId;
    private Method method;

    public TransferReceipt(long amount, User transmitter, User receiver, Method method) {
        super(amount);
        this.transmitter = transmitter;
        this.receiver = receiver;
        this.transferId = generateTransferID();
        this.method = method;
    }


    private String generateTransferID() {
        String transferID = "";
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            transferID += String.valueOf(random.nextInt(10));
        }
        return transferID;
    }

    @Override
    public String toString() {
        if(method.equals(Method.CONTACT)){
            return super.toString()  + "Transfer method : " +  "to contact" +
                    '\n' + "Transmitter : " + transmitter.getName() + " " +
                    transmitter.getLastName() + '\n' + "Receiver : " +
                    transmitter.findContact(receiver.getPhoneNumber()).getName() + " " +
                    transmitter.findContact(receiver.getPhoneNumber()).getLastName() + '\n' +
                    "Transfer ID : " + transferId + '\n';
        }
        return super.toString() + "Transfer method : " + "by AccountID" +
                '\n' +  "Transmitter : " + transmitter.getName() + " " +
                transmitter.getLastName() + '\n' + "Receiver : " +
                receiver.getName() + " " + receiver.getLastName() + '\n' +
                "Transfer ID : " + transferId + '\n' ;
    }
}

